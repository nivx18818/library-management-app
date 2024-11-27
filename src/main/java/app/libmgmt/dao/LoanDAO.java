package app.libmgmt.dao;

import app.libmgmt.model.Loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    public LoanDAO() {
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public void addLoan(Loan loan) throws SQLException {
        String sql = "INSERT INTO Loan (user_name, userid, amount, status, borrowed_date, due_date, book_isbn) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set values for the prepared statement
            statement.setString(1, loan.getUserName());
            statement.setString(2, loan.getUserId());
            statement.setDouble(3, loan.getAmount());
            statement.setString(4, "BORROWED");

            LocalDate borrowedDate = LocalDate.now();
            String borrowedDateString = Date.valueOf(borrowedDate).toString();
            statement.setString(5, borrowedDateString);

            LocalDate dueDate = borrowedDate.plusDays(14);
            String dueDateString = Date.valueOf(dueDate).toString();
            statement.setString(6, dueDateString);

            statement.setString(7, loan.getIsbn());

            statement.executeUpdate();
        }
    }

    public void updateLoan(Loan loan) throws SQLException {
        String sql = "UPDATE Loan SET user_name = ?, userid = ?, amount = ?, status = ?, " +
                     "borrowed_date = ?, due_date = ?, returned_date = ?, book_isbn = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, loan.getUserName());
            statement.setString(2, loan.getUserId());
            statement.setDouble(3, loan.getAmount());
            statement.setString(4, loan.getStatus());

            Date borrowDate = new java.sql.Date(loan.getBorrowedDate().getTime());
            String borrowString = borrowDate.toString();
            statement.setString(5, borrowString);

            Date duDate = new java.sql.Date(loan.getDueDate().getTime());
            String dueDateString = duDate.toString();
            statement.setString(6, dueDateString);

            Date returnedDate = new java.sql.Date(loan.getReturnedDate().getTime());
            String returnedDateString = returnedDate.toString();
            statement.setString(7, returnedDateString);
            
            statement.setString(8, loan.getIsbn());
            statement.setInt(9, loan.getLoanId());

            statement.executeUpdate();
        }
    }

    public void deleteLoan(int loanId) throws SQLException {
        String sql = "DELETE FROM Loan WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);
            statement.executeUpdate();
        }
    }

    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT id, user_name, userid, amount, status, borrowed_date, due_date, returned_date, book_isbn FROM Loan WHERE status = 'BORROWED'";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Loan loan = mapResultSetToLoan(rs);
                loans.add(loan);
            }
        }

        return loans;
    }

    public List<Loan> getOverdueLoans() throws SQLException {
        List<Loan> overdueLoans = new ArrayList<>();
        String sql = "SELECT id, user_name, userid, amount, status, borrowed_date, due_date, returned_date, book_isbn " +
                     "FROM Loan WHERE status = 'OVERDUE'";
    
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Loan loan = mapResultSetToLoan(rs);
                overdueLoans.add(loan);
            }
        }
    
        return overdueLoans;
    }
    
    public void markOverdueLoans() throws SQLException {
        String updateDueDateSql = "UPDATE Loan " +
                                  "SET due_date = DATE(borrowed_date, '+14 day') " +
                                  "WHERE due_date IS NULL AND status = 'BORROWED'";
    
        // UPDATE Loan SET status = 'OVERDUE' WHERE status = 'BORROWED' AND due_date < DATE('now');
        String markOverdueSql = "UPDATE Loan " +
                                "SET status = 'OVERDUE' " +
                                "WHERE status = 'BORROWED' AND due_date < DATE('now')";
    
        try (Connection connection = getConnection()) {
            try (PreparedStatement updateDueDateStmt = connection.prepareStatement(updateDueDateSql)) {
                int dueDateRowsUpdated = updateDueDateStmt.executeUpdate();
                System.out.println("Updated due_date for " + dueDateRowsUpdated + " loans.");
            }
    
            try (PreparedStatement markOverdueStmt = connection.prepareStatement(markOverdueSql)) {
                int overdueRowsUpdated = markOverdueStmt.executeUpdate();
                System.out.println("Marked " + overdueRowsUpdated + " loans as OVERDUE.");
            }
        }
    }
    
    
    public Loan getLoanById(int loanId) throws SQLException {
        String sql = "SELECT id, user_name, userid, amount, status, borrowed_date, due_date, returned_date, book_isbn FROM Loan WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoan(rs);
                }
            }
        }

        return null;
    }

    public List<Loan> getLoansByUserId(String userId) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT id, user_name, userid, amount, status, borrowed_date, due_date, returned_date, book_isbn FROM Loan WHERE userid = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Loan loan = mapResultSetToLoan(rs);
                    loans.add(loan);
                }
            }
        }

        return loans;
    }

    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        int loanId = rs.getInt("id");
        String userId = rs.getString("userid");
        int amount = rs.getInt("amount");
        String status = rs.getString("status");

        // String publishedDateString = rs.getString("published_date");
        // Date publishedDate = publishedDateString != null ? Date.valueOf(publishedDateString) : null;

        String borrowedDateString = rs.getString("borrowed_date");
        Date borrowedDate = borrowedDateString !=  null ? Date.valueOf(borrowedDateString) : null;
        String dueDateString = rs.getString("due_date");
        Date dueDate = dueDateString != null ? Date.valueOf(dueDateString) : null;
        // String returnedDateString = rs.getString("returned_date");
        // Date returnedDate = returnedDateString != null ? Date.valueOf(returnedDateString) : null;

        String bookIsbn = rs.getString("book_isbn");

        // int loanId, String userId, String isbn, int amount, Date borrowedDate, Date dueDate, String status
        return new Loan(loanId, userId, bookIsbn, amount, borrowedDate, dueDate, status);
    }
}
