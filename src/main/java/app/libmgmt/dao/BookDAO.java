package app.libmgmt.dao;

import app.libmgmt.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookDAO {

    public BookDAO() {
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public String generateNextIsbn() throws SQLException {
        String nextIsbn = "BK000001";
        String sql = "SELECT isbn FROM Book WHERE isbn LIKE 'BK%' ORDER BY isbn DESC LIMIT 1";
    
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
    
            if (resultSet.next()) {
                String lastIsbn = resultSet.getString("isbn");
                int numberPart = Integer.parseInt(lastIsbn.replace("BK", ""));
                nextIsbn = "BK" + String.format("%06d", numberPart + 1);
            }
        }
        return nextIsbn;
    }
    

    public void addBook(Book book) throws SQLException {
        if (book.getIsbn().equals("0") || book.getIsbn().isEmpty()) {
            book.setIsbn(generateNextIsbn()); 
        }

        String sql = "INSERT INTO Book(isbn, title, published_date, publisher, cover_url, "
                + "available_amount, authors, categories) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String authorsString = String.join(",", book.getAuthors());
            String categoriesString = String.join(",", book.getCategories());

            java.sql.Date publishDate = book.getPublishedDate() != null ? new java.sql.Date(book.getPublishedDate().getTime()) : null;
            String publishString = publishDate != null ? publishDate.toString() : null;

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, publishString);
            statement.setString(4, book.getPublisher());
            statement.setString(5, book.getCoverUrl());
            statement.setInt(6, book.getAvailableCopies());
            statement.setString(7, authorsString);
            statement.setString(8, categoriesString);

            statement.executeUpdate();
        }
    }

    public void updateBook(Book book) throws SQLException  {
        String sql = "UPDATE Book SET title = ?, published_date = ?, publisher = ?, cover_url = ?, "
                + "available_amount = ?, authors = ?, categories = ? WHERE isbn = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String authorsString = String.join(",", book.getAuthors());
            String categoriesString = String.join(",", book.getCategories());

            java.sql.Date publishDate = book.getPublishedDate() != null ? new java.sql.Date(book.getPublishedDate().getTime()) : null;
            String publishString = publishDate != null ? publishDate.toString() : null;

            statement.setString(1, book.getTitle());
            statement.setString(2, publishString);
            statement.setString(3, book.getPublisher());
            statement.setString(4, book.getCoverUrl());
            statement.setInt(5, book.getAvailableCopies());
            statement.setString(6, authorsString);
            statement.setString(7, categoriesString);
            statement.setString(8, book.getIsbn());

            statement.executeUpdate();
        }
    }

    public void deleteBookByIsbn(String isbn) throws SQLException  {
        String sql = "DELETE FROM Book WHERE isbn = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbn);
            statement.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT isbn, title, published_date, publisher, cover_url, available_amount, authors, categories FROM Book";
    
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
    
            while (rs.next()) {
                List<String> authors = parseStrings(rs.getString("authors"));
                List<String> categories = parseStrings(rs.getString("categories"));

                String publishedDateString = rs.getString("published_date");
                Date publishedDate = publishedDateString != null ? Date.valueOf(publishedDateString) : null;

                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        publishedDate,
                        rs.getString("publisher"),
                        rs.getString("cover_url"),
                        rs.getInt("available_amount"),
                        authors,
                        categories
                );
    
                books.add(book);
            }
        }
    
        return books;
    }
    

    public Book getBookByIsbn(String isbn) throws SQLException  {
        String sql = "SELECT isbn, title, published_date, publisher, cover_url, available_amount, authors, categories FROM Book WHERE isbn = ?";
        Book book = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, isbn);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    List<String> authors = parseStrings(rs.getString("authors"));
                    List<String> categories = parseStrings(rs.getString("categories"));

                    String publishedDateString = rs.getString("published_date");
                    Date publishedDate = publishedDateString != null ? Date.valueOf(publishedDateString) : null;

                    book = new Book(
                            rs.getString("isbn"),
                            rs.getString("title"),
                            publishedDate,
                            rs.getString("publisher"),
                            rs.getString("cover_url"),
                            rs.getInt("available_amount"),
                            authors,
                            categories);

                } else {
                    System.out.println("Book not found.");
                }
            }
        }

        return book;
    }

    public List<Book> getBooksByAuthor(String author) throws SQLException  {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT isbn, title, published_date, publisher, cover_url, available_amount, authors, categories FROM Book WHERE authors LIKE ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + author + "%");

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    List<String> authors = parseStrings(rs.getString("authors"));
                    List<String> categories = parseStrings(rs.getString("categories"));

                    String publishedDateString = rs.getString("published_date");
                    Date publishedDate = publishedDateString != null ? Date.valueOf(publishedDateString) : null;

                    Book book = new Book(
                            rs.getString("isbn"),
                            rs.getString("title"),
                            publishedDate,
                            rs.getString("publisher"),
                            rs.getString("cover_url"),
                            rs.getInt("available_amount"),
                            authors,
                            categories);

                    books.add(book);
                }
            }
        }

        return books;
    }

    public List<Book> getBooksByCategory(String category) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT isbn, title, published_date, publisher, cover_url, available_amount, authors, categories FROM Book WHERE categories LIKE ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + category + "%");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                List<String> authors = parseStrings(rs.getString("authors"));
                List<String> categories = parseStrings(rs.getString("categories"));

                String publishedDateString = rs.getString("published_date");
                Date publishedDate = publishedDateString != null ? Date.valueOf(publishedDateString) : null;

                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        publishedDate,
                        rs.getString("publisher"),
                        rs.getString("cover_url"),
                        rs.getInt("available_amount"),
                        authors,
                        categories);

                books.add(book);
            }

        } catch (SQLException e) {
            System.out.print("Error in select books by category: " + e.getMessage());
        }

        return books;
    }

    public int countTotalAvailableBooks() throws SQLException {
        int totalBooks = 0;
        String sql = "SELECT SUM(available_amount) FROM Book";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
             
            if (rs.next()) {
                totalBooks = rs.getInt(1);
            }
        }
        
        return totalBooks;
    }

    private List<String> parseStrings(String st) {
        if (st == null || st.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.asList(st.split(","));
    }
}
