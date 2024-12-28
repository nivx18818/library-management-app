package app.libmgmt.service;

import app.libmgmt.model.Loan;
import app.libmgmt.dao.DatabaseConnection;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

public class LoanServiceTest {

    private LoanService loanService;

    @BeforeAll
    static void setUpClass() throws SQLException {
        DatabaseConnection.isTesting = true;
    }

    @BeforeEach
    void setUp() throws Exception {
        Connection connection = DatabaseConnection.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Loan");
            statement.execute("CREATE TABLE Loan ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "user_id TEXT, "
                    + "user_name TEXT, "
                    + "amount INTEGER, "
                    + "status TEXT, "
                    + "borrowed_date TEXT, "
                    + "due_date TEXT, "
                    + "returned_date TEXT, "
                    + "book_isbn TEXT)");

            // Prepopulate with test data
            statement.execute(
                    "INSERT INTO Loan (user_id, user_name, amount, status, borrowed_date, due_date, book_isbn) "
                            + "VALUES ('1', 'User1', 1, 'OVERDUE', '2023-10-01', '2023-10-15', '1234567890')");
            statement.execute(
                    "INSERT INTO Loan (user_id, user_name, amount, status, borrowed_date, due_date, book_isbn) "
                            + "VALUES ('2', 'User2', 2, 'BORROWED', '2023-10-01', '2023-10-15', '2345678901')");
            statement.execute(
                    "INSERT INTO Loan (user_id, user_name, amount, status, borrowed_date, due_date, book_isbn) "
                            + "VALUES ('3', 'User3', 1, 'RETURNED', '2023-10-01', '2023-10-15', '3456789012')");

            // Testing requires User table to be present
            statement.execute("DROP TABLE IF EXISTS User");
            statement.execute("CREATE TABLE User (" +
                    "id TEXT PRIMARY KEY, " +
                    "name TEXT, " +
                    "email TEXT, " +
                    "password TEXT, " +
                    "role TEXT, " +
                    "admin_id INTEGER, " +
                    "student_id TEXT, " +
                    "major TEXT, " +
                    "social_id TEXT, " +
                    "phone_number TEXT, " +
                    "salt TEXT)");

            // Prepopulate with test data
            statement.execute("INSERT INTO User VALUES (" +
                    "'1', 'User1', 'student@test.com', 'password', 'STUDENT', NULL, 'S1', 'CS', NULL, NULL, 'salt')");
            statement.execute("INSERT INTO User VALUES (" +
                    "'2', 'User2', 'student@test.com', 'password', 'STUDENT', NULL, 'S1', 'CS', NULL, NULL, 'salt')");
            statement.execute("INSERT INTO User VALUES (" +
                    "'3', 'User3', 'external@test.com', 'password', 'EXTERNAL_BORROWER', NULL, NULL, NULL, '123456789', '1234567890', 'salt')");
        }

        loanService = new LoanService();
    }

    @AfterAll
    static void tearDownClass() {
        DatabaseConnection.isTesting = false;
    }

    @Test
    void testAddLoan() {
        Loan loan = new Loan("3", "4567890123", "1", new Date(), "BORROWED");
        loanService.addLoan(loan);
        List<Loan> loans = loanService.getLoansByUserId("3");
        Assertions.assertEquals(2, loans.size());
        Assertions.assertEquals("3", loans.get(1).getUserId());
        Assertions.assertEquals("User3", loans.get(1).getUserName());
        Assertions.assertEquals("1", loans.get(1).getAmount());
        // Assertions.assertEquals("BORROWED", loans.get(1).getStatus());
        Assertions.assertNotNull(loans.get(1).getBorrowedDate());
        Assertions.assertNotNull(loans.get(1).getDueDate());
        Assertions.assertEquals("2000-01-01", loans.get(1).getReturnedDate().toString());
        Assertions.assertEquals("4567890123", loans.get(1).getIsbn());
    }

    @Test
    void testUpdateLoan() {
        Loan loan = loanService.getLoansByUserId("1").get(0);
        loan.setAmount("2");
        loanService.updateLoan(loan);
        Loan updatedLoan = loanService.getLoansByUserId("1").get(0);
        Assertions.assertEquals("1", updatedLoan.getAmount());
    }

    @Test
    void testDeleteLoanById() {
        Loan loan = loanService.getLoansByUserId("1").get(0);
        loanService.deleteLoanById(loan.getLoanId());
        List<Loan> loans = loanService.getLoansByUserId("1");
        Assertions.assertTrue(loans.isEmpty());
    }

    @Test
    void testDeleteLoanByUserId() {
        loanService.deleteLoanByUserId("1");
        List<Loan> loans = loanService.getLoansByUserId("1");
        Assertions.assertTrue(loans.isEmpty());
    }

    @Test
    void testGetAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        Assertions.assertEquals(1, loans.size());
    }

    @Test
    void testGetOverdueLoans() {
        List<Loan> loans = loanService.getOverdueLoans();
        Assertions.assertEquals(1, loans.size());
    }

    @Test
    void testMarkOverdueLoans() {
        loanService.markOverdueLoans();
        List<Loan> loans = loanService.getOverdueLoans();
        Assertions.assertEquals(2, loans.size());
    }

    @Test
    void testGetLoansByUserId() {
        List<Loan> loans = loanService.getLoansByUserId("1");
        Assertions.assertEquals(1, loans.size());
    }

    @Test
    void testGetReturnLoansByUserId() {
        List<Loan> loans = loanService.getReturnLoansByUserId("1");
        Assertions.assertEquals(0, loans.size());
    }

    @Test
    void testGetIsbnByUserId() {
        String isbn = loanService.getIsbnByUserId("2");
        Assertions.assertEquals("2345678901", isbn);
    }

    @Test
    void testCountTotalBorrowedBooks() {
        int count = loanService.countTotalBorrowedBooks();
        Assertions.assertEquals(3, count);
    }

    @Test
    void testUpdateLoanReturnedDate() {
        Loan loan = loanService.getLoansByUserId("2").get(0);
        loanService.updateLoanReturnedDate(loan.getLoanId());
        Loan updatedLoan = loanService.getLoansByUserId("2").get(0);
        Assertions.assertNotNull(updatedLoan.getReturnedDate());
    }
}
