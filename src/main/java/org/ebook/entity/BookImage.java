package org.ebook.entity;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document(collection = "image")
public class BookImage {
    @Id
    @Field("isbn")
    private String isbn;

    @Field("image")
    private String image;

    @PersistenceConstructor
    public BookImage(String isbn, String image){
        this.isbn = isbn;
        this.image = image;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
