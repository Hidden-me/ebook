package org.ebook.util;

import org.ebook.entity.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class DatabaseUtils {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            configuration.addAnnotatedClass(Book.class);
            configuration.addAnnotatedClass(BookComment.class);
            configuration.addAnnotatedClass(BookCategory.class);
            configuration.addAnnotatedClass(Order.class);
            configuration.addAnnotatedClass(OrderItem.class);
            configuration.addAnnotatedClass(User.class);
            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static boolean save(Object obj){
        boolean succ = false;
        Session ss = getSession();
        Transaction ts = null;
        try {
            ts = ss.beginTransaction();
            ss.save(obj);
            ts.commit();
            succ = true;
        } catch (HibernateException e) {
            if (ts != null) {
                ts.rollback();
            }
            e.printStackTrace();
        } finally {
            ss.close();
        }
        return succ;
    }

    public static <X> DatabaseQuery<X> createQuery(Class<X> aClass){
        return new DatabaseQuery<>(aClass);
    }


}