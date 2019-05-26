package org.ebook.security;

import org.ebook.entity.User;
import org.ebook.dao.UserManager;
import org.ebook.util.SecurityUtils;

import java.util.*;

public class UserValidator {
    private static int pubkeyLength = 8;
    // <username, pubkey>
    private static Map<String, String> pubkeys = new HashMap<String, String>();

    private static User getUser(String username){
        return UserManager.getUserByName(username);
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
            // md5 encryption & validation
            String passToken = user.getPassToken();
            String expected = SecurityUtils.md5(pubkey + passToken);
            String actual = md5Token;
            return expected.equalsIgnoreCase(actual);
        }else{
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
