package org.ebook.dao;

import org.ebook.entity.Book;

import java.math.BigDecimal;
import java.util.*;

public interface BookManager {
    List<Object> getBookListJSON(List<String> titleFilters);
    List<Object> getBookListJSON();
    Book getBookByISBN(String isbn);
    Map<String, Object> getBookJSONByISBN(String isbn);
    boolean updateBookInfo(String isbn, String title, String author, BigDecimal price);
    boolean updateBookStock(String isbn, int stock);
    boolean insertBookInfo(String isbn, String title, String author, BigDecimal price);
    boolean deleteBook(String isbn);
    Map<String, Object> getBookJSON(Book book);
    boolean setBookImage(String isbn, String image);
}
