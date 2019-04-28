package org.ebook.entity;

import org.ebook.util.DatabaseUtils;

import java.util.List;

public class BookCommentManager {
    private static String getBookCommentString(BookComment comment){
        if(comment == null){
            return null;
        }
        Integer uid = comment.getUid();
        String username = "未知用户";
        if(uid != null){
            User user = UserManager.getUserByUid(comment.getUid());
            if(user != null){
                username = user.getUsername();
            }
        }
        String result = "{\"uid\":\"" + uid + "\"," +
                "\"username\":\"" + username + "\"," +
                "\"date\":\"" + comment.getTimeCreate() + "\"," +
                "\"comment\":\"" + comment.getComment() + "\"}";
        return result;
    }
    public static String getBookCommentsString(String isbn){
        if(isbn == null){
            return getEmptyCommentsString();
        }
        String result = "[";
        List<BookComment> comments = DatabaseUtils.createQuery(BookComment.class)
                .equal("isbn", isbn)
                .getResult();
        boolean first = true;
        for(BookComment c : comments){
            String str = getBookCommentString(c);
            if(str != null){
                if(first){
                    first = false;
                }else{
                    result += ",";
                }
                result += str;
            }
        }
        result += "]";
        return result;
    }
    public static String getEmptyCommentsString(){
        return "[]";
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
