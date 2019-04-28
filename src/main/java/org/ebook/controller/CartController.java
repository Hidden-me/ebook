package org.ebook.controller;

import org.ebook.entity.Book;
import org.ebook.entity.BookManager;
import org.ebook.entity.CartManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @GetMapping
    public ModelAndView getCartView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping
    public String getCartContent(){
        HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
        if(ss == null){
            // offline, should login first
            return "{\"result\":\"failure\"}";
        }
        String username = (String) ss.getAttribute("username");
        String result = "{\"result\":\"success\",\"cart\":";
        result += CartManager.getCartJSONString(username);
        result += "}";
        return result;
    }
    @PostMapping("add")
    public String addToCart(@RequestBody Map<String, Object> req){
        boolean succ = false;
        String isbn = (String) req.get("isbn");
        String username = null;
        HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
        if(ss != null){
            username = (String) ss.getAttribute("username");
        }
        Book book = BookManager.getBookByISBN(isbn);
        String message = "";
        if(username == null){
            message = "请先登录";
        }else if(book == null){
            message = "该图书不存在";
        }else{
            CartManager.addToCart(username, book);
            succ = true;
        }
        String result = "{\"result\":\"" + (succ ? "success" : "failure") + "\"";
        result += ",\"message\":\"" + message + "\"}";
        return result;
    }
    @PostMapping("remove")
    public String removeFromCart(@RequestBody Map<String, Object> req){
        boolean succ = false;
        String isbn = (String) req.get("isbn");
        String username = null;
        HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
        if(ss != null){
            username = (String) ss.getAttribute("username");
        }
        String message = "";
        if(username == null){
            message = "请先登录";
        }else{
            CartManager.removeFromCart(username, isbn);
            succ = true;
        }
        String result = "{\"result\":\"" + (succ ? "success" : "failure") + "\"";
        result += ",\"message\":\"" + message + "\"}";
        return result;
    }
    @PostMapping("set")
    public String setCartItemCount(@RequestBody Map<String, Object> req){
        boolean succ = false;
        String isbn = (String) req.get("isbn");
        int count = (Integer) req.get("count");
        String username = null;
        HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
        if(ss != null){
            username = (String) ss.getAttribute("username");
        }
        String message = "";
        if(username == null){
            message = "请先登录";
        }else{
            succ = CartManager.setBookCount(username, isbn, count);
            if(!succ){
                message = "图书购买数超出库存，或少于1本";
            }
        }
        String result = "{\"result\":\"" + (succ ? "success" : "failure") + "\"";
        result += ",\"message\":\"" + message + "\"}";
        return result;
    }
    @PostMapping("submit")
    public String submitOrder(){
        String username = null;
        HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
        if(ss != null){
            username = (String) ss.getAttribute("username");
        }
        boolean succ = CartManager.generateOrder(username);
        if(succ){
            CartManager.clearCart(username);
        }
        String result = "{\"result\":\"" + (succ ? "success" : "failure") + "\"}";
        return result;
    }
}
