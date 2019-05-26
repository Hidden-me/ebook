package org.ebook.service;

import org.ebook.util.JSONResponse;

public interface CartService {
    JSONResponse getCartContent(String username);
    JSONResponse addToCart(String username, String isbn);
    JSONResponse removeFromCart(String username, String isbn);
    JSONResponse setCartItemCount(String username, String isbn, int count);
    JSONResponse submitOrder(String username);
}
