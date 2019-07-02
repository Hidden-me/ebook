package org.ebook.controller;

import org.ebook.service.LibraryService;
import org.ebook.service.SessionService;
import org.ebook.util.JSONResponse;
import org.ebook.util.ParserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
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
    @PostMapping("refresh_book")
    public Map<String, Object> refreshBook(@RequestBody Map<String, Object> req){
        String isbn = (String) req.get("isbn");
        return library.getBookInfo(isbn).getJSON();
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
    @PostMapping("details/modify")
    public Map<String, Object> modifyBookInfo(@RequestBody Map<String, Object> req){
        String isbn = (String) req.get("isbn");
        String title = (String) req.get("title");
        String author = (String) req.get("author");
        BigDecimal price = new BigDecimal(ParserUtils.parseString(req.get("price")));
        session.setLastReferencedBookIsbn(isbn);
        return library.modifyBookInfo(isbn, title, author, price).getJSON();
    }
    @PostMapping("set_stock")
    public Map<String, Object> setBookStock(@RequestBody Map<String, Object> req){
        String isbn = (String) req.get("isbn");
        Integer stock = (Integer) req.get("stock");
        if(stock == null){
            JSONResponse resp = new JSONResponse();
            resp.setMessage("输入库存有误，请重新设置库存");
            resp.fail();
            return resp.getJSON();
        }
        return library.setBookStock(isbn, stock).getJSON();
    }
    @PostMapping("add")
    public Map<String, Object> addNewBook(@RequestBody Map<String, Object> req){
        String isbn = (String) req.get("isbn");
        String title = (String) req.get("title");
        String author = (String) req.get("author");
        BigDecimal price = new BigDecimal(ParserUtils.parseString(req.get("price")));
        session.setLastReferencedBookIsbn(isbn);
        return library.addNewBook(isbn, title, author, price).getJSON();
    }
    @PostMapping("delete")
    public Map<String, Object> removeBook(@RequestBody Map<String, Object> req){
        String isbn = (String) req.get("isbn");
        return library.removeBook(isbn).getJSON();
    }
    @PostMapping("set_image")
    public Map<String, Object> setImage(@RequestBody Map<String, Object> req){
        String isbn = session.getLastReferencedBookIsbn();
        String image = (String) req.get("image");
        return library.setBookImage(isbn, image).getJSON();
    }
}
