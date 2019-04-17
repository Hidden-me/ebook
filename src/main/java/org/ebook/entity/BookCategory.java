package org.ebook.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "book_category")
public class BookCategory {
    private int id;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
