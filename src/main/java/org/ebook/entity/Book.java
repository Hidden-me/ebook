package org.ebook.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "book")
public class Book {
    private String isbn;
    private String title;
    private String author;
    private BigDecimal price;
    private int stock;
    private byte[] image;
    private Integer categoryId;

    @Id
    @Column(name = "isbn")
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "stock")
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Column(name = "image")
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Column(name = "category_id")
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String toJSONString(){
        //TODO: image BLOB
        String result = "{\"title\":\"" + getTitle() + "\","
                + "\"author\":\"" + getAuthor() + "\","
                + "\"image\":\"" /*+ getImage()*/ + "\","
                + "\"isbn\":\"" + getIsbn() + "\","
                + "\"stock\":\"" + getStock() + "\","
                + "\"price\":\"" + getPrice() + "\"}";
        return result;
    }
}
