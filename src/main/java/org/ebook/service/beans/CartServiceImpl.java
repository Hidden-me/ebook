package org.ebook.service.beans;

import org.ebook.dao.BookManager;
import org.ebook.dao.CartManager;
import org.ebook.entity.Book;
import org.ebook.service.CartService;
import org.ebook.util.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private BookManager bookManager;
    private CartManager cartManager;

    @Autowired
    public CartServiceImpl(BookManager bookManager, CartManager cartManager){
        this.bookManager = bookManager;
        this.cartManager = cartManager;
    }

    public JSONResponse getCartContent(String username){
        JSONResponse resp = new JSONResponse();
        if(username == null){
            resp.fail();
        }else{
            resp.succeed();
            resp.put("cart", cartManager.getCartJSON(username));
        }
        return resp;
    }
    public JSONResponse addToCart(String username, String isbn){
        JSONResponse resp = new JSONResponse();
        Book book = bookManager.getBookByISBN(isbn);
        if(username == null){
            resp.setMessage("请先登录");
            resp.fail();
        }else if(book == null){
            resp.setMessage("该图书不存在");
            resp.fail();
        }else{
            cartManager.addToCart(username, book);
            resp.succeed();
        }
        return resp;
    }
    public JSONResponse removeFromCart(String username, String isbn){
        JSONResponse resp = new JSONResponse();
        if(username == null){
            resp.setMessage("请先登录");
            resp.fail();
        }else{
            cartManager.removeFromCart(username, isbn);
            resp.succeed();
        }
        return resp;
    }
    public JSONResponse setCartItemCount(String username, String isbn, int count){
        JSONResponse resp = new JSONResponse();
        if(username == null){
            resp.setMessage("请先登录");
            resp.fail();
        }else{
            boolean succ = cartManager.setBookCount(username, isbn, count);
            if(!succ){
                resp.setMessage("图书购买数超出库存，或少于1本");
                resp.fail();
            }else{
                resp.succeed();
            }
        }
        return resp;
    }
    public JSONResponse submitOrder(String username){
        JSONResponse resp = new JSONResponse();
        boolean succ = cartManager.generateOrder(username);
        if(succ){
            cartManager.clearCart(username);
            resp.succeed();
        }else{
            resp.fail();
        }
        return resp;
    }
}
