package org.ebook.entity;

import org.ebook.util.DatabaseQuery;
import org.ebook.util.DatabaseUtils;
import org.hibernate.HibernateException;
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
    private static String getBooksString(String cname, List<Book> books){
        String result = "{\"category\":\"" + cname + "\",\"books\":[";
        boolean first = true;
        for(Book b : books){
            if(first){
                first = false;
            }else{
                result += ",";
            }
            result += b.toJSONString();
        }
        result += "]}";
        return result;
    }
    private static String getBooksString(BookCategory category, List<String> titleFilters){
        List<Book> books = getBooks(category, titleFilters);
        if(category == null && books.size() == 0){
            // an empty "null/unclassified" category is meaningless
            return null;
        }
        String cname = "其他读物";
        if(category != null){
            cname = category.getName();
        }
        String result = getBooksString(cname, books);
        return result;
    }
    private static String getBooksString(BookCategory category){
        List<String> tmp = new ArrayList<>();
        return getBooksString(category, tmp);
    }
    private static String getBooksString(List<String> titleFilters){
        List<Book> books = getBooks(titleFilters);
        String cname = "全部分类";
        String result = getBooksString(cname, books);
        return result;
    }
    private static String getAllBooksString(){
        List<Book> books = getAllBooks();
        String cname = "全部分类";
        String result = getBooksString(cname, books);
        return result;
    }
    public static String getBookListJSONString(List<String> titleFilters){
        for(String s : titleFilters){
            logger.info("title:" + s);
        }
        String result = "{\"library\":[";
        result += getBooksString(titleFilters);
        List<BookCategory> categories = getBookCategories();
        categories.add(null);
        for(BookCategory c : categories){
            String books = getBooksString(c, titleFilters);
            if(books != null){
                result += ",";
                result += books;
            }
        }
        result += "]}";
        return result;
    }
    public static String getBookListJSONString(){
        List<String> tmp = new ArrayList<>();
        return getBookListJSONString(tmp);
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
