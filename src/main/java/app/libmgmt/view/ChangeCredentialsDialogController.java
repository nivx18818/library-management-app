package app.libmgmt.view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;

public class ChangeCredentialsDialogController {

    @FXML
    private TextField cfNewPasswordField;

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private TextField curPasswordField;

    @FXML
    private ImageView imgClose;

    @FXML
    private TextField newPasswordField;

    @FXML
    private Label notificationLabel;

    @FXML
    private Pane container;

    public void initialize() {
        System.out.println("ChangeCredentialsDialogController initialized");

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);

        container.setOnMouseClicked(
                event -> {
                    container.requestFocus();
                }
        );
    }

    @FXML
    void addButtonOnAction(ActionEvent event) {
        handleCredentialsChange("password");
    }

    public void handleCredentialsChange(String correctCurrentPassword) {
        String curPassword = curPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String cfNewPassword = cfNewPasswordField.getText();
        if (curPassword.isEmpty() || newPassword.isEmpty() || cfNewPassword.isEmpty()) {
            notificationLabel.setText("Please fill all fields");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        } else if (!curPassword.equals(correctCurrentPassword)) {
            notificationLabel.setText("Current password is incorrect");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        } else if (!newPassword.equals(cfNewPassword)) {
            notificationLabel.setText("New password do not match");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        } else {
            notificationLabel.setText("Credentials changed successfully");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "green");
            setNewPassword(newPassword);
        }
    }

    private void setNewPassword(String newPassword) {
        //TODO: Implement this method
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        curPasswordField.clear();
        newPasswordField.clear();
        cfNewPasswordField.clear();
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

}
