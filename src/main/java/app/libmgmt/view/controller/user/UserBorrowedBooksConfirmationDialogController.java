package app.libmgmt.view.controller.user;

import java.util.List;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.DateTimeUtils;
import app.libmgmt.view.controller.admin.AdminBooksLayoutController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class UserBorrowedBooksConfirmationDialogController {

    private static UserBorrowedBooksConfirmationDialogController controller;

    @FXML
    private Label borrowedDateLabel;

    @FXML
    private JFXButton closeButton;

    @FXML
    private Label closeLabel;

    @FXML
    private Pane closePane;

    @FXML
    private JFXButton confirmButton;

    @FXML
    private Label confirmLabel;

    @FXML
    private Pane confirmPane;

    @FXML
    private Label totalBorrowedBooksLabel;

    @FXML
    private VBox vBoxSelectedBooksList;

    private List<String[]> selectedBooksList;

    public UserBorrowedBooksConfirmationDialogController() {
        controller = this;
    }

    public static UserBorrowedBooksConfirmationDialogController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        System.out.println("User Borrowed Books Confirmation Dialog initialized");
        selectedBooksList = UserBooksLayoutController.getInstance().getSelectedBooksList();
        borrowedDateLabel.setText(getToday());
        preloadData();
        totalBorrowedBooksLabel.setText(selectedBooksList.size() + (selectedBooksList.size() > 1 ? " Books" : " Book"));
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        vBoxSelectedBooksList.getChildren().clear();
        ChangeScene.closePopUp();
    }

    @FXML
    void btnConfirmOnAction(ActionEvent event) {
        // TODO: Implement confirmation logic
    }

    @FXML
    void btnOnMouseEntered(MouseEvent event) {
        if (event.getSource() == closeButton) {
            closePane.setStyle(
                "-fx-background-color: #d7d7d7; -fx-background-radius: 10;");
        } else if (event.getSource() == confirmButton) {
            confirmPane.setStyle(
                "-fx-background-color: #F2F2F2; -fx-background-radius: 10; -fx-border-color: #000; -fx-border-radius: 10; -fx-border-width: 1.2;");
            confirmLabel.setStyle("-fx-text-fill: #000;");
        }
    }

    @FXML
    void btnOnMouseExited(MouseEvent event) {
        if (event.getSource() == closeButton) {
            closePane.setStyle(
                "-fx-background-color: #fff; -fx-background-radius: 10;");
        } else if (event.getSource() == confirmButton) {
            confirmPane.setStyle(
                "-fx-background-color: #000; -fx-background-radius: 10;");
            confirmLabel.setStyle("-fx-text-fill: #fff;");
        }

    }

    // Loads data asynchronously to prevent blocking the main thread.
    private void preloadData() {
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int orderNumber = 1;
                for (String[] d : selectedBooksList) {
                    loadBookData(d, orderNumber++);
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
    private void loadBookData(String[] bookData, int orderNumber) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminBooksLayoutController.class.getResource(
                    "/fxml/user/user-borrowed-book-bar.fxml"));
            Pane scene = fxmlLoader.load();
            UserBorrowedBookBarController controller = fxmlLoader.getController();
            controller.setOrderNumber(orderNumber);
            controller.setData(bookData);
            Platform.runLater(() -> {
                vBoxSelectedBooksList.getChildren().add(scene);
                AnimationUtils.zoomIn(scene, 1.0);
            });
        } catch (Exception e) {
            System.err.println("Error loading book data: " + e.getMessage());
        }
    }

    public String getToday() {
        return DateTimeUtils.convertDateToString(DateTimeUtils.currentLocalTime);
    }

}
