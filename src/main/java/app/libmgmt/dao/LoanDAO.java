package app.libmgmt.dao;

import app.libmgmt.model.Loan;
import app.libmgmt.model.Book;
import app.libmgmt.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    private final Connection connection;
    private final BookDAO bookDAO;
    private final UserDAO userDAO;

    public LoanDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.bookDAO = new BookDAO();
        this.userDAO = new UserDAO();
    }

    public void addLoan(Loan loan) {
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
            System.out.println("Loan added successfully");

        } catch (SQLException e) {
            System.err.println("Error adding loan: " + e.getMessage());
        }
    }

    public void updateLoan(Loan loan) {
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
            System.out.println("Loan updated successfully");

        } catch (SQLException e) {
            System.err.println("Error updating loan: " + e.getMessage());
        }
    }

    public void deleteLoan(int loanId) {
        String sql = "DELETE FROM Loan WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);
            statement.executeUpdate();
            System.out.println("Loan deleted successfully");
        } catch (SQLException e) {
            System.err.println("Error deleting loan: " + e.getMessage());
        }
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Loan";

        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Loan loan = mapResultSetToLoan(rs);
                loans.add(loan);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving loans: " + e.getMessage());
        }

        return loans;
    }

    public Loan getLoanById(int loanId) {
        String sql = "SELECT * FROM Loan WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapResultSetToLoan(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving loan: " + e.getMessage());
        }

        return null;
    }

    public List<Loan> getLoansByUserId(int userId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Loan WHERE userId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Loan loan = mapResultSetToLoan(rs);
                loans.add(loan);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving loans: " + e.getMessage());
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

    public Book getBookFromLoan(Loan loan) {
        return bookDAO.getBookByIsbn(loan.getBookIsbn());
    }

    public User getUserFromLoan(Loan loan) {
        return userDAO.getUserById(loan.getUserId());
    }
}
