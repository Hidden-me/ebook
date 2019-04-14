package org.ebook.entity;

import org.ebook.DatabaseAdapter;
import org.hibernate.*;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

public class BookListGetter {
    private static LinkedList<BookCategory> getBookCategories(){
        LinkedList<BookCategory> cs = new LinkedList<BookCategory>();
        Session session = DatabaseAdapter.getSession();
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
        Session session = DatabaseAdapter.getSession();
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
    public static String getBookListJSONString(){
        String result = "{\"library\":[";
        LinkedList<BookCategory> categories = getBookCategories();
        categories.add(null);
        boolean first = true;
        for(BookCategory c : categories){
            String books = getBooksString(c);
            if(books != null){
                if(first){
                    first = false;
                }else{
                    result += ",";
                }
                result += books;
            }
        }
        result += "]}";
        return result;
    }
}
