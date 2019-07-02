package org.ebook.dao;

import org.ebook.entity.Book;
import org.ebook.entity.CartItem;

import java.util.Map;

public interface CartManager {
    Map<String, Object> getCartItemJSON(CartItem item);
    Map<String, Object> getCartJSON(String username);
    void addToCart(String username, Book book);
    void removeFromCart(String username, String isbn);
    void clearCart(String username);
    boolean setBookCount(String username, String isbn, int count);
    boolean increaseBookCount(String username, String isbn);
    boolean decreaseBookCount(String username, String isbn);
    boolean generateOrder(String username);
}
