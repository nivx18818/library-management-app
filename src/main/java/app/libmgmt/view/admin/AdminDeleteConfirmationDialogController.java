package app.libmgmt.view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
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
    private Label cancelLabel;

    @FXML
    private Pane cancelPane;

    @FXML
    private Label notificationLabel;

    private String id;

    private EnumUtils.PopupList popupType;

    public void initialize() {
        System.out.println("Admin Delete Confirmation Dialog initialized");
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    @FXML
    void deleteButtonOnAction(ActionEvent event) {
        lblConfirm.setText("Deleting...");
        deleteButton.setDisable(true);
        cancelButton.setDisable(true);
        if (popupType == EnumUtils.PopupList.BOOK_DELETE) {
            notificationLabel.setText("Are you sure you want to delete this book?");
            AdminGlobalController.getInstance().deleteBookDataById(id);
        } else if (popupType == EnumUtils.PopupList.STUDENT_DELETE || popupType == EnumUtils.PopupList.GUEST_DELETE) {
            notificationLabel.setText("Are you sure you want to delete this user?");
            AdminGlobalController.getInstance().deleteUserById(popupType, id);
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.48), e -> {
            ChangeScene.closePopUp();
        }));
        timeline.play();
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void cancelButtonOnMouseEntered() {
        cancelPane.setStyle("-fx-background-color: #d7d7d7; -fx-background-radius: 10");
    }

    @FXML
    void cancelButtonOnMouseExited() {
        cancelPane.setStyle("-fx-background-color: #fff; -fx-background-radius: 10");
    }

    public void setId(String id, EnumUtils.PopupList popupList) {
        this.id = id;
        this.popupType = popupList;
    }
}
