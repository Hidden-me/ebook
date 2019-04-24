package org.ebook.entity;

import org.ebook.DatabaseUtils;
import org.hibernate.*;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

public class BookListGetter {
    private static LinkedList<BookCategory> getBookCategories(){
        LinkedList<BookCategory> cs = new LinkedList<BookCategory>();
        Session session = DatabaseUtils.getSession();
        try {
            CriteriaQuery<BookCategory> cr = session.getCriteriaBuilder()
                    .createQuery(BookCategory.class);
            cr.from(BookCategory.class);
            List categories = session.createQuery(cr).getResultList();
            for(Object obj : categories){
                BookCategory c = (BookCategory) obj;
                cs.add(c);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return cs;
    }
    private static LinkedList<Book> getBooksFromCategory(BookCategory category){
        LinkedList<Book> books = null;
        Session session = DatabaseUtils.getSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> cr = cb.createQuery(Book.class);
            Root from = cr.from(Book.class);
            Predicate[] pred = {null};
            if(category != null){
                pred[0] = cb.equal(from.get("categoryId"), category.getId());
            }else{
                pred[0] = cb.isNull(from.get("categoryId"));
            }
            cr.where(pred);
            books = new LinkedList<Book>(session.createQuery(cr).getResultList());
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return books;
    }
    private static LinkedList<Book> getAllBooks(){
        LinkedList<Book> books = null;
        Session session = DatabaseUtils.getSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Book> cr = cb.createQuery(Book.class);
            cr.from(Book.class);
            books = new LinkedList<Book>(session.createQuery(cr).getResultList());
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return books;
    }
    private static String getBooksString(String cname, LinkedList<Book> books){
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
    private static String getBooksString(BookCategory category){
        LinkedList<Book> books = getBooksFromCategory(category);
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
    private static String getAllBooksString(){
        LinkedList<Book> books = getAllBooks();
        String cname = "全部书籍";
        String result = getBooksString(cname, books);
        return result;
    }
    public static String getBookListJSONString(){
        String result = "{\"library\":[";
        result += getAllBooksString();
        LinkedList<BookCategory> categories = getBookCategories();
        categories.add(null);
        for(BookCategory c : categories){
            String books = getBooksString(c);
            if(books != null){
                result += ",";
                result += books;
            }
        }
        result += "]}";
        return result;
    }
}
