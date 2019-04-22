package org.ebook.entity;

import org.ebook.DatabaseAdapter;
import org.hibernate.*;

import javax.persistence.criteria.*;
import java.util.LinkedList;

public class UserManager {
    public static User getUser(String username){
        LinkedList<User> users = null;
        Session session = DatabaseAdapter.getSession();
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
}
