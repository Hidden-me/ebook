package org.ebook.controller;

import org.ebook.entity.BookManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
}
