package org.ebook.controller;

import org.ebook.entity.BookCommentManager;
import org.ebook.entity.BookManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/library")
public class LibraryController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @GetMapping
    public ModelAndView getLibraryView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping
    public String getBookList(@RequestBody Map<String, Object> req){
        Map<String, Object> filters = (Map<String, Object>) req.get("filters");
        String result;
        if(filters != null){
            ArrayList<String> titleFilters = (ArrayList<String>) filters.get("title");
            result = BookManager.getBookListJSONString(titleFilters);
        }else{
            result = BookManager.getBookListJSONString();
        }
        return result;
    }
    @PostMapping("details")
    public String getBookDetails(@RequestBody Map<String, Object> req){
        String isbn = (String) req.get("isbn");
        String result = BookManager.getBookDetailsString(isbn);
        logger.info(result);
        return result;
    }
    @PostMapping("details/comments/add")
    public String addBookComment(@RequestBody Map<String, Object> req){
        String message = "";
        String comment = (String) req.get("comment");
        String isbn = (String) req.get("isbn");
        String username = null;
        HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
        if(ss != null){
            username = (String) ss.getAttribute("username");
        }
        boolean succ = false;
        try{
            BookCommentManager.addComment(username, isbn, comment);
            succ = true;
        }catch(IllegalArgumentException e){
            message = e.getMessage();
            logger.info(message);
        }
        String result = "{\"result\":\"" + (succ ? "success" : "failure") + "\"," +
                "\"message\":\"" + message + "\"}";
        return result;
    }
}
