package org.ebook.service.beans;

import org.ebook.service.SessionService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Service
public class SessionServiceImpl implements SessionService {
    private Object getAttribute(String name){
        HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
        if(ss == null){
            // offline, should login first
            return null;
        }
        return ss.getAttribute(name);
    }

    private void setAttribute(String name, Object value){
        HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        ss.setAttribute(name, value);
    }

    public String getUsername(){
        return (String) getAttribute("username");
    }

    public String getIdentity(){
        return (String) getAttribute("identity");
    }

    public String getToken(){
        return (String) getAttribute("token");
    }

    public void setUsername(String username){
        setAttribute("username", username);
    }

    public void setIdentity(String identity){
        setAttribute("identity", identity);
    }

    public void setToken(String token){
        setAttribute("token", token);
    }

}
