package app.libmgmt.view.controller;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class EmptyDataNotificationDialogController {

    private static EmptyDataNotificationDialogController controller;

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private Label closeLabel;

    @FXML
    private Pane closePane;

    @FXML
    private ImageView imgClose;

    @FXML
    private JFXButton closeButton;

    @FXML
    private Label notificationLabel;

    public EmptyDataNotificationDialogController() {
        controller = this;
    }

    public static EmptyDataNotificationDialogController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);

        closeButton.setOnMouseEntered(event -> {
            closePane.setStyle(
                    "-fx-background-color: #f2f2f2; -fx-background-radius: 10px; border-radius: 10px; -fx-border-radius: 10px; -fx-border-color: black; -fx-border-width: 1.2px;");
        });

        closeButton.setOnMouseExited(event -> {
            closePane.setStyle("-fx-background-color: #d7d7d7; -fx-background-radius: 10px;");
        });
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    public void setNotificationLabel(String message) {
        notificationLabel.setText(message);
    }

}
