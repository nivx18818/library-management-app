package view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Animation;
import util.ChangeScene;

import java.awt.*;
import java.net.URI;

public class ForgotPasswordDialogController {

    @FXML
    private JFXButton facebookAdmin1Button;

    @FXML
    private JFXButton facebookAdmin2Button;

    @FXML
    private JFXButton facebookAdmin3Button;

    @FXML
    private JFXButton igAdmin1Button;

    @FXML
    private JFXButton igAdmin2Button;

    @FXML
    private JFXButton igAdmin3Button;

    @FXML
    private ImageView fbIcon1;

    @FXML
    private ImageView igIcon1;

    @FXML
    private ImageView fbIcon2;

    @FXML
    private ImageView igIcon2;

    @FXML
    private ImageView fbIcon3;

    @FXML
    private ImageView igIcon3;

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private ImageView imgClose;

    public void initialize() {
        System.out.println("Initialize Forgot Password Dialog");

        Animation.hoverCloseIcons(closeDialogButton, imgClose);
        changeHoverIcons();
    }

    public void changeHoverIcons() {
        Image hoverFbImage =
                new Image(getClass().getResource("/assets/icon/facebook2.png").toExternalForm());
        Image defaultFbImage =
                new Image(getClass().getResource("/assets/icon/facebook.png").toExternalForm());
        Image hoverIgImage =
                new Image(getClass().getResource("/assets/icon/instagram2.png").toExternalForm());
        Image defaultIgImage = new Image(getClass().getResource("/assets/icon/instagram.png").toExternalForm());
        facebookAdmin1Button.setOnMouseEntered(event -> fbIcon1.setImage(hoverFbImage));
        facebookAdmin1Button.setOnMouseExited(event -> fbIcon1.setImage(defaultFbImage));
        igAdmin1Button.setOnMouseEntered(event -> igIcon1.setImage(hoverIgImage));
        igAdmin1Button.setOnMouseExited(event -> igIcon1.setImage(defaultIgImage));
        facebookAdmin2Button.setOnMouseEntered(event -> fbIcon2.setImage(hoverFbImage));
        facebookAdmin2Button.setOnMouseExited(event -> fbIcon2.setImage(defaultFbImage));
        igAdmin2Button.setOnMouseEntered(event -> igIcon2.setImage(hoverIgImage));
        igAdmin2Button.setOnMouseExited(event -> igIcon2.setImage(defaultIgImage));
        facebookAdmin3Button.setOnMouseEntered(event -> fbIcon3.setImage(hoverFbImage));
        facebookAdmin3Button.setOnMouseExited(event -> fbIcon3.setImage(defaultFbImage));
        igAdmin3Button.setOnMouseEntered(event -> igIcon3.setImage(hoverIgImage));
        igAdmin3Button.setOnMouseExited(event -> igIcon3.setImage(defaultIgImage));
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void iconOnAction(ActionEvent event) {
        if (event.getSource() == facebookAdmin1Button) {
            System.out.println("Facebook Admin 1");
            openLink("https://www.facebook.com/mrthinh.ueter");
        } else if (event.getSource() == facebookAdmin2Button) {
            openLink("https://www.facebook.com/tuong.mt.73");
        } else if (event.getSource() == facebookAdmin3Button) {
            openLink("https://www.facebook.com/ichigoi.h");
        } else if (event.getSource() == igAdmin1Button) {
            openLink("https://www.instagram.com/mrthinh_/");
        } else if (event.getSource() == igAdmin2Button) {
            openLink("https://www.instagram.com/cristiano/");
        } else if (event.getSource() == igAdmin3Button) {
            openLink("https://www.instagram.com/pamyeuoi/");
        }
    }

    private void openLink(String link) {
        try {
            System.out.println("Open Link");
            Desktop.getDesktop().browse(new URI(link));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
