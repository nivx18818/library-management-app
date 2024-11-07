package app.libmgmt.dao;

import app.libmgmt.model.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookDAO {

    private final Connection connection;

    public BookDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void addBook(Book book) {
        String sql = "INSERT INTO Book(isbn, title, published_date, publisher, cover_url, "
                + "available_amount, authors, categories) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String authorsString = String.join(",", book.getAuthors());
            String categoriesString = String.join(",", book.getCategories());

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setDate(3, new java.sql.Date(book.getPublishedDate().getTime()));
            statement.setString(4, book.getPublisher());
            statement.setString(5, book.getCoverUrl());
            statement.setInt(6, book.getAvailableCopies());
            statement.setString(7, authorsString);
            statement.setString(8, categoriesString);

            statement.executeUpdate();
            System.out.println("Book added");

        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

    public void updateBook(Book book) {
        String sql = "UPDATE Book SET title = ?, published_date = ?, publisher = ?, cover_url = ?, "
                + "available_amount = ?, authors = ?, categories = ? WHERE isbn = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String authorsString = String.join(",", book.getAuthors());
            String categoriesString = String.join(",", book.getCategories());

            statement.setString(1, book.getTitle());
            statement.setDate(2, new java.sql.Date(book.getPublishedDate().getTime()));
            statement.setString(3, book.getPublisher());
            statement.setString(4, book.getCoverUrl());
            statement.setInt(5, book.getAvailableCopies());
            statement.setString(6, authorsString);
            statement.setString(7, categoriesString);
            statement.setString(8, book.getIsbn());

            statement.executeUpdate();
            System.out.println("Book updated");

        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

    public void deleteBook(Book book) {
        String sql = "DELETE FROM Book WHERE isbn = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getIsbn());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Book";

        try (PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                List<String> authors = parseStrings(rs.getString("authors"));
                List<String> categories = parseStrings(rs.getString("category_id"));

                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getDate("published_date"),
                        rs.getString("publisher"),
                        rs.getString("cover_url"),
                        rs.getInt("available_amount"),
                        authors,
                        categories
                );

                books.add(book);
            }

        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }

        return books;
    }

    public Book selectBookByIsbn(String isbn) {
        String sql = "SELECT * FROM Book WHERE isbn = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbn);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                List<String> authors = parseStrings(rs.getString("authors"));
                List<String> categories = parseStrings(rs.getString("category_id"));

                return new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getDate("published_date"),
                        rs.getString("publisher"),
                        rs.getString("cover_url"),
                        rs.getInt("available_amount"),
                        authors,
                        categories
                );
            }

        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }

        return null;
    }

    public List<Book> selectBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE authors LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + author + "%");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                List<String> authors = parseStrings(rs.getString("authors"));
                List<String> categories = parseStrings(rs.getString("category_id"));

                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getDate("published_date"),
                        rs.getString("publisher"),
                        rs.getString("cover_url"),
                        rs.getInt("available_amount"),
                        authors,
                        categories
                );

                books.add(book);
            }

        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }

        return books;
    }

    public List<Book> selectBooksByCategory(String category) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE categories LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + category + "%");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                List<String> authors = parseStrings(rs.getString("authors"));
                List<String> categories = parseStrings(rs.getString("category_id"));

                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getDate("published_date"),
                        rs.getString("publisher"),
                        rs.getString("cover_url"),
                        rs.getInt("available_amount"),
                        authors,
                        categories
                );

                books.add(book);
            }

        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }

        return books;
    }

    private List<String> parseStrings(String st) {
        if (st == null || st.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.asList(st.split(","));
    }
}