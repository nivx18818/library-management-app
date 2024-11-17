package app.libmgmt.view.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;

import java.awt.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordDialogController {

    @FXML
    private JFXButton facebookAdmin1Button, facebookAdmin2Button, facebookAdmin3Button;
    @FXML
    private JFXButton teleAdmin1Button, teleAdmin2Button, teleAdmin3Button;
    @FXML
    private ImageView fbIcon1, fbIcon2, fbIcon3;
    @FXML
    private ImageView telegramIcon1, telegramIcon2, telegramIcon3;
    @FXML
    private JFXButton closeDialogButton;
    @FXML
    private ImageView imgClose;

    // Store hover and default images for quick access
    private final Image hoverFbImage = new Image(getClass().getResource("/assets/icon/5296499_fb_facebook_facebook logo_icon.png").toExternalForm());
    private final Image defaultFbImage = new Image(getClass().getResource("/assets/icon/5305154_fb_facebook_facebook logo_icon.png").toExternalForm());
    private final Image hoverTeleImage = new Image(getClass().getResource("/assets/icon/3787425_telegram_logo_messanger_social_social media_icon.png").toExternalForm());
    private final Image defaultTeleImage = new Image(getClass().getResource("/assets/icon/7693324_telegram_social media_logo_messenger_icon.png").toExternalForm());

    // Map for handling button links
    private final Map<JFXButton, String> buttonLinks = new HashMap<>();

    public void initialize() {
        System.out.println("Initialize Forgot Password Dialog");

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
        initializeButtonLinks();
        initializeHoverEffects();
    }

    /**
     * Initializes the mapping of buttons to their respective URLs.
     */
    private void initializeButtonLinks() {
        buttonLinks.put(facebookAdmin1Button, "https://www.facebook.com/hgthinh3072x");
        buttonLinks.put(facebookAdmin2Button, "https://www.facebook.com/tuong.mt.73");
        buttonLinks.put(facebookAdmin3Button, "https://www.facebook.com/ichigoi.h");
        buttonLinks.put(teleAdmin1Button, "https://t.me/hg_thinh3072x");
        buttonLinks.put(teleAdmin2Button, "https://www.instagram.com/cristiano/");
        buttonLinks.put(teleAdmin3Button, "https://www.instagram.com/pamyeuoi/");
    }

    /**
     * Sets up hover effects for all social media buttons.
     */
    private void initializeHoverEffects() {
        setupHoverEffect(facebookAdmin1Button, fbIcon1, hoverFbImage, defaultFbImage);
        setupHoverEffect(facebookAdmin2Button, fbIcon2, hoverFbImage, defaultFbImage);
        setupHoverEffect(facebookAdmin3Button, fbIcon3, hoverFbImage, defaultFbImage);

        setupHoverEffect(teleAdmin1Button, telegramIcon1, hoverTeleImage, defaultTeleImage);
        setupHoverEffect(teleAdmin2Button, telegramIcon2, hoverTeleImage, defaultTeleImage);
        setupHoverEffect(teleAdmin3Button, telegramIcon3, hoverTeleImage, defaultTeleImage);
    }

    /**
     * Configures hover effects for a specific button and associated icon.
     *
     * @param button The button to apply hover effects.
     * @param icon The icon to change on hover.
     * @param hoverImage The image to display when hovered.
     * @param defaultImage The image to display when not hovered.
     */
    private void setupHoverEffect(JFXButton button, ImageView icon, Image hoverImage, Image defaultImage) {
        button.setOnMouseEntered(event -> icon.setImage(hoverImage));
        button.setOnMouseExited(event -> icon.setImage(defaultImage));
    }

    @FXML
    private void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    private void iconOnAction(ActionEvent event) {
        JFXButton sourceButton = (JFXButton) event.getSource();
        String link = buttonLinks.get(sourceButton);
        if (link != null) {
            openLink(link);
        }
    }

    /**
     * Opens the provided URL in the default browser.
     *
     * @param link The URL to open.
     */
    private void openLink(String link) {
        try {
            System.out.println("Open Link: " + link);
            Desktop.getDesktop().browse(new URI(link));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
