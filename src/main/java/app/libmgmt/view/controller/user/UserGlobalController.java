package app.libmgmt.view.controller.user;

import java.util.List;

import app.libmgmt.model.Loan;
import app.libmgmt.model.Book;
import app.libmgmt.service.LoanService;
import app.libmgmt.service.BookService;
import app.libmgmt.util.AnimationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class UserGlobalController {

    private static volatile UserGlobalController controller;

    // Collections for data management
    private final ObservableList<Loan> borrowedBooksData;
    private final ObservableList<Loan> returnedBooksData;
    private final ObservableList<Book> observableBooksData;

    private final LoanService loanService = new LoanService();
    private final BookService bookService = new BookService();

    // FXML injected components
    @FXML
    private Pane backgroundPane;
    @FXML
    private HBox globalFormContainer;
    @FXML
    private Pane pagingPane;
    @FXML
    private StackPane stackPaneContainer;

    // Constructor and Singleton Pattern
    public UserGlobalController() {

        // Initialize collections
        borrowedBooksData = FXCollections.observableArrayList(preLoadLoansData());
        returnedBooksData = FXCollections.observableArrayList(setOriginalReturnedBooksData());
        observableBooksData = FXCollections.observableArrayList(preLoadBooksData());
    }

    public static UserGlobalController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        controller = this;
        AnimationUtils.fadeInRight(pagingPane, 1);
    }

    // Data Pre-loading Methods
    private List<Loan> preLoadLoansData() {
        // test data
        return loanService.getAllLoans();
    }

    private List<Loan> setOriginalReturnedBooksData() {
        return loanService.getOverdueLoans();
    }

    public List<Book> preLoadBooksData() {
        return bookService.getAllBooks();
    }

    // CRUD Methods
    public void addBorrowedBook(List<Loan> newBorrowedBooksList) {
        if (newBorrowedBooksList == null || newBorrowedBooksList.isEmpty()) {
            return;
        }

        for (int i = 0; i < newBorrowedBooksList.size(); i++) {
            borrowedBooksData.add(newBorrowedBooksList.get(i));
        }
    }

    public void addReturnedBook(int loanId) {
        if (loanId <= 0 || loanId > borrowedBooksData.size()) {
            System.out.println("Invalid loan id");
            return;
        }

        for (int i = 0; i < borrowedBooksData.size(); i++) {
            Loan data = borrowedBooksData.get(i);
            if (data.getLoanId() == loanId && (data.getStatus().equals("BORROWED") || data.getStatus().equals("OVERDUE"))) {
                data.markAsReturned();
                returnedBooksData.add(data);
                break;
            }
        }
    }

    public Book getBookDataById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        return observableBooksData.stream()
                .filter(data -> data.getIsbn().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean isBookReturned(int loanId, String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        for (int i = 0; i < borrowedBooksData.size(); i++) {
            Loan data = borrowedBooksData.get(i);
            if (data.getLoanId() == loanId && data.getIsbn().equals(isbn) && data.getStatus().equals("RETURNED")) {
                return true;
            }
        }

        return false;
    }

    public boolean isBookBorrowed(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }

        for (int i = 0; i < borrowedBooksData.size(); i++) {
            Loan data = borrowedBooksData.get(i);
            if (data.getIsbn().equals(isbn) && (borrowedBooksData.get(i).getStatus().equals("BORROWED") || 
            borrowedBooksData.get(i).getStatus().equals("OVERDUE"))) {
                return true;
            }
        }

        return false;
    }

    // Getter Methods for Data
    public ObservableList<Loan> getBorrowedBooksData() {
        return borrowedBooksData;
    }

    public ObservableList<Book> getObservableBooksData() {
        return observableBooksData;
    }

    public ObservableList<Loan> getReturnedBooksData() {
        return returnedBooksData;
    }

    // Getter Methods for UI components
    public Pane getPagingPane() {
        return pagingPane;
    }

    public Pane getBackgroundPane() {
        return backgroundPane;
    }

    public StackPane getStackPaneContainer() {
        return stackPaneContainer;
    }

}
