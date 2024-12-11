package app.libmgmt.view.controller.admin;

import java.io.IOException;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.model.Loan;
import app.libmgmt.service.LoanService;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AdminAllBorrowedBookViewDialogController {

    private static AdminAllBorrowedBookViewDialogController controller;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private Label closeLabel;

    @FXML
    private Pane closePane;

    @FXML
    private Pane closePane2;

    @FXML
    private HBox hBoxReturn;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblTotalBorrowedBooks;

    @FXML
    private Label lblUserId;

    @FXML
    private JFXButton returnButton;

    @FXML
    private ImageView returnImage;

    @FXML
    private Label returnLabel;

    @FXML
    private VBox vBox;

    public AdminAllBorrowedBookViewDialogController() {
        controller = this;
    }

    public static AdminAllBorrowedBookViewDialogController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        vBox.getChildren().clear();
        closeButton.setDisable(true);
        closeLabel.setText("Closing...");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), e -> {
            closeLabel.setText("Closed!");
            ChangeScene.closePopUp();
        }));
        timeline.play();
    }

    public void setData(String userId) {
        lblUserId.setText(userId);
    }

    public void preLoadData() {
        String userId = lblUserId.getText();
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                LoanService loanService = new LoanService();
                List<Loan> loans = loanService.getLoansByUserId(userId);
                for (Loan loan : loans) {
                    loadBookBar(loan);
                }

                return null;
            }

            @Override
            protected void failed() {
                System.out.println("Error during data table loading: " + getException().getMessage());
            }
        };

        preloadTask.setOnSucceeded(event -> {
            lblTotalBorrowedBooks.setText(String.valueOf(vBox.getChildren().size()) + " borrowed books");
        });

        new Thread(preloadTask).start();
    }

    private void loadBookBar(Loan d) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/admin/admin-all-borrowed-book-bar.fxml"));
            Pane scene = fxmlLoader.load();
            AdminAllBorrowedBookBarController controller = fxmlLoader.getController();

            // String name, String id, int amount, String dueDate, String borrowedDate
            controller.setData(d);

            // Add to VBox and animate
            Platform.runLater(() -> {
                vBox.getChildren().add(scene);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
