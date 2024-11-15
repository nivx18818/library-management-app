package app.libmgmt.service;

import app.libmgmt.dao.LoanDAO;
import app.libmgmt.model.Book;
import app.libmgmt.model.Loan;
import app.libmgmt.model.User;

import java.sql.SQLException;
import java.util.List;

public class LoanService {
    private final LoanDAO loanDAO;
    private final BookService bookService;
    private final UserService userService;

    public LoanService() throws SQLException {
        this.loanDAO = new LoanDAO();
        this.bookService = new BookService();
        this.userService = new UserService();
    }

    public void addLoan(Loan loan) {
        try {
            loanDAO.addLoan(loan);
            System.out.println("Loan added successfully");
        } catch (SQLException e) {
            throw new ServiceException("Error adding loan", e);
        }
    }

    public void updateLoan(Loan loan) {
        try {
            loanDAO.updateLoan(loan);
            System.out.println("Loan updated successfully");
        } catch (SQLException e) {
            throw new ServiceException("Error updating loan", e);
        }
    }

    public void deleteLoan(int loanId) {
        try {
            loanDAO.deleteLoan(loanId);
            System.out.println("Loan deleted successfully");
        } catch (SQLException e) {
            throw new ServiceException("Error deleting loan", e);
        }
    }

    public List<Loan> getAllLoans() {
        try {
            return loanDAO.getAllLoans();
        } catch (SQLException e) {
            throw new ServiceException("Error getting all loans", e);
        }
    }

    public Loan getLoanById(int loanId) {
        try {
            return loanDAO.getLoanById(loanId);
        } catch (SQLException e) {
            throw new ServiceException("Error getting loan by id", e);
        }
    }

    public List<Loan> getLoansByUserId(int userId) {
        try {
            return loanDAO.getLoansByUserId(userId);
        } catch (SQLException e) {
            throw new ServiceException("Error getting loans by user id", e);
        }
    }

    public Book getBookFromLoan(Loan loan) {
        return bookService.getBookByIsbn(loan.getBookIsbn());
    }

    public User getUserFromLoan(Loan loan) {
        return userService.getUserById(loan.getUserId());
    }
}
