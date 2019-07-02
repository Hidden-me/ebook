package org.ebook.security.beans;

import org.ebook.dao.UserManager;
import org.ebook.entity.User;
import org.ebook.security.UserValidator;
import org.ebook.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserValidatorImpl implements UserValidator {
    private static int pubkeyLength = 8;
    // <username, pubkey>
    private static Map<String, String> pubkeys = new HashMap<>();

    private UserManager userManager;

    @Autowired
    public UserValidatorImpl(UserManager userManager){
        this.userManager = userManager;
    }

    private User getUser(String username){
        return userManager.getUserByName(username);
    }
    public boolean validate(String username, String md5Token){
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
    public void removePubkey(String username){
        pubkeys.remove(username);
    }
    public String getRandomPubkey(String username){
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
