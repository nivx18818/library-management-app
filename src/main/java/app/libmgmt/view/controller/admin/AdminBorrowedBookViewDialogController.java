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
import app.libmgmt.service.LoanService;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;

import java.util.List;

public class AdminBorrowedBookViewDialogController {

    private static AdminBorrowedBookViewDialogController controller;
    private List<Book> data;
    private final LoanService loanService = new LoanService();
    private int totalBook;

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
        // data = setExampleData("sd"); // Initialize example data
        // loadDataAsync(); // Load data asynchronously
    }

    // public static AdminBookViewDialogController getInstance() {
    //     return controller;
    // }
    public AdminBorrowedBookViewDialogController() {
        controller = this;
    }

    public static AdminBorrowedBookViewDialogController getInstance() {
        return controller;
    }

    public List<Book> getBooksData(String id) {
        return loanService.getBookFromLoan(loanService.getIsbnByUserId(id));
    }

    /**
     * Loads data asynchronously to prevent blocking the main thread.
     */
    public void loadDataAsync(String id) {
        data = getBooksData(id);
        this.totalBook = data.size();
        setTotalBook();
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (Book d : data) {
                    loadBookData(d);
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
    private void loadBookData(Book bookData) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminBooksLayoutController.class.getResource(
                    "/fxml/admin/admin-borrowed-book-view-bar.fxml"));
            Pane scene = fxmlLoader.load();
            AdminBorrowedBookViewBarController controller = fxmlLoader.getController();

            controller.setData(bookData.getCoverUrl(), bookData.getTitle(), bookData.getAuthors().toString(), "2021-01-01");
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
}
