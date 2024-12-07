package app.libmgmt.view.controller.user;

import java.text.SimpleDateFormat;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.model.Loan;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.view.controller.admin.AdminBooksLayoutController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class UserBorrowedBookViewDialogController {

    private static UserBorrowedBookViewDialogController controller;

    @FXML
    private JFXButton closeButton;

    @FXML
    private Label closeLabel;

    @FXML
    private Pane closePane;

    @FXML
    private Label lblBorrowedDate;

    @FXML
    private Label lblLoanId;

    @FXML
    private VBox vBox;

    public UserBorrowedBookViewDialogController() {
        controller = this;
    }

    public static UserBorrowedBookViewDialogController getInstance() {
        return controller;
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        vBox.getChildren().clear();
        closeButton.setDisable(true);
        closeLabel.setText("Closing...");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), e -> {
            closeLabel.setText("Closed!");
            ChangeScene.closePopUp();
        }));
        timeline.play();
    }

    // Loads data asynchronously to prevent blocking the main thread.
    public void loadDataAsync(Loan loan) {
        lblLoanId.setText(String.valueOf(loan.getLoanId()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String borrowedDateString = sdf.format(loan.getBorrowedDate());
        lblBorrowedDate.setText(borrowedDateString);
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Load book data from loan.
                // Format data: [isbn, name book, amount, due date]
                loadBookData(loan);
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

    private void loadBookData(Loan loan) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminBooksLayoutController.class.getResource(
                    "/fxml/user/user-borrowed-view-bar.fxml"));
            Pane scene = fxmlLoader.load();
            UserBorrowedBookViewBarController controller = fxmlLoader.getController();
            controller.setData(loan);
            scene.setUserData(controller);

            Platform.runLater(() -> {
                vBox.getChildren().add(scene);
                AnimationUtils.zoomIn(scene, 1.0);
            });
        } catch (Exception e) {
            System.err.println("Error loading book data: " + e.getMessage());
        }
    }
}
