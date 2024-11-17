package app.libmgmt.view.admin;

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
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;

import java.util.List;

public class AdminBorrowedBookViewDialogController {

    private List<String[]> data;

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
        data = setExampleData(); // Initialize example data
        loadDataAsync(); // Load data asynchronously
    }

    private List<String[]> setExampleData() {
        return List.of(
                new String[] { "https://th.bing.com/th/id/OIP.aQ3e1NxnNQVFCXiJJesFZwDMEx?rs=1&pid=ImgDetMain",
                        "Book Title", "Author", "2021-01-01" },
                new String[] { "https://th.bing.com/th/id/OIP.aQ3e1NxnNQVFCXiJJesFZwDMEx?rs=1&pid=ImgDetMain",
                        "Book Title", "Author", "2021-01-01" },
                new String[] { "https://th.bing.com/th/id/OIP.aQ3e1NxnNQVFCXiJJesFZwDMEx?rs=1&pid=ImgDetMain",
                        "Book Title", "Author", "2021-01-01" }

        );
    }

    /**
     * Loads data asynchronously to prevent blocking the main thread.
     */
    private void loadDataAsync() {
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (String[] d : data) {
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
    private void loadBookData(String[] bookData) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminBooksLayoutController.class.getResource(
                    "/fxml/admin-borrowed-book-view-bar.fxml"));
            Pane scene = fxmlLoader.load();
            AdminBorrowedBookViewBarController controller = fxmlLoader.getController();
            controller.setData(bookData[0], bookData[1], bookData[2], bookData[3]);
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
}
