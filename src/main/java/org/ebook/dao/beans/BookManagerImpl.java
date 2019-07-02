package org.ebook.dao.beans;

import org.ebook.dao.BookManager;
import org.ebook.entity.Book;
import org.ebook.entity.BookCategory;
import org.ebook.entity.BookImage;
import org.ebook.util.DatabaseQuery;
import org.ebook.util.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookManagerImpl implements BookManager {
    @Autowired
    private MongoTemplate mongo;

    private List<BookCategory> getBookCategories(){
        List<BookCategory> cs =
                DatabaseUtils.createQuery(BookCategory.class).getResult();
        return cs;
    }
    private List<Book> getBooks(BookCategory category){
        List<Book> books = null;
        if(category != null){
            books = DatabaseUtils.createQuery(Book.class)
                    .equal("categoryId", category.getId())
                    .getResult();
        }else{
            books = DatabaseUtils.createQuery(Book.class)
                    .isNull("categoryId")
                    .getResult();
        }
        return books;
    }
    private List<Book> getBooks(BookCategory category, List<String> titleFilters){
        List<Book> books = null;
        DatabaseQuery<Book> query = DatabaseUtils.createQuery(Book.class);
        if(category != null){
            query.equal("categoryId", category.getId());
        }else{
            query.isNull("categoryId");
        }
        for(String s : titleFilters){
            String filter = "%" + s + "%";
            query.like("title", filter);
        }
        books = query.getResult();
        return books;
    }
    private List<Book> getBooks(List<String> titleFilters){
        DatabaseQuery<Book> query = DatabaseUtils.createQuery(Book.class);
        List<Book> books = null;
        for(String s : titleFilters){
            String filter = "%" + s + "%";
            query.like("title", filter);
        }
        books = query.getResult();
        return books;
    }
    private List<Book> getAllBooks(){
        List<Book> books = DatabaseUtils.createQuery(Book.class).getResult();
        return books;
    }
    private Map<String, Object> getCategoryBooksJSON(String cname, List<Book> books){
        Map<String, Object> map = new HashMap<>();
        map.put("category", cname);
        List<Object> bookList = new ArrayList<>();
        for(Book b : books){
            bookList.add(getBookJSON(b));
        }
        map.put("books", bookList);
        return map;
    }
    private Map<String, Object> getCategoryBooksJSON(BookCategory category, List<String> titleFilters){
        List<Book> books = getBooks(category, titleFilters);
        if(category == null && books.size() == 0){
            // an empty "null/unclassified" category is meaningless
            return null;
        }
        String cname = "其他读物";
        if(category != null){
            cname = category.getName();
        }
        return getCategoryBooksJSON(cname, books);
    }
    private Map<String, Object> getCategoryBooksJSON(BookCategory category){
        // empty filters
        List<String> tmp = new ArrayList<>();
        return getCategoryBooksJSON(category, tmp);
    }
    private Map<String, Object> getBooksJSON(List<String> titleFilters){
        List<Book> books = getBooks(titleFilters);
        String cname = "全部分类";
        return getCategoryBooksJSON(cname, books);
    }
    private Map<String, Object> getAllBooksJSON(){
        List<Book> books = getAllBooks();
        String cname = "全部分类";
        return getCategoryBooksJSON(cname, books);
    }
    public List<Object> getBookListJSON(List<String> titleFilters){
        List<Object> list = new ArrayList<>();
        // "all books" category
        list.add(getBooksJSON(titleFilters));
        // category list
        List<BookCategory> categories = getBookCategories();
        // "unclassified" category
        categories.add(null);
        // add JSONs to the result list
        for(BookCategory c : categories){
            Map<String, Object> books = getCategoryBooksJSON(c, titleFilters);
            if(books != null){
                list.add(books);
            }
        }
        return list;
    }
    public List<Object> getBookListJSON(){
        List<String> tmp = new ArrayList<>();
        return getBookListJSON(tmp);
    }
    public Book getBookByISBN(String isbn){
        List<Book> list = DatabaseUtils.createQuery(Book.class)
                .equal("isbn", isbn)
                .getResult();
        if(list == null || list.size() <= 0){
            return null;
        }
        return list.get(0);
    }
    public Map<String, Object> getBookJSONByISBN(String isbn){
        Book book = getBookByISBN(isbn);
        if(book == null){
            return null;
        }
        Map<String, Object> map = getBookJSON(book);
        return map;
    }
    public boolean updateBookInfo(String isbn, String title, String author, BigDecimal price){
        Book book = getBookByISBN(isbn);
        if(book == null){
            return false;
        }
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        return DatabaseUtils.update(book);
    }
    public boolean updateBookStock(String isbn, int stock){
        Book book = getBookByISBN(isbn);
        if(book == null){
            return false;
        }
        book.setStock(stock);
        return DatabaseUtils.update(book);
    }
    public boolean insertBookInfo(String isbn, String title, String author, BigDecimal price){
        Book book = getBookByISBN(isbn);
        if(book != null){
            return false;
        }
        book = new Book();
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        return DatabaseUtils.insert(book);
    }
    public boolean deleteBook(String isbn){
        Book book = getBookByISBN(isbn);
        if(book == null){
            return false;
        }
        return DatabaseUtils.delete(book);
    }

    public Map<String, Object> getBookJSON(Book book){
        Map<String, Object> json = new HashMap<>();
        json.put("title", book.getTitle());
        json.put("author", book.getAuthor());
        json.put("isbn", book.getIsbn());
        json.put("stock", book.getStock());
        json.put("price", book.getPrice().toPlainString());
        json.put("hasImage", book.isHasImage());
        // temporary assembly
        List<BookImage> list = mongo.find(Query.query(Criteria.where("isbn").is(book.getIsbn())), BookImage.class);
        if(list.isEmpty()){
            json.put("imageBase", "");
        }else{
            BookImage image = list.get(0);
            if(image == null){
                json.put("imageBase", "");
            }else{
                json.put("imageBase", image.getImage());
            }
        }
        return json;
    }

    public boolean setBookImage(String isbn, String image){
        List<BookImage> list = mongo.find(Query.query(Criteria.where("isbn").is(isbn)), BookImage.class);
        if(list.isEmpty()){
            BookImage img = new BookImage(isbn, image);
            mongo.insert(img);
            Book book = getBookByISBN(isbn);
            book.setHasImage(true);
            DatabaseUtils.update(book);
        }else{
            BookImage img = list.get(0);
            mongo.remove(img);
            img.setImage(image);
            mongo.insert(img);
        }
        return true;
    }
}
