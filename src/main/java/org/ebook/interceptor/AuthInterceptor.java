package org.ebook.interceptor;

import org.ebook.security.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;

public class AuthInterceptor implements HandlerInterceptor {
    private UserValidator userValidator;

    @Autowired
    public void setUserValidator(UserValidator userValidator){
        this.userValidator = userValidator;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object obj)
            throws Exception {
        // TODO: prevent a user from signing up twice at different places
        HttpSession ss = req.getSession(false);
        String[] path = req.getServletPath().split("/");
        String loginPath = req.getContextPath() + "/login";
        String accessDeniedPath = req.getContextPath() + "/forbidden/access_denied";
        String duplicateAuthPath = req.getContextPath() + "/forbidden/duplicate_auth";
        if (ss == null) {
            // session does not exist
            if(path[1].equals("login") || path[1].equals("register")){
                // pass login/register requests
                return true;
            }
            // redirect to login page
            resp.sendRedirect(loginPath);
            return false;
        }
        String token = (String) ss.getAttribute("token");
        String username = (String) ss.getAttribute("username");
        String identity = (String) ss.getAttribute("identity");
        if (token == null || username == null || identity == null) {
            // invalid identity
            if(path[1].equals("login") || path[1].equals("register")){
                // pass login/register requests
                return true;
            }
            // redirect to login page
            resp.sendRedirect(loginPath);
            return false;
        } else {
            boolean succ = userValidator.validate(username, token);
            if(!succ){
                // authentication failed
                if(path[1].equals("login") || path[1].equals("register")){
                    // pass login/register requests
                    return true;
                }
                // redirect to login page
                resp.sendRedirect(loginPath);
                return false;
            }else{
                // authentication succeeded
                if(path[1].equals("login") || path[1].equals("register")){
                    // already login
                    // no duplicate login/register requests
                    // redirect to 403 page
                    resp.sendRedirect(duplicateAuthPath);
                    return false;
                }else if(path[1].equals("users") || path[1].equals("cart")
                        || path[1].equals("order")){
                    // those pages needs "user" identity
                    if(!identity.equals("user")){
                        // access denied
                        // redirect to 403 page
                        resp.sendRedirect(accessDeniedPath);
                        return false;
                    }
                    return true;
                }else if(path[1].equals("management") || path[1].equals("admin")){
                    // those pages needs "admin" identity
                    if(!identity.equals("admin")){
                        // access denied
                        // redirect to 403 page
                        resp.sendRedirect(accessDeniedPath);
                        return false;
                    }
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object obj, ModelAndView mav)
            throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object obj, Exception e)
            throws Exception {

    }

}
