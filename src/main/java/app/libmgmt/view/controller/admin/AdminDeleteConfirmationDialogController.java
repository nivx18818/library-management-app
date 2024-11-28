package app.libmgmt.view.controller.admin;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;

public class AdminDeleteConfirmationDialogController {

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private JFXButton deleteButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblConfirm;

    @FXML
    private Pane cancelPane;

    @FXML
    private Label notificationLabel;

    private String id;
    private EnumUtils.PopupList popupType;

    @FXML
    public void initialize() {
        System.out.println("Admin Delete Confirmation Dialog initialized");
        setupHoverEffects();
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        closeDialog();
    }

    @FXML
    void deleteButtonOnAction(ActionEvent event) {
        startDeleteProcess();
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        closeDialog();
    }

    @FXML
    void cancelButtonOnMouseEntered() {
        changeCancelPaneStyle("#d7d7d7");
    }

    @FXML
    void cancelButtonOnMouseExited() {
        changeCancelPaneStyle("#fff");
    }

    public void setId(String id, EnumUtils.PopupList popupType) {
        this.id = id;
        this.popupType = popupType;
        setupNotificationText();
    }

    /**
     * Sets up hover effects for interactive buttons.
     */
    private void setupHoverEffects() {
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    /**
     * Closes the dialog.
     */
    private void closeDialog() {
        ChangeScene.closePopUp();
    }

    /**
     * Changes the background color of the cancel pane based on hover state.
     */
    private void changeCancelPaneStyle(String color) {
        cancelPane.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10");
    }

    /**
     * Configures the notification text based on the popup type.
     */
    private void setupNotificationText() {
        if (popupType == EnumUtils.PopupList.BOOK_DELETE) {
            notificationLabel.setText("Are you sure you want to delete this book?");
        } else if (popupType == EnumUtils.PopupList.STUDENT_DELETE || popupType == EnumUtils.PopupList.GUEST_DELETE) {
            notificationLabel.setText("Are you sure you want to delete this user?");
        }
    }

    /**
     * Handles the deletion process.
     */
    private void startDeleteProcess() {
        lblConfirm.setText("Deleting...");
        disableButtons(true);

        try {
            if (popupType == EnumUtils.PopupList.BOOK_DELETE) {
                AdminGlobalController.getInstance().deleteBookDataByIsbn(id);
            } else if (popupType == EnumUtils.PopupList.STUDENT_DELETE || popupType == EnumUtils.PopupList.GUEST_DELETE) {
                AdminGlobalController.getInstance().deleteUserById(popupType, id);
            }

            closeDialogAfterDelay();
        } catch (Exception e) {
            notificationLabel.setText("Error deleting data!");
            notificationLabel.setStyle("-fx-text-fill: red;");
            lblConfirm.setText("Error!");
            disableButtons(false);
        }
    }

    /**
     * Disables or enables delete and cancel buttons.
     */
    private void disableButtons(boolean disable) {
        deleteButton.setDisable(disable);
        cancelButton.setDisable(disable);
    }

    /**
     * Closes the dialog after a short delay.
     */
    private void closeDialogAfterDelay() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.4), e -> {
            notificationLabel.setText("Deleted successfully!");
            lblConfirm.setText("Deleted!");
            notificationLabel.setStyle("-fx-text-fill: green;");
        }));
        Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(1.4), e -> {
            closeDialog();
        }));
        timeline.play();
        timeline2.play();
    }
}
