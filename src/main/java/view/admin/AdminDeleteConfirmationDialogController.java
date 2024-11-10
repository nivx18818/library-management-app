package view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import util.AnimationUtils;
import util.ChangeScene;
import util.EnumUtils;

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

    private String id;

    private EnumUtils.PopupList popupList;

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
        if (popupList == EnumUtils.PopupList.BOOK_DELETE) {
            deleteBook();
        } else if (popupList == EnumUtils.PopupList.USER_DELETE) {
            deleteUser();
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
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

    private void deleteBook() {
        AdminGlobalFormController.getInstance().deleteBookDataById(id);
        AdminBooksLayoutController.getInstance().refreshBooksList();
    }

    private void deleteUser() {
        AdminGlobalFormController.getInstance().deleteUserById(id);
        AdminUsersLayoutController.getInstance().deleteUserDataById(id);
        AdminUsersLayoutController.getInstance().refreshUsersList();
    }

    public void setId(String id, EnumUtils.PopupList popupList) {
        this.id = id;
        this.popupList = popupList;
    }
}
