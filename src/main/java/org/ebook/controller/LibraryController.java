package org.ebook.controller;

import org.ebook.service.LibraryService;
import org.ebook.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("/library")
public class LibraryController {
    private LibraryService library;
    private SessionService session;

    @Autowired
    public LibraryController(LibraryService library, SessionService session){
        this.library = library;
        this.session = session;
    }

    @GetMapping
    public ModelAndView getLibraryView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping
    public Map<String, Object> getBookList(@RequestBody Map<String, Object> req){
        Map<String, Object> filters = (Map<String, Object>) req.get("filters");
        return library.getBookList(filters).getJSON();
    }
    @PostMapping("details")
    public Map<String, Object> getBookDetails(@RequestBody Map<String, Object> req){
        String isbn = (String) req.get("isbn");
        return library.getBookDetails(isbn).getJSON();
    }
    @PostMapping("details/comments/add")
    public Map<String, Object> addBookComment(@RequestBody Map<String, Object> req){
        String isbn = (String) req.get("isbn");
        String username = session.getUsername();
        String comment = (String) req.get("comment");
        return library.addBookComment(isbn, username, comment).getJSON();
    }
}
