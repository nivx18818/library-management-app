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
    private Pane closePane;

    @FXML
    private JFXButton confirmButton;

    @FXML
    private Pane confirmPane;

    @FXML
    private Pane container;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblConfirm;

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
    void confirmButtonOnAction(ActionEvent event) {
        lblConfirm.setText("Deleting...");
        confirmButton.setDisable(true);
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
