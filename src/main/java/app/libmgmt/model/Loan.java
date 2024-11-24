package app.libmgmt.model;

import java.util.Date;
import java.util.Objects;

import app.libmgmt.util.DateTimeUtils;

public class Loan {
    private int loanId;
    private Date borrowedDate;
    private Date returnedDate;
    private String isbn;
    private int userId;
    private String status;
    private final long twoWeeksInMillis = 1209600000; // 14L * 24 * 60 * 60 * 1000

    public Loan(int loanId, Date borrowedDate, Date returnedDate, String isbn, int userId,
            String status) {
        this.loanId = loanId;
        this.borrowedDate = borrowedDate;
        this.returnedDate = returnedDate;
        this.isbn = isbn;
        this.userId = userId;
        this.status = status;
    }

    public int getLoanId() {
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

    public String getBookIsbn() {
        return isbn;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Loan ID: " + loanId
                + ", ISBN: " + isbn
                + ", User ID: " + userId
                + ", Borrowed Date: " + DateTimeUtils.convertDateToString(borrowedDate)
                + ", Returned Date: " + DateTimeUtils.convertDateToString(returnedDate)
                + ", Status: " + getStatus();
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