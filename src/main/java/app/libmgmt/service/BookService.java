package app.libmgmt.service;

import app.libmgmt.dao.BookDAO;
import app.libmgmt.model.Book;

import java.sql.SQLException;
import java.util.List;

public class BookService {
    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    public void addBook(Book book) {
        try {
            bookDAO.addBook(book);
            System.out.println("Book added successfully");
        } catch (SQLException e) {
            throw new ServiceException("Error in book add: ", e);
        }
    }

    public void updateBook(Book book) {
        try {
            bookDAO.updateBook(book);
            System.out.println("Book updated successfully");
        } catch (SQLException e) {
            throw new ServiceException("Error in book update: ", e);
        }
    }

    public void deleteBookByIsbn(String isbn) {
        try {
            bookDAO.deleteBookByIsbn(isbn);
            System.out.println("Book deleted successfully");
        } catch (SQLException e) {
            throw new ServiceException("Error in book delete: ", e);
        }
    }

    public List<Book> getAllBooks() {
        try {
            return bookDAO.getAllBooks();
        } catch (SQLException e) {
            throw new ServiceException("Error in getting all books: ", e);
        }
    }

    public Book getBookByIsbn(String isbn) {
        try {
            return bookDAO.getBookByIsbn(isbn);
        } catch (SQLException e) {
            throw new ServiceException("Error in getting book by isbn: ", e);
        }
    }

    public List<Book> getBooksByAuthor(String author) {
        try {
            return bookDAO.getBooksByAuthor(author);
        } catch (SQLException e) {
            throw new ServiceException("Error in getting books by author: ", e);
        }
    }

    public List<Book> getBooksByCategory(String category) {
        try {
            return bookDAO.getBooksByCategory(category);
        } catch (SQLException e) {
            throw new ServiceException("Error in getting books by category: ", e);
        }
    }

    public int countBook() {
        try {
            return bookDAO.countBook();
        } catch (SQLException e) {
            throw new ServiceException("Error in getting count books: ", e);
        }
    }
}
