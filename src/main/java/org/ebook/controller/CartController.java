package org.ebook.controller;

import org.ebook.service.CartService;
import org.ebook.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    private CartService cart;
    private SessionService session;

    @Autowired
    public CartController(CartService cart, SessionService session){
        this.cart = cart;
        this.session = session;
    }

    @GetMapping
    public ModelAndView getCartView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping
    public Map<String, Object> getCartContent(){
        String username = session.getUsername();
        return cart.getCartContent(username).getJSON();
    }
    @PostMapping("add")
    public Map<String, Object> addToCart(@RequestBody Map<String, Object> req){
        String isbn = (String) req.get("isbn");
        String username = session.getUsername();
        return cart.addToCart(username, isbn).getJSON();
    }
    @PostMapping("remove")
    public Map<String, Object> removeFromCart(@RequestBody Map<String, Object> req){
        String isbn = (String) req.get("isbn");
        String username = session.getUsername();
        return cart.removeFromCart(username, isbn).getJSON();
    }
    @PostMapping("set")
    public Map<String, Object> setCartItemCount(@RequestBody Map<String, Object> req){
        String isbn = (String) req.get("isbn");
        int count = (Integer) req.get("count");
        String username = session.getUsername();
        return cart.setCartItemCount(username, isbn, count).getJSON();
    }
    @PostMapping("submit")
    public Map<String, Object> submitOrder(){
        String username = session.getUsername();
        return cart.submitOrder(username).getJSON();
    }
}
