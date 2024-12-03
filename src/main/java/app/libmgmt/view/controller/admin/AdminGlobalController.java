package app.libmgmt.view.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.model.Book;
import app.libmgmt.model.User;
import app.libmgmt.model.ExternalBorrower;
import app.libmgmt.model.Loan;
import app.libmgmt.model.Student;
import app.libmgmt.service.BookService;
import app.libmgmt.service.LoanService;
import app.libmgmt.service.UserService;
import app.libmgmt.util.EnumUtils;

import java.util.ArrayList;
import java.util.List;

public class AdminGlobalController {

    private static AdminGlobalController controller;

    // Data loaded when starting the application
    private final ObservableList<Loan> borrowedBooksData = FXCollections.observableArrayList();
    private final List<String[]> adminsData = preLoadAdminData();

    // Real time load data
    private final ObservableList<Student> studentsData = FXCollections.observableArrayList();
    private final ObservableList<ExternalBorrower> externalBorrowersData = FXCollections.observableArrayList();
    private final ObservableList<Book> observableBooksData = FXCollections.observableArrayList();

    private final BookService bookService = new BookService();
    private final UserService userService = new UserService();
    private final LoanService loanService = new LoanService();

    // FXML UI components
    @FXML
    private Pane pagingPane;
    @FXML
    private StackPane stackPaneContainer;
    @FXML
    private Pane backgroundPane;
    @FXML
    private HBox globalFormContainer;

    // Constructor and Singleton Pattern
    public AdminGlobalController() {
        controller = this;
    }

    public static AdminGlobalController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        System.out.println("Admin Global Form initialized");
        AnimationUtils.fadeInRight(pagingPane, 1);
    }

    // Data Pre-loading Methods
    public List<Loan> preLoadBorrowedBooksData() {
        loanService.markOverdueLoans();
        return loanService.getAllLoans();
    }

    public List<Loan> preLoadOverDueLoans() {
        return loanService.getOverdueLoans();
    }

    public List<Book> preLoadBooksData() {
        return bookService.getAllBooks();
    }

    public List<Student> preLoadStudentsData() {
        return userService.getAllStudents();
    }

    public List<ExternalBorrower> preLoadExternalBorrowersData() {
        return userService.getAllExternalBorrowers();
    }

    private List<String[]> preLoadAdminData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] { "H D Thịnh", "23020708@vnu.edu.vn" });
        data.add(new String[] { "N X Vinh", "23020101@vnu.edu.vn" });
        data.add(new String[] { "L M Tường", "23020102@vnu.edu.vn" });
        data.add(new String[] { "N Đ Nguyên", "23020103@vnu.edu.vn" });
        return data;
    }

    // CRUD Operations for Books
    public void insertBooksData(String[] bookData) {
        // data format: [id, coverURL, name, type, author, quantity, publisher,
        // publishedDate, webReaderUrl]
        Book book = new Book(bookData);
        getInstance().observableBooksData.add(book);
        System.out.println("Book data: " + book.getPublishedDate());
        System.out.println("Book data: " + bookData[0] + " " + bookData[1] + " " + bookData[2] + " " + bookData[3] + " "
                + bookData[4] + " " + bookData[5] + " " + bookData[6] + " " + bookData[7] + " " + bookData[8]);
        // Insert book data to database
        bookService.addBook(book);
    }

    public void updateBookData(String[] updatedData) {
        Book updatedBook = new Book(updatedData);
        bookService.updateBook(updatedBook);

        for (int i = 0; i < observableBooksData.size(); i++) {
            Book book = observableBooksData.get(i);

            if (book.getIsbn().equals(updatedBook.getIsbn())) {
                observableBooksData.set(i, updatedBook);
                return;
            }
        }
    }

    public void deleteBookDataByIsbn(String isbn) {
        observableBooksData.removeIf(book -> book.getIsbn().equals(isbn));
        bookService.deleteBookByIsbn(isbn);
    }

    // CRUD Operations for Users
    public void updateUserData(String[] updatedData, EnumUtils.UserType userType) {
        User updatedUser = userType == EnumUtils.UserType.STUDENT
                ? new Student(updatedData)
                : new ExternalBorrower(updatedData);

        userService.updateUser(updatedUser);
        System.out.println("Updated user: " + updatedUser.getName() + " " + updatedUser.getEmail() + " " + updatedUser.getUserId());
                
        if (userType == EnumUtils.UserType.STUDENT) {
            for (int i = 0; i < studentsData.size(); i++) {
                Student student = studentsData.get(i);

                if (student.getStudentId().equals(updatedUser.getUserId())) {
                    studentsData.set(i, (Student) updatedUser);
                    return;
                }
            }
        }

        for (int i = 0; i < externalBorrowersData.size(); i++) {
            ExternalBorrower externalBorrower = externalBorrowersData.get(i);

            if (externalBorrower.getUserId().equals(updatedUser.getUserId())) {
                externalBorrowersData.set(i, (ExternalBorrower) updatedUser);
                return;
            }
        }

    }

    public void deleteUserById(EnumUtils.PopupList popupType, String id) {
        switch (popupType) {
            case STUDENT_DELETE:
                studentsData.removeIf(student -> student.getUserId().equals(id));
                break;
            case GUEST_DELETE:
                externalBorrowersData.removeIf(externalBorrower -> externalBorrower.getUserId().equals(id));
            default:
                break;
        }
        for (int i = 0; i < borrowedBooksData.size(); i++) {
            Loan loan = borrowedBooksData.get(i);
            if (loan.getUserId().equals(id)) {
                borrowedBooksData.removeIf(loan1 -> loan1.getUserId().equals(id));
            }
        }
        loanService.deleteLoanByUserId(id);
        userService.deleteUserById(id);
    }

    // fetchBooksFromDatabase
    public List<Book> fetchBooksFromDatabase() {
        return bookService.getAllBooks();
    }

    // Getter and Setter Methods for Data
    public List<Loan> getBorrowedBooksData() {
        return borrowedBooksData;
    }

    public List<Loan> getOverDueLoans() {
        return preLoadOverDueLoans();
    }

    public List<String[]> getAdminData() {
        return adminsData;
    }

    public ObservableList<Book> getObservableBookData() {
        return observableBooksData;
    }

    public ObservableList<Student> getObservableStudentsData() {
        return studentsData;
    }

    public ObservableList<ExternalBorrower> getObservableExternalBorrowersData() {
        return externalBorrowersData;
    }

    public int getTotalUsersCount() {
        return userService.countUser();
    }

    public int getTotalBooksCount() {
        return bookService.countBook();
    }

    public int getTotalBorrowedBooks() {
        return loanService.countTotalBorrowedBooks();
    }

    public void setObservableBookData(List<Book> data) {
        observableBooksData.setAll(data);
    }

    public void setStudentsData(List<Student> data) {
        studentsData.setAll(data);
    }

    public void setExternalBorrowersData(List<ExternalBorrower> data) {
        externalBorrowersData.setAll(data);
    }

    public void setLoansData(List<Loan> data) {
        borrowedBooksData.setAll(data);
    }

    // Getter Methods for UI components
    public Pane getPagingPane() {
        return pagingPane;
    }

    public Pane getBackgroundPane() {
        return backgroundPane;
    }

    public HBox getGlobalFormContainer() {
        return globalFormContainer;
    }

    public StackPane getStackPaneContainer() {
        return stackPaneContainer;
    }

}
