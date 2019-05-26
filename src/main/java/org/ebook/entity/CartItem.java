package org.ebook.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CartItem {
    private String isbn;
    private int count, stock;
    private BigDecimal price;

    public CartItem(Book book){
        isbn = book.getIsbn();
        count = 1;
        price = book.getPrice();
        stock = book.getStock();
    }

    public String getIsbn() {
        return isbn;
    }

    public int getCount() {
        return count;
    }

    public boolean setCount(int count){
        if(count > 0 && count <= stock){
            this.count = count;
            return true;
        }
        return false;
    }

    public boolean increaseCount(){
        return setCount(this.count + 1);
    }

    public boolean decreaseCount(){
        return setCount(this.count - 1);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean setPrice(BigDecimal price){
        if(price.compareTo(BigDecimal.ZERO) > 0){
            this.price = price;
            return true;
        }
        return false;
    }

}
