package app.libmgmt.model;

import java.util.List;

public class Author {
    private int id;
    private String name;

    public Author() {
    }

    public Author(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooksWritten() {
        // TODO: Get books written by this author
    }
}
