package org.ebook.interceptor;

import org.ebook.security.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.*;

import javax.servlet.http.*;
import java.io.PrintWriter;

public class AuthInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object obj)
            throws Exception {
        HttpSession ss = req.getSession(false);
        String loginPath = req.getContextPath() + "/login";
        if (ss == null) {
            // session does not exist
            // redirect to login page
            resp.sendRedirect(loginPath);
            logger.debug("session does not exist, redirected");
            return false;
        }
        String token = (String) ss.getAttribute("token");
        String username = (String) ss.getAttribute("username");
        if (token == null || username == null) {
            // invalid header
            // redirect to login page
            resp.sendRedirect(loginPath);
            logger.debug("invalid header, redirected");
            return false;
        } else {
            boolean succ = UserValidator.validate(username, token);
            if(!succ){
                // authentication failed
                // redirect to login page
                resp.sendRedirect(loginPath);
                logger.debug("validation failed, redirected");
            }
            logger.debug("authorization succeeded");
            return succ;
        }
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
