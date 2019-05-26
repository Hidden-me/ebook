package org.ebook.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {
    public static String md5(String origin){
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
}
