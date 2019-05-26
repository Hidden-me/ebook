package org.ebook.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseQuery<T> {

    private Session session;
    private CriteriaBuilder cb;
    private CriteriaQuery<T> cr;
    private Root from;
    private List<Predicate> pred;

    private Predicate[] getPredicateArray(){
        Predicate[] result = new Predicate[pred.size()];
        int i = 0;
        for(Predicate p : pred){
            result[i++] = p;
        }
        return result;
    }

    public DatabaseQuery(Class<T> aClass){
        session = DatabaseUtils.getSession();
        cb = session.getCriteriaBuilder();
        cr = cb.createQuery(aClass);
        from = cr.from(aClass);
        pred = new LinkedList<>();
    }

    public DatabaseQuery<T> equal(String attr, Object obj){
        pred.add(cb.equal(from.get(attr), obj));
        return this;
    }

    public DatabaseQuery<T> between(String attr, Comparable start, Comparable end){
        pred.add(cb.between(from.get(attr), start, end));
        return this;
    }

    public DatabaseQuery<T> ge(String attr, Comparable obj){
        pred.add(cb.greaterThanOrEqualTo(from.get(attr), obj));
        return this;
    }

    public DatabaseQuery<T> le(String attr, Comparable obj){
        pred.add(cb.lessThanOrEqualTo(from.get(attr), obj));
        return this;
    }

    public DatabaseQuery<T> like(String attr, String str){
        pred.add(cb.like(from.get(attr), str));
        return this;
    }

    public DatabaseQuery<T> isNull(String attr){
        pred.add(cb.isNull(from.get(attr)));
        return this;
    }

    public List<T> getResult(){
        List<T> result = null;
        try {
            cr.where(getPredicateArray());
            result = new LinkedList<>(session.createQuery(cr).getResultList());
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }
}
