package app.libmgmt.dao;

import app.libmgmt.model.Loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    private final Connection connection;

    public LoanDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void addLoan(Loan loan) throws SQLException {
        String sql = "INSERT INTO Loan (status, borrowed_date, returned_date, isbn, userId) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, loan.getStatus());

            statement.setString(2,
                    loan.getBorrowedDate() != null ? loan.getBorrowedDate().toString() : null);

            statement.setString(3,
                    loan.getReturnedDate() != null ? loan.getReturnedDate().toString() : null);

            statement.setString(4, loan.getBookIsbn());
            statement.setInt(5, loan.getUserId());

            statement.executeUpdate();
        }
    }

    public void updateLoan(Loan loan) throws SQLException {
        String sql = "UPDATE Loan SET status = ?, borrowed_date = ?, returned_date = ?, isbn = ?, "
                + "userId = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, loan.getStatus());

            statement.setString(2,
                    loan.getBorrowedDate() != null ? loan.getBorrowedDate().toString() : null);

            statement.setString(3,
                    loan.getReturnedDate() != null ? loan.getReturnedDate().toString() : null);

            statement.setString(4, loan.getBookIsbn());
            statement.setInt(5, loan.getUserId());
            statement.setInt(6, loan.getLoanId());

            statement.executeUpdate();
        }
    }

    public void deleteLoan(int loanId) throws SQLException {
        String sql = "DELETE FROM Loan WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);
            statement.executeUpdate();
        }
    }

    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Loan";

        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Loan loan = mapResultSetToLoan(rs);
                loans.add(loan);
            }
        }

        return loans;
    }

    public Loan getLoanById(int loanId) throws SQLException {
        String sql = "SELECT * FROM Loan WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {
            statement.setInt(1, loanId);

            if (rs.next()) {
                return mapResultSetToLoan(rs);
            }
        }

        return null;
    }

    public List<Loan> getLoansByUserId(int userId) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Loan WHERE userId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Loan loan = mapResultSetToLoan(rs);
                loans.add(loan);
            }
        }

        return loans;
    }

    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        int loanId = rs.getInt("id");
        String status = rs.getString("status");

        String borrowedDateStr = rs.getString("borrowed_date");
        Date borrowedDate = borrowedDateStr != null ? java.sql.Date.valueOf(borrowedDateStr) : null;
        String returnedDateStr = rs.getString("returned_date");
        Date returnedDate = returnedDateStr != null ? java.sql.Date.valueOf(returnedDateStr) : null;

        String isbn = rs.getString("isbn");
        int userId = rs.getInt("userId");

        return new Loan(loanId, borrowedDate, returnedDate, isbn, userId, status);
    }
}
