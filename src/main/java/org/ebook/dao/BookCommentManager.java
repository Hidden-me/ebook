package org.ebook.dao;

import org.ebook.entity.Book;
import org.ebook.entity.BookComment;
import org.ebook.entity.User;
import org.ebook.util.DatabaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookCommentManager {
    private static Map<String, Object> getBookCommentJSON(BookComment comment){
        if(comment == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        Integer uid = comment.getUid();
        String username = "未知用户";
        if(uid != null){
            User user = UserManager.getUserByUid(comment.getUid());
            if(user != null){
                username = user.getUsername();
            }
        }
        map.put("uid", uid);
        map.put("username", username);
        map.put("date", comment.getTimeCreate());
        map.put("comment", comment.getComment());
        return map;
    }
    public static List<Object> getBookCommentListJSON(String isbn){
        if(isbn == null){
            return getEmptyCommentListJSON();
        }
        List<Object> list = new ArrayList<>();
        List<BookComment> comments = DatabaseUtils.createQuery(BookComment.class)
                .equal("isbn", isbn)
                .getResult();
        for(BookComment c : comments){
            Map<String, Object> cmt = getBookCommentJSON(c);
            if(cmt != null){
                list.add(cmt);
            }
        }
        return list;
    }
    public static List<Object> getEmptyCommentListJSON(){
        return new ArrayList<Object>();
    }
    public static void addComment(String username, String isbn, String comment)
            throws IllegalArgumentException{
        // exceptions
        boolean succ = false;
        if(username == null){
            throw new IllegalArgumentException("请先登录");
        }
        if(isbn == null){
            throw new IllegalArgumentException("图书不存在");
        }
        if(comment == null){
            throw new IllegalArgumentException("评论不得为空");
        }
        User user = UserManager.getUserByName(username);
        if(user == null){
            throw new IllegalArgumentException("请先登录");
        }
        Book book = BookManager.getBookByISBN(isbn);
        if(book == null){
            throw new IllegalArgumentException("图书不存在");
        }
        if(comment.length() <= 6 || comment.length() > 500){
            throw new IllegalArgumentException("评论应多于6字，至多500字！");
        }
        // save the comment to DB
        BookComment c = new BookComment();
        c.setComment(comment);
        c.setIsbn(isbn);
        c.setUid(UserManager.getUid(username));
        DatabaseUtils.save(c);
    }
}
