package app.libmgmt.view.controller.user;

import java.util.ArrayList;
import java.util.List;

import app.libmgmt.model.Loan;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.DateUtils;
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
    private final ObservableList<String[]> observableBooksData;

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
        List<Loan> data = new ArrayList<>();
        // Loan(int loanId, String userId, String isbn, int amount, Date borrowedDate, Date dueDate, String status) 
        data.add(new Loan(1, "23020708", "A1", 1, DateUtils.parseStringToDate("09/11/2024"), DateUtils.parseStringToDate("22/11/2024"), "BORROWED"));
        data.add(new Loan(2, "23020708", "B2", 1, DateUtils.parseStringToDate("11/11/2024"), DateUtils.parseStringToDate("24/11/2024"), "BORROWED"));
        data.add(new Loan(3, "23020708", "C3", 1, DateUtils.parseStringToDate("13/11/2024"), DateUtils.parseStringToDate("26/11/2024"),"BORROWED"));

        return data;
    }

    private List<Loan> setOriginalReturnedBooksData() {
        List<Loan> data = new ArrayList<>();
        for (Loan d : borrowedBooksData) {
            if (d.getStatus().equals("RETURNED")) {
                data.add(d);
            }
        }
        return data;
    }

    public List<String[]> preLoadBooksData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] { "A1", "https://marketplace.canva" +
                ".com/EAFaQMYuZbo/1/0/1003w/canva-brown-rusty-mystery-novel-book-cover-hG1QhA7BiBU.jpg",
                "The Great Gatsby", "Education", "F. Scott Fitzgerald", "3", "NXB Trẻ", "13/08/2024" });
        data.add(new String[] { "B2", "https://encrypted-tbn0.gstatic" +
                ".com/images?q=tbn:ANd9GcS66i6hTBkniGtDdwxyi4hA3PFm2mJ0GUIDxw&s", "To Kill a Mockingbird", "Education",
                "Harper Lee", "4", "NXB Trẻ", "13/08/2024" });
        data.add(new String[] { "C3", "https://thuviensach.vn/img/news/2022/09/larger/1011-1984-1" +
                ".jpg?v=8882", "1984", "Education", "George Orwell", "0", "NXB Trẻ", "13/08/2024" });
        data.add(new String[] { "D4", "https://play-lh.googleusercontent" +
                ".com/f1jkKDk5wKz1CZMyNjOR7klTu-ORIZs9sBMWSOVtd09GE6ulfiW5M4FmWrS54CZmCDiZ", "Pride & Prejudice",
                "Education", "J.D. Salinger", "12", "NXB Trẻ", "13/08/2024" });
        data.add(new String[] { "E5", "https://encrypted-tbn0.gstatic" +
                ".com/images?q=tbn:ANd9GcRYqVdifswPLs8J53knQLpfO0dYIVMq4Mu14w&s", "Sherlock Holmes", "Detective",
                "Arthur Conan Doyle", "32", "NXB Trẻ", "13/08/2024" });
        data.add(new String[] { "F6", "https://encrypted-tbn0.gstatic" +
                ".com/images?q=tbn:ANd9GcQapwj529X6xqxmWUlrZAbQLhi-jEpU1-gx8A&s", "Dracula", "Horror", "Bram Stoker",
                "0", "NXB Trẻ", "13/08/2024" });
        data.add(new String[] { "G7", "https://www.thejapanshop" +
                ".com/cdn/shop/products/new_doc_91_1_1280x.jpg?v=1571438916", "Doraemon", "Comic", "Fujko F Fujio", "0",
                "NXB Trẻ", "13/08/2024" });
        return data;
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

    public String[] getBookDataById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        return observableBooksData.stream()
                .filter(data -> data[0].equals(id))
                .findFirst()
                .map(String[]::clone)
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

    public ObservableList<String[]> getObservableBooksData() {
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
