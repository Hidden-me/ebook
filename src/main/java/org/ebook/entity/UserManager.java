package org.ebook.entity;

import org.ebook.DatabaseUtils;
import org.hibernate.*;

import javax.persistence.criteria.*;
import java.util.LinkedList;

public class UserManager {
    public static User getUserByName(String username){
        LinkedList<User> users = null;
        Session session = DatabaseUtils.getSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root from = cr.from(User.class);
            cr.select(from);
            cr.where(cb.equal(from.get("username"), username));
            users = new LinkedList<User>(session.createQuery(cr).getResultList());
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        if(users == null){
            return null;
        }else{
            if(users.isEmpty()){
                return null;
            }
            return users.getFirst();
        }
    }
    public static User getUserByEmail(String email){
        if(email == null){
            return null;
        }
        LinkedList<User> users = null;
        Session session = DatabaseUtils.getSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root from = cr.from(User.class);
            cr.select(from);
            cr.where(cb.equal(from.get("email"), email));
            users = new LinkedList<User>(session.createQuery(cr).getResultList());
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        if(users == null){
            return null;
        }else{
            if(users.isEmpty()){
                return null;
            }
            return users.getFirst();
        }
    }
}
