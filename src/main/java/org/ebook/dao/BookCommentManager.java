package org.ebook.dao;

import java.util.List;

public interface BookCommentManager {
    List<Object> getBookCommentListJSON(String isbn);
    List<Object> getEmptyCommentListJSON();
    void addComment(String username, String isbn, String comment)
            throws IllegalArgumentException;
}
