package org.ebook.security;

import org.ebook.DatabaseAdapter;
import org.ebook.entity.User;
import org.hibernate.*;

import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.security.*;
import java.util.*;

public class UserValidator {
    private static int pubkeyLength = 4;
    // <username, pubkey>
    private static Map<String, String> pubkeys = new HashMap<String, String>();

    private static String md5(String origin){
        String result = null;
        byte[] secretBytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(origin.getBytes());
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return result;
        }
        result = new BigInteger(1, secretBytes).toString(16);
        int spaces = 32 - result.length();
        for (int i = 0; i < spaces; i++) {
            result = "0" + result;
        }
        return result;
    }
    private static User getUser(String username){
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
    public static boolean validate(String username, String md5Token){
        User user = getUser(username);
        if(user != null){
            // username matches
            String pubkey = pubkeys.get(username);
            if(pubkey == null){
                // invalid validation: validate before requesting for public key
                return false;
            }
            // reset the user's public key
            pubkeys.remove(username);
            // md5 encryption & validation
            String passToken = user.getPassToken();
            String expected = md5(pubkey + passToken);
            String actual = md5Token;
            return expected.equalsIgnoreCase(actual);
        }else{
            return false;
        }
    }
    public static String getRandomPubkey(String username){
        Random r = new Random();
        char[] ch = new char[pubkeyLength];
        for(int i = 0; i < pubkeyLength; i++){
            ch[i] = (char) (r.nextInt(26) - 1 + 'a');
        }
        String result = String.valueOf(ch);
        pubkeys.put(username, result);
        return result;
    }
}
