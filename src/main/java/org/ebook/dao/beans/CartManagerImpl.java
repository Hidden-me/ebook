package org.ebook.dao.beans;

import org.ebook.dao.BookManager;
import org.ebook.dao.CartManager;
import org.ebook.dao.UserManager;
import org.ebook.entity.Book;
import org.ebook.entity.CartItem;
import org.ebook.entity.Order;
import org.ebook.entity.OrderItem;
import org.ebook.util.DatabaseUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartManagerImpl implements CartManager {
    // <username, List<CartItem>>
    private static Map<String, List<CartItem>> carts = new HashMap<>();

    private BookManager bookManager;
    private UserManager userManager;

    @Autowired
    public CartManagerImpl(BookManager bookManager, UserManager userManager){
        this.bookManager = bookManager;
        this.userManager = userManager;
    }

    private CartItem getCartItem(List<CartItem> cart, String isbn){
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
    private CartItem getCartItem(String username, String isbn){
        List<CartItem> cart = carts.get(username);
        return getCartItem(cart, isbn);
    }
    private OrderItem generateOrderItem(Order order, CartItem citem){
        if(order == null){
            return null;
        }
        OrderItem item = new OrderItem(order.getOrderId());
        if(citem != null){
            Book book = bookManager.getBookByISBN(citem.getIsbn());
            if(book != null){
                item.setBookTitle(book.getTitle());
                item.setBookAuthor(book.getAuthor());
            }else{
                item.setBookTitle("");
                item.setBookAuthor("");
            }
            item.setPrice(citem.getPrice());
            item.setAmount(citem.getCount());
            item.setBookIsbn(citem.getIsbn());
        }
        return item;
    }
    public Map<String, Object> getCartItemJSON(CartItem item){
        Map<String, Object> json = new HashMap<>();
        Book book = bookManager.getBookByISBN(item.getIsbn());
        if(book != null){
            json.put("book", bookManager.getBookJSON(book));
            json.put("count", item.getCount());
            json.put("price", item.getPrice());
        }
        return json;
    }

    public Map<String, Object> getCartJSON(String username){
        Map<String, Object> json = new HashMap<>();
        List<Object> listJSON = new ArrayList<>();
        List<CartItem> cart = carts.get(username);
        if(cart != null){
            boolean first = true;
            for(CartItem item : cart){
                listJSON.add(getCartItemJSON(item));
            }
        }
        json.put("items", listJSON);
        return json;
    }
    public void addToCart(String username, Book book){
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
    public void removeFromCart(String username, String isbn){
        List<CartItem> cart = carts.get(username);
        if(cart != null){
            CartItem item = getCartItem(cart, isbn);
            cart.remove(item);
        }
    }
    public void clearCart(String username){
        carts.remove(username);
    }
    public boolean setBookCount(String username, String isbn, int count){
        CartItem item = getCartItem(username, isbn);
        if(item == null){
            return false;
        }
        return item.setCount(count);
    }
    public boolean increaseBookCount(String username, String isbn) {
        CartItem item = getCartItem(username, isbn);
        if(item == null){
            return false;
        }
        return item.increaseCount();
    }
    public boolean decreaseBookCount(String username, String isbn) {
        CartItem item = getCartItem(username, isbn);
        if(item == null){
            return false;
        }
        return item.decreaseCount();
    }
    public boolean generateOrder(String username){
        if(username == null){
            return false;
        }
        int uid = userManager.getUid(username);
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
                // insert order item
                OrderItem oitem = generateOrderItem(order, citem);
                ss.save(oitem);
                // decrease stock
                String isbn = oitem.getBookIsbn();
                int count = oitem.getAmount();
                Book book = bookManager.getBookByISBN(isbn);
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
