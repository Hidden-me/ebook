package org.ebook.util;

import java.util.HashMap;
import java.util.Map;

public class JSONResponse {
    private Map<String, Object> map;
    private String result = "";
    private String message = "";
    private void update(){
        put("result", result);
        put("message", message);
    }
    public JSONResponse(){
        map = new HashMap<>();
    }
    public Map<String, Object> getJSON(){
        update();
        return map;
    }
    public Object get(String key){
        return map.get(key);
    }
    public void put(String key, Object value){
        map.put(key, value);
    }
    public void setMessage(String msg){
        this.message = msg;
    }
    public void setResult(String result){
        this.result = result;
    }
    public void fail(){
        setResult("failure");
    }
    public void succeed(){
        setResult("success");
    }
    public boolean isSuccessful(){
        return result.equals("success");
    }
}
