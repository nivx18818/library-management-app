package app.libmgmt.dao;

import app.libmgmt.model.Loan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LoanDAO {
    private Connection connection;

    public LoanDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void addLoan(Loan loan) {
        String sql = "INSERT INTO Loan(status, borrowed_date, returned_date, book_isbn, userId) VALUES(?, ?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, loan.getStatus());
            statement.setLong(2, loan.getBorrowedDate().getTime());
            statement.setLong(3, loan.getReturnedDate() != null ? loan.getReturnedDate().getTime() : null);
            statement.setString(4, loan.getBookIsbn());
            statement.setInt(5, loan.getUserId());

            statement.executeUpdate();
            System.out.println("Loan added");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLoan(Loan loan) {
        String sql = "UPDATE Loan SET status = ?, borrowed_date = ?, returned_date = ?, book_isbn = ?, userId = ? WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, loan.getStatus());
            statement.setLong(2, loan.getBorrowedDate().getTime());
            statement.setLong(3, loan.getReturnedDate() != null ? loan.getReturnedDate().getTime() : null);
            statement.setString(4, loan.getBookIsbn());
            statement.setInt(5, loan.getUserId());
            statement.setInt(6, loan.getId());

            statement.executeUpdate();
            System.out.println("Loan updated");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLoan(int id) {
        String sql = "DELETE FROM Loan WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Loan deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Loan> getAllLoan() {
        ArrayList<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Loan";

        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Loan loan = new Loan(
                        rs.getInt("id"),
                        rs.getString("status"),
                        rs.getDate("borrowed_date"),
                        rs.getDate("returned_date"),
                        rs.getString("book_isbn"),
                        rs.getInt("userid")
                );

                loans.add(loan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loans;
    }

    public Loan getLoanById(int id) {
        String sql = "SELECT * FROM Loan WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Loan(
                        resultSet.getInt("id"),
                        resultSet.getString("status"),
                        resultSet.getDate("borrowed_date"),
                        resultSet.getDate("returned_date"),
                        resultSet.getString("book_isbn"),
                        resultSet.getInt("userId")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}