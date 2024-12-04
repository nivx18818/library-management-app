package app.libmgmt.model;

import java.util.Date;
import java.util.List;

import app.libmgmt.util.DateUtils;

public class Book {
    private String isbn;
    private String title;
    private Date publishedDate;
    private String publisher;
    private String coverUrl;
    private String webReaderUrl;
    private int availableCopies;
    private List<String> authors;
    private List<String> categories;

    public Book(String isbn, String title, Date publishedDate, String publisher, String coverUrl,
            int availableCopies, List<String> authors, List<String> categories, String webReaderUrl) {
        this.isbn = isbn;
        this.title = title;
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.coverUrl = coverUrl;
        this.availableCopies = availableCopies;
        this.authors = authors;
        this.categories = categories;
        this.webReaderUrl = webReaderUrl;
    }

    // data format: [id, coverURL, name, type, author, quantity, publisher, publishedDate, webReaderUrl]
    public Book(String[] bookData) {
        this.isbn = bookData[0];
        this.title = bookData[2];
        this.publishedDate = (bookData[7].equals("Not Available")) ? null :
                DateUtils.parseStringToDate(bookData[7]);
        this.publisher = bookData[6];
        this.coverUrl = bookData[1];
        this.availableCopies = Integer.parseInt(bookData[5]);
        this.authors = List.of(bookData[4].split(","));
        this.categories = List.of(bookData[3].split(","));
        this.webReaderUrl = bookData[8];
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public String getWebReaderUrl() {
        return webReaderUrl;
    }

    public void setWebReaderUrl(String webReaderUrl) {
        this.webReaderUrl = webReaderUrl;
    }

    public void updateAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return "Book [authors=" + authors + ", availableCopies=" + availableCopies + ", categories=" + categories
                + ", coverUrl=" + coverUrl + ", isbn=" + isbn + ", publishedDate=" + publishedDate + ", publisher="
                + publisher + ", title=" + title + ", webReaderUrl=" + webReaderUrl + "]";
    }
}
