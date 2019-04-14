package org.ebook.controller;

import org.ebook.entity.BookListGetter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/library")
public class LibraryController {
    @GetMapping
    public ModelAndView getLibraryView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping
    public String getBookList(){
        String result = BookListGetter.getBookListJSONString();
        return result;
    }
}
