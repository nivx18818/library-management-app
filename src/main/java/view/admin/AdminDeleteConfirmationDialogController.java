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
        AdminGlobalFormController.getInstance().deleteBookDataById(id);
        AdminBooksLayoutController.getInstance().refreshBooksList();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            ChangeScene.closePopUp();
        }));
        timeline.play();
    }

    public void setId(String id) {
        this.id = id;
    }

}
