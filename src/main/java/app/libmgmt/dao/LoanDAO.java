package app.libmgmt.dao;

import app.libmgmt.model.Loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    public LoanDAO() {
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public void addLoan(Loan loan) throws SQLException {
        String sql = "INSERT INTO Loan (user_id, user_name, amount, status, borrowed_date, due_date, book_isbn) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, loan.getUserId());
            statement.setString(2, loan.getUserName());
            statement.setString(3, loan.getAmount());
            statement.setString(4, "BORROWED");

            LocalDate borrowedDate = LocalDate.now();
            DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String borrowedDateString = borrowedDate.format(outputFormat);
            statement.setString(5, borrowedDateString);

            LocalDate dueDate = borrowedDate.plusDays(14);
            String dueDateString = dueDate.format(outputFormat);
            statement.setString(6, dueDateString);

            statement.setString(7, loan.getIsbn());

            statement.executeUpdate();
        }
    }

    public void updateLoan(Loan loan) throws SQLException {
        String sql = "UPDATE Loan SET user_id = ?, user_name = ?, amount = ?, status = ?, " +
                     "book_isbn = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, loan.getUserId());
            statement.setString(2, loan.getUserName());
            statement.setString(3, loan.getAmount());
            statement.setString(4, "BORROWED");

            statement.setString(5, loan.getIsbn());
            statement.setInt(6, loan.getLoanId());
            statement.executeUpdate();
        }
    }

    public void deleteLoanById(int loanId) throws SQLException {
        String sql = "DELETE FROM Loan WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);
            statement.executeUpdate();
        }
    }

    public void deleteLoanByUserId(String userId) throws SQLException {
        String sql = "DELETE FROM Loan WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            statement.executeUpdate();
        }
    }

    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT id, user_id, user_name, amount, status, borrowed_date, due_date, returned_date, book_isbn FROM Loan WHERE status = 'BORROWED'";

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
        String sql = "SELECT id, user_id, user_name, amount, status, borrowed_date, due_date, returned_date, book_isbn " +
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
    
    public List<Loan> getLoansByUserId(String userId) throws SQLException {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is null.");
        }
    
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT id, user_id, user_name, amount, status, borrowed_date, due_date, returned_date, book_isbn " +
                     "FROM Loan WHERE user_id = ?";
    
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

    public List<Loan> getReturnLoansByUserId(String userId) throws SQLException {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is null.");
        }
    
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT id, user_name, user_id, amount, status, borrowed_date, due_date, returned_date, book_isbn " +
                     "FROM Loan WHERE user_id = ? and status = 'RETURNED'";
    
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

    public String getIsbnByUserId (String userId) throws SQLException {
        String sql = "SELECT GROUP_CONCAT(book_isbn) AS isbn_list FROM Loan WHERE user_id = ? and status = 'BORROWED' GROUP BY user_id";
        String isbnList = null;

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    isbnList = rs.getString("isbn_list");
                }
            }
        } 

        return isbnList;
    }
    
    public int countTotalBorrowedBooks() throws SQLException {
        int totalBooks = 0;
        String sql = "SELECT SUM(amount) FROM Loan WHERE status = 'OVERDUE' or status = 'BORROWED'";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
             
            if (rs.next()) {
                totalBooks = rs.getInt(1);
            }
        }
        
        return totalBooks;
    }

    public int getMaxLoanId() throws SQLException {
        int maxId = Integer.MIN_VALUE;
        String sql = "SELECT MAX(id) FROM Loan";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
             
            if (rs.next()) {
                maxId = rs.getInt(1);
            }
        }
        
        return maxId;
    }

    public void updateLoanReturnedDate(int loanId) throws SQLException {
        String sql = "UPDATE Loan SET returned_date = ?, status = 'RETURNED' WHERE id = ?";

        LocalDate returnedDate = LocalDate.now();
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String returnedDateString = returnedDate.format(outputFormat);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, returnedDateString);
            statement.setInt(2, loanId);
            statement.executeUpdate();
        }
    }

    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        int loanId = rs.getInt("id");
        String userId = rs.getString("user_id");
        String bookIsbn = rs.getString("book_isbn");
        String amount = rs.getString("amount");

        String borrowedDateString = rs.getString("borrowed_date");
        Date borrowedDate = borrowedDateString != null ? Date.valueOf(borrowedDateString) : null;

        String dueDateString = rs.getString("due_date");
        Date dueDate = dueDateString != null ? Date.valueOf(dueDateString) : null;

        String returnedDateString = rs.getString("returned_date");
        Date returnedDate = returnedDateString != null
                ? Date.valueOf(returnedDateString)
                : Date.valueOf(LocalDate.of(2000, 1, 1));

        String status = rs.getString("status");

        return new Loan(loanId, userId, bookIsbn, amount, borrowedDate, dueDate, returnedDate, status);
    }

    public Loan getLoanById(int loanId) throws SQLException {
        Loan loan = null;
        String sql = "SELECT id, user_id, user_name, amount, status, borrowed_date, due_date, returned_date, book_isbn " +
                     "FROM Loan WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    loan = mapResultSetToLoan(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loan;
    }

}
