package app.libmgmt.view.controller;

import animatefx.animation.ZoomOut;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.view.controller.admin.AdminGlobalController;
import app.libmgmt.view.controller.admin.AdminNavigationController;
import app.libmgmt.view.controller.user.UserGlobalController;
import app.libmgmt.view.controller.user.UserNavigationController;

import java.io.IOException;

public class LogoutDialogController {

    private static LogoutDialogController controller;

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private Pane closePane;

    @FXML
    private Pane container;

    @FXML
    private ImageView imgClose;

    @FXML
    private JFXButton confirmButton;

    @FXML
    private Pane confirmPane;

    @FXML
    private Label confirmLabel;

    private EnumUtils.UserType userType;

    // Constructor and Singleton Pattern
    public LogoutDialogController() {
        controller = this;
    }

    public static LogoutDialogController getInstance() {
        return controller;
    }

    public void initialize() {
        System.out.println("Logout Dialog Controller initialized");
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void confirmButtonOnAction(ActionEvent event) throws IOException {
        if (AdminGlobalController.getInstance() != null) {
            AdminGlobalController.getInstance().getGlobalFormContainer().getChildren().clear();
            AdminNavigationController.getInstance().setLastButtonClicked(EnumUtils.NavigationButton.DASHBOARD);
            AdminNavigationController.getInstance().setUploadedData(false, false);
        } else {
            UserGlobalController.getInstance().getGlobalFormContainer().getChildren().clear();
            UserNavigationController.getInstance().setLastButtonClicked(EnumUtils.NavigationButton.DASHBOARD);
            UserNavigationController.getInstance().setUploadedData(false);
        }
        ChangeScene.closePopUp();
        backToLoginForm();
    }

    @FXML
    void confirmButtonOnMouseEntered() {
        confirmPane.setStyle("-fx-background-color: #d7d7d7; -fx-background-radius: 10");
        confirmLabel.setStyle("-fx-text-fill: #000000");
    }

    @FXML
    void confirmButtonOnMouseExited() {
        confirmPane.setStyle("-fx-background-color: #000; -fx-background-radius: 10");
        confirmLabel.setStyle("-fx-text-fill: #fff");
    }

    public void backToLoginForm() throws IOException {
        ZoomOut zo;
        if (AdminGlobalController.getInstance() != null) {
            zo = new ZoomOut(AdminGlobalController.getInstance().getGlobalFormContainer());
        } else {
            zo = new ZoomOut(UserGlobalController.getInstance().getGlobalFormContainer());
        }

        ChangeScene.navigateToScene("loading-form.fxml", userType);

        zo.setOnFinished(event -> {
            try {
                ChangeScene.changeInterfaceWindow(
                        (Stage) LoadingPageController.getInstance().getLoadingPane().getScene().getWindow(),
                        "/fxml/login-form.fxml", "Login Window");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        zo.play();
    }

    public void setUserType(EnumUtils.UserType userType) {
        this.userType = userType;
    }
}
