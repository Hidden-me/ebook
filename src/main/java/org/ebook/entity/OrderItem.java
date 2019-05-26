package org.ebook.entity;

import org.ebook.dao.BookManager;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItem {
    private int itemId;
    private int orderId;
    private String bookIsbn;
    private String bookTitle;
    private String bookAuthor;
    private BigDecimal price;
    private int amount;

    public OrderItem(){}

    public OrderItem(int orderId){
        setOrderId(orderId);
    }

    public OrderItem(Order order, CartItem citem){
        if(order != null){
            setOrderId(order.getOrderId());
        }else{
            return;
        }
        if(citem != null){
            Book book = BookManager.getBookByISBN(citem.getIsbn());
            if(book != null){
                setBookTitle(book.getTitle());
                setBookAuthor(book.getAuthor());
            }else{
                setBookTitle("");
                setBookAuthor("");
            }
            setPrice(citem.getPrice());
            setAmount(citem.getCount());
            setBookIsbn(citem.getIsbn());
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Column(name = "order_id")
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Column(name = "book_isbn")
    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    @Column(name = "book_title")
    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        if(bookTitle != null){
            this.bookTitle = bookTitle;
        }else{
            this.bookTitle = "";
        }
    }

    @Column(name = "book_author")
    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        if(bookAuthor != null){
            this.bookAuthor = bookAuthor;
        }else{
            this.bookAuthor = "";
        }
    }

    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "amount")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
