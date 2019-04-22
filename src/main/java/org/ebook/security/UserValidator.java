package org.ebook.security;

import org.ebook.DatabaseAdapter;
import org.ebook.entity.User;
import org.ebook.entity.UserManager;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.security.*;
import java.util.*;

public class UserValidator {
    private static Logger logger = LoggerFactory.getLogger(UserValidator.class);
    private static int pubkeyLength = 8;
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
        return UserManager.getUser(username);
    }
    public static boolean validate(String username, String md5Token){
        User user = getUser(username);
        if(user != null){
            // username matches
            String pubkey = pubkeys.get(username);
            logger.info("pubkey: " + pubkey);
            if(pubkey == null){
                // invalid validation: validate before requesting for public key
                return false;
            }
            // md5 encryption & validation
            String passToken = user.getPassToken();
            String expected = md5(pubkey + passToken);
            String actual = md5Token;
            return expected.equalsIgnoreCase(actual);
        }else{
            logger.info("user " + username + " does not exist");
            return false;
        }
    }
    public static void removePubkey(String username){
        pubkeys.remove(username);
    }
    public static String getRandomPubkey(String username){
        // reset the user's public key
        removePubkey(username);
        // generate a random public key
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
