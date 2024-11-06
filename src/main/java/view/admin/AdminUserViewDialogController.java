package view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import util.AnimationUtils;
import util.ChangeScene;
import util.EnumUtils;

public class AdminUserViewDialogController {

    @FXML
    private JFXButton cancelButton;

    @FXML
    private Pane cancelPane;

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private Pane closePane;

    @FXML
    private Pane container;

    @FXML
    private Label emailLabel;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label idLabel;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblCancel;

    @FXML
    private Label majorOrPhoneLabel;

    @FXML
    private Label userTypeLabel;

    private static AdminUserViewDialogController controller;

    public AdminUserViewDialogController() {
        controller = this;
    }

    public static AdminUserViewDialogController getInstance() {
        return controller;
    }

    public void initialize() {
        System.out.println("AdminUserViewDialogController initialized");
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    public void setData(String[] data, EnumUtils.UserType userType) {
        idLabel.setText("ID : " + data[0]);
        fullNameLabel.setText("Full name : " + data[1]);
        majorOrPhoneLabel.setText((userType == EnumUtils.UserType.STUDENT) ?
                "Major : " + data[2] : "Phone number : " + data[2]);
        emailLabel.setText("Email : " + data[3]);
        userTypeLabel.setText((userType == EnumUtils.UserType.STUDENT) ? "Student" : "External " +
                "Borrower");
    }

}
