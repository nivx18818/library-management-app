package view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.AnimationUtils;
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
    private JFXButton teleAdmin1Button;

    @FXML
    private JFXButton teleAdmin2Button;

    @FXML
    private JFXButton teleAdmin3Button;

    @FXML
    private ImageView fbIcon1;

    @FXML
    private ImageView telegramIcon1;

    @FXML
    private ImageView fbIcon2;

    @FXML
    private ImageView telegramIcon2;

    @FXML
    private ImageView fbIcon3;

    @FXML
    private ImageView telegramIcon3;

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private ImageView imgClose;

    public void initialize() {
        System.out.println("Initialize Forgot Password Dialog");

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
        changeHoverIcons();
    }

    public void changeHoverIcons() {
        Image hoverFbImage =
                new Image(getClass().getResource("/assets/icon/5296499_fb_facebook_facebook " +
                        "logo_icon.png").toExternalForm());
        Image defaultFbImage =
                new Image(getClass().getResource("/assets/icon/5305154_fb_facebook_facebook logo_icon.png").toExternalForm());
        Image hoverTeleImage =
                new Image(getClass().getResource("/assets/icon/3787425_telegram_logo_messanger_social_social media_icon.png").toExternalForm());
        Image defaultTeleImage = new Image(getClass().getResource("/assets/icon" +
                "/7693324_telegram_social media_logo_messenger_icon.png").toExternalForm());

        facebookAdmin1Button.setOnMouseEntered(event -> fbIcon1.setImage(hoverFbImage));
        facebookAdmin1Button.setOnMouseExited(event -> fbIcon1.setImage(defaultFbImage));
        teleAdmin1Button.setOnMouseEntered(event -> telegramIcon1.setImage(hoverTeleImage));
        teleAdmin1Button.setOnMouseExited(event -> telegramIcon1.setImage(defaultTeleImage));
        facebookAdmin2Button.setOnMouseEntered(event -> fbIcon2.setImage(hoverFbImage));
        facebookAdmin2Button.setOnMouseExited(event -> fbIcon2.setImage(defaultFbImage));
        teleAdmin2Button.setOnMouseEntered(event -> telegramIcon2.setImage(hoverTeleImage));
        teleAdmin2Button.setOnMouseExited(event -> telegramIcon2.setImage(defaultTeleImage));
        facebookAdmin3Button.setOnMouseEntered(event -> fbIcon3.setImage(hoverFbImage));
        facebookAdmin3Button.setOnMouseExited(event -> fbIcon3.setImage(defaultFbImage));
        teleAdmin3Button.setOnMouseEntered(event -> telegramIcon3.setImage(hoverTeleImage));
        teleAdmin3Button.setOnMouseExited(event -> telegramIcon3.setImage(defaultTeleImage));
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void iconOnAction(ActionEvent event) {
        if (event.getSource() == facebookAdmin1Button) {
            System.out.println("Facebook Admin 1");
            openLink("https://www.facebook.com/hgthinh3072x");
        } else if (event.getSource() == facebookAdmin2Button) {
            openLink("https://www.facebook.com/tuong.mt.73");
        } else if (event.getSource() == facebookAdmin3Button) {
            openLink("https://www.facebook.com/ichigoi.h");
        } else if (event.getSource() == teleAdmin1Button) {
            openLink("https://t.me/hg_thinh3072x");
        } else if (event.getSource() == teleAdmin2Button) {
            openLink("https://www.instagram.com/cristiano/");
        } else if (event.getSource() == teleAdmin3Button) {
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
