package org.ebook.service;

import org.ebook.util.JSONResponse;

import java.util.Map;

public interface LibraryService {
    JSONResponse getBookList(Map<String, Object> filters);
    JSONResponse getBookDetails(String isbn);
    JSONResponse addBookComment(String isbn, String username, String comment);
}
