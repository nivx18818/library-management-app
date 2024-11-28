package app.libmgmt.view.controller.admin;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import app.libmgmt.model.Book;
import app.libmgmt.model.Loan;

import app.libmgmt.service.LoanService;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdminBorrowedBookViewDialogController {

    private static AdminBorrowedBookViewDialogController controller;
    private final LoanService loanService = new LoanService();
    private int totalBook;
    private int totalLoan;

    @FXML
    private Pane closePane;
    @FXML
    private Label closeLabel;
    @FXML
    private Label lblId;
    @FXML
    private Label lblTotalBooks;
    @FXML
    private VBox vBox;
    @FXML
    private JFXButton closeButton;

    @FXML
    public void initialize() {
        System.out.println("AdminBorrowedBookViewDialogController initialized");
    }

    public AdminBorrowedBookViewDialogController() {
        controller = this;
    }

    public static AdminBorrowedBookViewDialogController getInstance() {
        if (controller == null) {
            controller = new AdminBorrowedBookViewDialogController();
        }
        return controller;
    }

    public List<Book> getBooksData(String id) {
        return loanService.getBookFromLoan(loanService.getIsbnByUserId(id));
    }

    /**
     * Loads data asynchronously to prevent blocking the main thread.
     */
    public void loadDataAsync(String id) {
        List<Book> data = getBooksData(id);
        List<Loan> loans = loanService.getLoansByUserId(id);
        this.totalBook = data.size();
        this.totalLoan = loans.size();
        setTotalBook();
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < data.size(); i++) {
                    loadBookData(data.get(i), loans.get(i));
                }
                return null;
            }

            @Override
            protected void failed() {
                System.err.println("Error loading data: " + getException().getMessage());
                throw new RuntimeException(getException());
            }
        };
        new Thread(preloadTask).start();
    }

    /**
     * Loads a single book's data into the VBox.
     * 
     * @param bookData Array containing [imageURL, title, author, date].
     */
    public void loadBookData(Book bookData, Loan loanData) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminBooksLayoutController.class.getResource(
                    "/fxml/admin/admin-borrowed-book-view-bar.fxml"));
            Pane scene = fxmlLoader.load();
            AdminBorrowedBookViewBarController controller = fxmlLoader.getController();

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dueDateString = outputFormat.format(loanData.getDueDate());
            controller.setData(bookData.getCoverUrl(), bookData.getTitle(), bookData.getAuthors().toString(), dueDateString);
            Platform.runLater(() -> {
                vBox.getChildren().add(scene);
                AnimationUtils.zoomIn(scene, 1.0);
            });
        } catch (Exception e) {
            System.err.println("Error loading book data: " + e.getMessage());
        }
    }

    @FXML
    private void btnCloseOnAction(ActionEvent event) {
        vBox.getChildren().clear();
        ChangeScene.closePopUp();
    }

    @FXML
    private void btnCloseOnMouseEntered(MouseEvent event) {
        closePane.setStyle("-fx-background-color: #d7d7d7; -fx-background-radius: 12px");
        closeLabel.setStyle("-fx-text-fill: #000000");
    }

    @FXML
    private void btnCloseOnMouseExited(MouseEvent event) {
        closePane.setStyle("-fx-background-color: #000000; -fx-background-radius: 12px");
        closeLabel.setStyle("-fx-text-fill: #ffffff");
    }

    /**
     * Sets the ID label with the given ID.
     * 
     * @param id The ID to set.
     */
    public void setId(String id) {
        lblId.setText(id);
    }

    public void setTotalBook() {
        lblTotalBooks.setText(String.valueOf(this.totalBook));
    }

    public void setTotalLoan() {
        lblTotalBooks.setText(String.valueOf(this.totalLoan + 1));
    }
}
