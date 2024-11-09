package app.libmgmt.model;

import java.util.Date;

public class Loan {
    private int loanId;
    private Date borrowedDate;
    private Date returnedDate;
    private Book book;
    private User user;
    private String status;
    private final long twoWeeksInMillis = 1209600000 ; // 14L * 24 * 60 * 60 * 1000

    public Loan(int loanId, Date borrowedDate, Date returnedDate, Book book, User user) {
        this.loanId = loanId;
        this.borrowedDate = borrowedDate;
        this.returnedDate = returnedDate;
        this.book = book;
        this.user = user;
        this.status = "BORROWED";
    }

    public int getId() {
        return loanId;
    }

    public String getStatus() {
        if (status.equals("BORROWED")
                && new Date().getTime() - borrowedDate.getTime() > twoWeeksInMillis) {
            status = "OVERDUE";
        }

        return status;
    }

    public void markAsReturned() {
        this.status = "RETURNED";
        this.returnedDate = new Date();
    }

    public Date getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(Date borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate) {
        this.returnedDate = returnedDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
