package app.libmgmt.model;

import app.libmgmt.model.Book;

import java.util.List;

public class Category {
    private int id;
    private String name;

    public Category() {
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooksInCategory() {
        // TODO: Get books in this category
    }
}
