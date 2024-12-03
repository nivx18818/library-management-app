package app.libmgmt.model;

import java.util.Date;

import app.libmgmt.service.BookService;
import app.libmgmt.service.UserService;
import java.util.Objects;

public class Loan {
    private int loanId;
    private String userId;
    private String userName;
    private int amount;
    private Date borrowedDate;
    private Date dueDate;
    private Date returnedDate;
    private String isbn;
    private String status;
    private static final long TWO_WEEKS_IN_MILLIS = 1209600000; // 14L * 24 * 60 * 60 * 1000

    public Loan(String userId, String isbn, int amount, Date borrowedDate, String status) {
        this.userId = userId;
        this.userName = fetchUserNameFromUserId(userId);
        this.isbn = isbn;
        this.amount = amount;
        this.borrowedDate = borrowedDate;
        this.dueDate = new Date(borrowedDate.getTime() + TWO_WEEKS_IN_MILLIS);
        this.status = status;
    }

    public Loan(int loanId, String userId, String isbn, int amount, Date borrowedDate, String status) {
        this.loanId = loanId;
        this.userId = userId;
        this.userName = fetchUserNameFromUserId(userId);
        this.isbn = isbn;
        this.amount = amount;
        this.borrowedDate = borrowedDate;
        this.dueDate = new Date(borrowedDate.getTime() + TWO_WEEKS_IN_MILLIS);
        this.status = status;
    }

    // Getters và Setters
    public int getLoanId() {
        return loanId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(Date borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate) {
        this.returnedDate = returnedDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getStatus() {
        updateStatus();
        return status;
    }

    private void updateStatus() {
        if ("BORROWED".equals(status) && new Date().after(dueDate)) {
            status = "OVERDUE";
        }
    }

    public void markAsReturned() {
        this.status = "RETURNED";
        this.returnedDate = new Date();
    }

    private String fetchUserNameFromUserId(String userId) {
        UserService userService = new UserService();
        return userService.fetchUserNameFromUserId(userId);
    }

    public Book getBook() {
        BookService bookService = new BookService();
        return bookService.getBookByIsbn(isbn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Loan loan = (Loan) o;
        return loanId == loan.loanId &&
                userId == loan.userId &&
                Objects.equals(borrowedDate, loan.borrowedDate) &&
                Objects.equals(returnedDate, loan.returnedDate) &&
                Objects.equals(isbn, loan.isbn) &&
                Objects.equals(status, loan.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId, borrowedDate, returnedDate, isbn, userId, status);
    }
}
