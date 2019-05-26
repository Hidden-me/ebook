package org.ebook.dao;

import org.ebook.entity.Book;
import org.ebook.entity.BookCategory;
import org.ebook.util.DatabaseQuery;
import org.ebook.util.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BookManager {
    private static Logger logger = LoggerFactory.getLogger(BookManager.class);
    private static List<BookCategory> getBookCategories(){
        List<BookCategory> cs =
                DatabaseUtils.createQuery(BookCategory.class).getResult();
        return cs;
    }
    private static List<Book> getBooks(BookCategory category){
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
    private static List<Book> getBooks(BookCategory category, List<String> titleFilters){
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
    private static List<Book> getBooks(List<String> titleFilters){
        DatabaseQuery<Book> query = DatabaseUtils.createQuery(Book.class);
        List<Book> books = null;
        for(String s : titleFilters){
            String filter = "%" + s + "%";
            query.like("title", filter);
        }
        books = query.getResult();
        return books;
    }
    private static List<Book> getAllBooks(){
        List<Book> books = DatabaseUtils.createQuery(Book.class).getResult();
        return books;
    }
    private static Map<String, Object> getCategoryBooksJSON(String cname, List<Book> books){
        Map<String, Object> map = new HashMap<>();
        map.put("category", cname);
        List<Object> bookList = new ArrayList<>();
        for(Book b : books){
            bookList.add(b.getJSON());
        }
        map.put("books", bookList);
        return map;
    }
    private static Map<String, Object> getCategoryBooksJSON(BookCategory category, List<String> titleFilters){
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
    private static Map<String, Object> getCategoryBooksJSON(BookCategory category){
        // empty filters
        List<String> tmp = new ArrayList<>();
        return getCategoryBooksJSON(category, tmp);
    }
    private static Map<String, Object> getBooksJSON(List<String> titleFilters){
        List<Book> books = getBooks(titleFilters);
        String cname = "全部分类";
        return getCategoryBooksJSON(cname, books);
    }
    private static Map<String, Object> getAllBooksJSON(){
        List<Book> books = getAllBooks();
        String cname = "全部分类";
        return getCategoryBooksJSON(cname, books);
    }
    public static List<Object> getBookListJSON(List<String> titleFilters){
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
    public static List<Object> getBookListJSON(){
        List<String> tmp = new ArrayList<>();
        return getBookListJSON(tmp);
    }
    public static Book getBookByISBN(String isbn){
        List<Book> list = DatabaseUtils.createQuery(Book.class)
                .equal("isbn", isbn)
                .getResult();
        if(list == null || list.size() <= 0){
            return null;
        }
        return list.get(0);
    }
}
