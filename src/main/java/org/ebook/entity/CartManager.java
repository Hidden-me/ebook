package org.ebook.entity;

import org.ebook.util.DatabaseUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.*;

public class CartManager {
    // <username, List<CartItem>>
    private static Map<String, List<CartItem>> carts = new HashMap<>();
    private static CartItem getCartItem(List<CartItem> cart, String isbn){
        if(cart == null){
            return null;
        }
        CartItem item = null;
        for(CartItem i : cart){
            if(i.getIsbn().equals(isbn)){
                item = i;
                break;
            }
        }
        return item;
    }
    private static CartItem getCartItem(String username, String isbn){
        List<CartItem> cart = carts.get(username);
        return getCartItem(cart, isbn);
    }
    public static String getCartJSONString(String username){
        List<CartItem> cart = carts.get(username);
        if(cart == null){
            return "{\"items\":[]}";
        }
        String result = "{\"items\":[";
        boolean first = true;
        for(CartItem item : cart){
            String isbn = item.getIsbn();
            int count = item.getCount();
            BigDecimal price = item.getPrice();
            Book book = BookManager.getBookByISBN(isbn);
            if(book != null){
                if(first){
                    first = false;
                }else{
                    result += ",";
                }
                result += "{\"book\":";
                result += book.toJSONString();
                result += ",\"count\":\"" + count + "\"";
                result += ",\"price\":\"" + price + "\"}";
            }
        }
        result += "]}";
        return result;
    }
    public static void addToCart(String username, Book book){
        List<CartItem> cart = carts.get(username);
        if(cart == null){
            cart = new ArrayList<>();
            carts.put(username, cart);
            cart.add(new CartItem(book));
        }else{
            CartItem item = getCartItem(cart, book.getIsbn());
            if(item == null){
                cart.add(new CartItem(book));
            }else{
                item.increaseCount();
            }
        }
    }
    public static void removeFromCart(String username, String isbn){
        List<CartItem> cart = carts.get(username);
        if(cart != null){
            CartItem item = getCartItem(cart, isbn);
            cart.remove(item);
        }
    }
    public static void clearCart(String username){
        carts.remove(username);
    }
    public static boolean setBookCount(String username, String isbn, int count){
        CartItem item = getCartItem(username, isbn);
        if(item == null){
            return false;
        }
        return item.setCount(count);
    }
    public static boolean increaseBookCount(String username, String isbn) {
        CartItem item = getCartItem(username, isbn);
        if(item == null){
            return false;
        }
        return item.increaseCount();
    }
    public static boolean decreaseBookCount(String username, String isbn) {
        CartItem item = getCartItem(username, isbn);
        if(item == null){
            return false;
        }
        return item.decreaseCount();
    }
    public static boolean generateOrder(String username){
        if(username == null){
            return false;
        }
        int uid = UserManager.getUid(username);
        if(uid < 0){
            return false;
        }
        List<CartItem> cart = carts.get(username);
        if(cart == null){
            return false;
        }
        Order order = new Order();
        order.setBuyerUid(uid);
        // start DB transaction
        boolean succ = false;
        Session ss = DatabaseUtils.getSession();
        Transaction ts = null;
        try {
            ts = ss.beginTransaction();
            ss.save(order);
            for(CartItem citem : cart){
                // save order item
                OrderItem oitem = new OrderItem(order, citem);
                ss.save(oitem);
                // decrease stock
                String isbn = oitem.getBookIsbn();
                int count = oitem.getAmount();
                Book book = BookManager.getBookByISBN(isbn);
                if(book == null){
                    throw new HibernateException("book with ISBN " +
                            oitem.getBookIsbn() + " does not exist");
                }else{
                    int dst = book.getStock() - count;
                    if(dst < 0){
                        throw new HibernateException("book with ISBN " +
                                oitem.getBookIsbn() + " is out of stock");
                    }
                    book.setStock(dst);
                    ss.update(book);
                }
            }
            // commit
            ts.commit();
            succ = true;
        } catch (HibernateException e) {
            if (ts != null) {
                ts.rollback();
            }
            e.printStackTrace();
        } finally {
            ss.close();
        }
        return succ;
    }
}
