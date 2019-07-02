package org.ebook.service;

import org.ebook.util.JSONResponse;

import java.math.BigDecimal;
import java.util.Map;

public interface LibraryService {
    JSONResponse getBookList(Map<String, Object> filters);
    JSONResponse getBookDetails(String isbn);
    JSONResponse addBookComment(String isbn, String username, String comment);
    JSONResponse modifyBookInfo(String isbn, String title, String author, BigDecimal price);
    JSONResponse getBookInfo(String isbn);
    JSONResponse setBookStock(String isbn, int stock);
    JSONResponse addNewBook(String isbn, String title, String author, BigDecimal price);
    JSONResponse removeBook(String isbn);
    JSONResponse setBookImage(String isbn, String image);
}
