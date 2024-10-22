package app.libmgmt.dao;

import app.libmgmt.model.Loan;
import java.sql.*;
import java.util.ArrayList;

public class LoanDAO implements DAOInterface<Loan> {

    @Override
    public void add(Loan loan) {
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

    @Override
    public void update(Loan loan) {
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

    @Override
    public void delete(Loan loan) {
        String sql = "DELETE FROM Loan WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, loan.getId());
            statement.executeUpdate();
            System.out.println("Loan deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Loan> selectAll() {
        ArrayList<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Book";
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Loan loan = new Loan(
                        rs.getInt("id"),
                        rs.getString("status"),
                        rs.getString("brrower_date"),
                        rs.getString("returned_date"),
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

    @Override
    public ArrayList<Loan> selectByCondition(String condition) {
        return null;
    }
}
