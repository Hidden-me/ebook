package org.ebook.entity;

import org.ebook.DatabaseAdapter;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class EntityManager {
    public static int insert(Object obj){
        Session session = DatabaseAdapter.getSession();
        Transaction ts = null;
        int id = -1;
        try {
            ts = session.beginTransaction();
            id = (int) session.save(obj);
            ts.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if(ts != null){
                ts.rollback();
            }
        } finally {
            session.close();
        }
        return id;
    }
}
