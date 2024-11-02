package app.libmgmt.dao;

import app.libmgmt.model.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookDAO {

    private Connection connection;

    public BookDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void addBook(Book book) {
        String sql = "INSERT INTO Book(isbn, title, published_date, publisher, cover_url, " +
                "available_amount, author_id, category_id, admin_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setDate(3, new java.sql.Date(book.getPublishedDate().getTime()));
            statement.setString(4, book.getPublisher());
            statement.setString(5, book.getCoverUrl());
            statement.setInt(6, book.getAvailableCopies());
            statement.setInt(7, book.getAuthorId());
            statement.setInt(8, book.getCategoryId());
            statement.setInt(9, book.getAdminId());

            statement.executeUpdate();
            System.out.println("Book added");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        String sql = "UPDATE Book SET title = ?, published_date = ?, publisher = ?, cover_url = ?, " +
                "available_amount = ?, author_id = ?, category_id = ?, admin_id = ? WHERE isbn = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, book.getTitle());
            statement.setDate(2, new java.sql.Date(book.getPublishedDate().getTime()));
            statement.setString(3, book.getPublisher());
            statement.setString(4, book.getCoverUrl());
            statement.setInt(5, book.getAvailableCopies());
            statement.setInt(6, book.getAuthorId());
            statement.setInt(7, book.getCategoryId());
            statement.setInt(8, book.getAdminId());
            statement.setString(9, book.getIsbn());

            statement.executeUpdate();
            System.out.println("Book updated");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(Book book) {
        String sql = "DELETE FROM Book WHERE isbn = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, book.getIsbn());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> getAllBook() {
        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Book";

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getDate("published_date"),
                        rs.getString("publisher"),
                        rs.getString("cover_url"),
                        rs.getInt("available_amount"),
                        rs.getInt("author_id"),
                        rs.getInt("category_id"),
                        rs.getInt("admin_id")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public Book getBookByISBN(String isbn) {
        String sql = "SELECT * FROM Book WHERE isbn = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, isbn);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getDate("published_date"),
                        rs.getString("publisher"),
                        rs.getString("cover_url"),
                        rs.getInt("available_amount"),
                        rs.getInt("author_id"),
                        rs.getInt("category_id"),
                        rs.getInt("admin_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}