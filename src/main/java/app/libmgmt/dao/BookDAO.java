package app.libmgmt.dao;

import app.libmgmt.model.Book;

import java.sql.*;
import java.util.ArrayList;

public class BookDAO implements DAOInterface<Book> {

    @Override
    public void add(Book book) {
        String sql = "INSERT INTO Book(isbn, title, published_date, publisher, cover_url, " +
                "available_amount, author_id, category_id, admin_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getPublishedDate());
            statement.setString(4, book.getPublisher());
            statement.setString(5, book.getCoverUrl());
            statement.setInt(6, book.getAvailableAmount());
            statement.setInt(7, book.getAuthorId());
            statement.setInt(8, book.getCategoryId());
            statement.setInt(9, book.getAdminId());

            statement.executeUpdate();
            System.out.println("Book added");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Book book) {
        String sql = "UPDATE Book SET title = ?, published_date = ?, publisher = ?, cover_url = ?, " +
                "available_amount = ?, author_id = ?, category_id = ?, admin_id = ? WHERE isbn = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getPublishedDate());
            statement.setString(3, book.getPublisher());
            statement.setString(4, book.getCoverUrl());
            statement.setInt(5, book.getAvailableAmount());
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

    @Override
    public void delete(Book book) {
        String sql = "DELETE FROM Book WHERE isbn = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, book.getIsbn());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList selectAll() {
        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Book";

        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("published_date"),
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

    @Override
    public ArrayList selectByCondition(String condition) {
        return null;
    }
}
