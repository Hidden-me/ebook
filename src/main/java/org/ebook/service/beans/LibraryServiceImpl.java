package org.ebook.service.beans;

import org.ebook.dao.BookCommentManager;
import org.ebook.dao.BookManager;
import org.ebook.service.LibraryService;
import org.ebook.util.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class LibraryServiceImpl implements LibraryService {
    private BookCommentManager bookCommentManager;
    private BookManager bookManager;

    @Autowired
    public LibraryServiceImpl(BookCommentManager bookCommentManager,
                              BookManager bookManager){
        this.bookCommentManager = bookCommentManager;
        this.bookManager = bookManager;
    }

    public JSONResponse getBookList(Map<String, Object> filters){
        JSONResponse resp = new JSONResponse();
        String result;
        if(filters != null){
            List<String> titleFilters = (List<String>) filters.get("title");
            resp.put("library", bookManager.getBookListJSON(titleFilters));
        }else{
            resp.put("library", bookManager.getBookListJSON());
        }
        resp.succeed();
        return resp;
    }
    public JSONResponse getBookDetails(String isbn){
        JSONResponse resp = new JSONResponse();
        resp.put("comments", bookCommentManager.getBookCommentListJSON(isbn));
        resp.succeed();
        return resp;
    }
    public JSONResponse addBookComment(String isbn, String username, String comment){
        JSONResponse resp = new JSONResponse();
        try{
            bookCommentManager.addComment(username, isbn, comment);
            resp.succeed();
        }catch(IllegalArgumentException e){
            resp.setMessage(e.getMessage());
            resp.fail();
        }
        return resp;
    }
    public JSONResponse modifyBookInfo(String isbn, String title, String author, BigDecimal price){
        JSONResponse resp = new JSONResponse();
        boolean succ = bookManager.updateBookInfo(isbn, title, author, price);
        if(succ){
            resp.succeed();
        }else{
            resp.fail();
        }
        return resp;
    }
    public JSONResponse getBookInfo(String isbn){
        JSONResponse resp = new JSONResponse();
        Map<String, Object> json = bookManager.getBookJSONByISBN(isbn);
        if(json == null){
            resp.fail();
        }else{
            resp.put("book", json);
            resp.succeed();
        }
        return resp;
    }
    public JSONResponse setBookStock(String isbn, int stock){
        JSONResponse resp = new JSONResponse();
        boolean succ = bookManager.updateBookStock(isbn, stock);
        if(succ){
            resp.succeed();
        }else{
            resp.fail();
        }
        return resp;
    }
    public JSONResponse addNewBook(String isbn, String title, String author, BigDecimal price){
        JSONResponse resp = new JSONResponse();
        boolean succ = bookManager.insertBookInfo(isbn, title, author, price);
        if(succ){
            resp.succeed();
        }else{
            resp.setMessage("添加书籍失败");
            resp.fail();
        }
        return resp;
    }
    public JSONResponse removeBook(String isbn){
        JSONResponse resp = new JSONResponse();
        boolean succ = bookManager.deleteBook(isbn);
        if(succ){
            resp.succeed();
        }else{
            resp.setMessage("删除书籍失败");
            resp.fail();
        }
        return resp;
    }
    public JSONResponse setBookImage(String isbn, String image){
        JSONResponse resp = new JSONResponse();
        boolean succ = bookManager.setBookImage(isbn, image);
        if(succ){
            resp.succeed();
        }else{
            resp.setMessage("设置图书封面失败");
            resp.fail();
        }
        return resp;
    }
}
