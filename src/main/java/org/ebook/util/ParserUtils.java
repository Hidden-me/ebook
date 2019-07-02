package org.ebook.util;

import java.text.ParseException;

public class ParserUtils {
    public static int parseInt(Object obj) throws ParseException {
        if(obj instanceof String){
            if(((String) obj).isEmpty()){
                // parse empty string to 0
                return 0;
            }
            return Integer.parseInt((String) obj);
        }else if(obj instanceof Integer){
            return (Integer) obj;
        }else{
            throw new ParseException("Cannot parse non-string/integer variables to int", 0);
        }
    }
    public static String parseString(Object obj) {
        if(obj instanceof Integer){
            return String.valueOf((Integer) obj);
        }else if(obj instanceof Double){
            return String.valueOf((Double) obj);
        }else if(obj instanceof Float){
            return String.valueOf((Float) obj);
        }else if(obj instanceof String){
            return (String) obj;
        }else{
            return null;
        }
    }
}
