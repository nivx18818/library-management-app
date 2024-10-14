package app.libmgmt.model;

import app.libmgmt.model.Book;
import app.libmgmt.model.Borrower;

import java.util.Date;

public class Loan {
    private int id;
    private String status;
    private Date borrowedDate;
    private Date returnedDate;
    private Book book;
    private Borrower borrower;

    public Loan() {
    }

    public Loan(int id, String status, Date borrowedDate, Date returnedDate, Book book, Borrower borrower) {
        this.id = id;
        this.status = status;
        this.borrowedDate = borrowedDate;
        this.returnedDate = returnedDate;
        this.book = book;
        this.borrower = borrower;
    }

    public String getStatus() {
        return status;
    }

    public void markAsReturned() {
        this.status = "RETURNED";
        this.returnedDate = new Date();
    }

    public boolean isOverdue() {
        // TODO: Implement this method
    }

    public Date getBorrowedDate() {
        return borrowedDate;
    }

    public Date getReturnedDate() {
        return returnedDate;
    }
}
