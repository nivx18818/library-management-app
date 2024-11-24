package app.libmgmt.view.controller.user;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class UserReturnBookConfirmationDialogController {

    @FXML
    private JFXButton cancelButton;

    @FXML
    private Label cancelLabel;

    @FXML
    private Pane cancelPane;

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private JFXButton confirmButton;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblConfirm;

    @FXML
    private Label notificationLabel;

    @FXML
    private Pane confirmPane;

    private int loanId;

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    @FXML
    public void initialize() {
        System.out.println("User Return Confirmation Dialog initialized");
        setupHoverEffects();
    }

    @FXML
    void btnConfirmOnAction(ActionEvent event) {
        startReturnBookProcess();
    }

    private void startReturnBookProcess() {
        lblConfirm.setText("Returning...");
        disableButtons(true);
        closeDialogAfterDelay();
        UserGlobalController.getInstance().addReturnedBook(loanId);
    }

    /**
     * Disables or enables delete and cancel buttons.
     */
    private void disableButtons(boolean disable) {
        confirmButton.setDisable(disable);
        cancelButton.setDisable(disable);
    }

    /**
     * Closes the dialog after a short delay.
     */
    private void closeDialogAfterDelay() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            notificationLabel.setText("Return book successfully! Returning to the main page...");
            lblConfirm.setText("Returned!");
        }));
        Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
            ChangeScene.closePopUp();
        }));
        timeline.play();
        timeline2.play();
    }

    @FXML
    void btnConfirmOnMouseEntered(MouseEvent event) {
        confirmPane.setStyle("-fx-background-color: #f2f2f2; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: black");
        lblConfirm.setStyle("-fx-text-fill: #000");
    }

    @FXML
    void btnConfirmOnMouseExited(MouseEvent event) {
        confirmPane.setStyle("-fx-background-color: black; -fx-background-radius: 10");
        lblConfirm.setStyle("-fx-text-fill: #fff");
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void cancelButtonOnMouseEntered(MouseEvent event) {
        changeCancelPaneStyle("#d7d7d7");
    }

    @FXML
    void cancelButtonOnMouseExited(MouseEvent event) {
        changeCancelPaneStyle("#fff");
    }

    /**
     * Changes the background color of the cancel pane based on hover state.
     */
    private void changeCancelPaneStyle(String color) {
        cancelPane.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10");
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    /**
     * Sets up hover effects for interactive buttons.
     */
    private void setupHoverEffects() {
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }
}
