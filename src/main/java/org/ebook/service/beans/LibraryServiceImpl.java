package org.ebook.service.beans;

import org.ebook.dao.BookCommentManager;
import org.ebook.dao.BookManager;
import org.ebook.service.LibraryService;
import org.ebook.util.JSONResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LibraryServiceImpl implements LibraryService {
    public JSONResponse getBookList(Map<String, Object> filters){
        JSONResponse resp = new JSONResponse();
        String result;
        if(filters != null){
            List<String> titleFilters = (List<String>) filters.get("title");
            resp.put("library", BookManager.getBookListJSON(titleFilters));
        }else{
            resp.put("library", BookManager.getBookListJSON());
        }
        resp.succeed();
        return resp;
    }
    public JSONResponse getBookDetails(String isbn){
        JSONResponse resp = new JSONResponse();
        resp.put("comments", BookCommentManager.getBookCommentListJSON(isbn));
        resp.succeed();
        return resp;
    }
    public JSONResponse addBookComment(String isbn, String username, String comment){
        JSONResponse resp = new JSONResponse();
        try{
            BookCommentManager.addComment(username, isbn, comment);
            resp.succeed();
        }catch(IllegalArgumentException e){
            resp.setMessage(e.getMessage());
            resp.fail();
        }
        return resp;
    }
}
