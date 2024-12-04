package app.libmgmt.service;

import app.libmgmt.model.Book;
import app.libmgmt.dao.DatabaseConnection;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

public class BookServiceTest {

    private BookService bookService;

    @BeforeAll
    static void setUpClass() throws SQLException {
        DatabaseConnection.isTesting = true;
    }

    @BeforeEach
    void setUp() throws Exception {
        Connection connection = DatabaseConnection.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Book");
            statement.execute("CREATE TABLE Book (" +
                    "isbn TEXT PRIMARY KEY, " +
                    "title TEXT, " +
                    "published_date TEXT, " +
                    "publisher TEXT, " +
                    "cover_url TEXT, " +
                    "available_amount INTEGER, " +
                    "authors TEXT, " +
                    "categories TEXT, " +
                    "web_reader_url TEXT)");

            // Prepopulate with test data
            statement.execute("INSERT INTO Book VALUES (" +
                    "'1234567890', 'Test Book 1', '2024-01-01', 'Test Publisher 1', " +
                    "'http://test.com/cover-1.jpg', 10, 'Author1', 'Category1', 'http://test.com/reader')");
            statement.execute("INSERT INTO Book VALUES (" +
                    "'2345678901', 'Test Book 2', '2024-01-01', 'Test Publisher 2', " +
                    "'http://test.com/cover-2.jpg', 10, 'Author2', 'Category2', 'http://test.com/reader')");
            statement.execute("INSERT INTO Book VALUES (" +
                    "'3456789012', 'Test Book 3', '2024-01-01', 'Test Publisher 3', " +
                    "'http://test.com/cover-3.jpg', 10, 'Author3', 'Category3', 'http://test.com/reader')");
        }

        bookService = new BookService();
    }

    @AfterAll
    static void tearDownClass() {
        DatabaseConnection.isTesting = false;
    }

    @Test
    void testAddBook() {
        Book book = new Book("TestAddBook", "Book TestAdd", new Date(), "Publisher TestAdd", "http://test-add.com/cover.jpg", 10,
                List.of("Author2"), List.of("Category2"), "http://test-add.com/reader");
        bookService.addBook(book);
        Book retrievedBook = bookService.getBookByIsbn("TestAddBook");
        Assertions.assertNotNull(retrievedBook);
        Assertions.assertEquals("Book TestAdd", retrievedBook.getTitle());
        Assertions.assertEquals("Publisher TestAdd", retrievedBook.getPublisher());
        Assertions.assertEquals("http://test-add.com/cover.jpg", retrievedBook.getCoverUrl());
        Assertions.assertEquals(10, retrievedBook.getAvailableCopies());
        Assertions.assertEquals("Author2", retrievedBook.getAuthors().get(0));
        Assertions.assertEquals("Category2", retrievedBook.getCategories().get(0));
        Assertions.assertEquals("http://test-add.com/reader", retrievedBook.getWebReaderUrl());
    }
    
    @Test
    void testUpdateBook() {
        Book book = bookService.getBookByIsbn("1234567890");
        book.setTitle("Updated Test Book 1");
        bookService.updateBook(book);
        Book updatedBook = bookService.getBookByIsbn("1234567890");
        Assertions.assertEquals("Updated Test Book 1", updatedBook.getTitle());
    }

    @Test
    void testDeleteBookByIsbn() {
        bookService.deleteBookByIsbn("1234567890");
        Book deletedBook = bookService.getBookByIsbn("1234567890");
        Assertions.assertNull(deletedBook);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = bookService.getAllBooks();
        Assertions.assertEquals(3, books.size());
    }

    @Test
    void testGetBookByIsbn() {
        Book retrievedBook = bookService.getBookByIsbn("1234567890");
        Assertions.assertNotNull(retrievedBook);
        Assertions.assertEquals("Test Book 1", retrievedBook.getTitle());
        Assertions.assertEquals("2024-01-01", retrievedBook.getPublishedDate().toString());
        Assertions.assertEquals("Test Publisher 1", retrievedBook.getPublisher());
        Assertions.assertEquals("http://test.com/cover-1.jpg", retrievedBook.getCoverUrl());
        Assertions.assertEquals(10, retrievedBook.getAvailableCopies());
        Assertions.assertEquals("Author1", retrievedBook.getAuthors().get(0));
        Assertions.assertEquals("Category1", retrievedBook.getCategories().get(0));
        Assertions.assertEquals("http://test.com/reader", retrievedBook.getWebReaderUrl());
    }

    @Test
    void testGetBooksByAuthor() {
        List<Book> books = bookService.getBooksByAuthor("Author1");
        Assertions.assertEquals(1, books.size());
        Assertions.assertEquals("Test Book 1", books.get(0).getTitle());
        Assertions.assertEquals("2024-01-01", books.get(0).getPublishedDate().toString());
        Assertions.assertEquals("Test Publisher 1", books.get(0).getPublisher());
        Assertions.assertEquals("http://test.com/cover-1.jpg", books.get(0).getCoverUrl());
        Assertions.assertEquals(10, books.get(0).getAvailableCopies());
        Assertions.assertEquals("Category1", books.get(0).getCategories().get(0));
        Assertions.assertEquals("http://test.com/reader", books.get(0).getWebReaderUrl());
    }

    @Test
    void testGetBooksByCategory() {
        List<Book> books = bookService.getBooksByCategory("Category1");
        Assertions.assertEquals(1, books.size());
        Assertions.assertEquals("Test Book 1", books.get(0).getTitle());
        Assertions.assertEquals("2024-01-01", books.get(0).getPublishedDate().toString());
        Assertions.assertEquals("Test Publisher 1", books.get(0).getPublisher());
        Assertions.assertEquals("http://test.com/cover-1.jpg", books.get(0).getCoverUrl());
        Assertions.assertEquals(10, books.get(0).getAvailableCopies());
        Assertions.assertEquals("Author1", books.get(0).getAuthors().get(0));
        Assertions.assertEquals("http://test.com/reader", books.get(0).getWebReaderUrl());
    }

    @Test
    void testCountBook() {
        int count = bookService.countBook();
        Assertions.assertEquals(30, count);
    }
}
