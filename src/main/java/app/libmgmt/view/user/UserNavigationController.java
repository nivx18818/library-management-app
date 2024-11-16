package app.libmgmt.view.user;

import app.libmgmt.view.admin.AdminGlobalController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;

import java.io.IOException;
import java.util.logging.Logger;

public class UserNavigationController {

    private static EnumUtils.NavigationButton latestButtonClicked = EnumUtils.NavigationButton.DASHBOARD;
    private static int initializedTimes = 0;
    private static UserNavigationController controller;

    @FXML
    private JFXButton dashboardButton, catalogButton, booksButton, logoutButton;
    @FXML
    private ImageView dashboardLogo, catalogLogo, booksLogo, logoutLogo;
    @FXML
    private VBox navigationContainer;

    // Constructor to set the controller instance
    public UserNavigationController() {
        controller = this;
    }

    // Singleton pattern to get the current instance
    public static UserNavigationController getInstance() {
        return controller;
    }

    // Initialization method called when the view is loaded
    @FXML
    public void initialize() {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);
        System.out.println("Navigation Controller initialized " + ++initializedTimes + " times");
        playAnimationOnFirstInitialization();
    }

    // Plays an animation if it's the first time the view is initialized
    private void playAnimationOnFirstInitialization() {
        if (initializedTimes == 1) {
            AnimationUtils.fadeInLeft(navigationContainer);
        }
    }

    // Handles the click effect for navigation buttons
    public void handleEffectButtonClicked(JFXButton button) {
        latestButtonClicked = getButtonType(button);
        resetButtonStylesToDefault();
        updateButtonStyle();
    }

    // Resets all navigation buttons to their default styles
    private void resetButtonStylesToDefault() {
        setButtonStyle(dashboardButton, dashboardLogo, "/assets/icon/dashboard-icon.png", "black", "white");
        setButtonStyle(booksButton, booksLogo, "/assets/icon/books-icon.png", "black", "white");
        setButtonStyle(catalogButton, catalogLogo, "/assets/icon/catalog-icon.png", "black", "white");
        setButtonStyle(logoutButton, logoutLogo, "/assets/icon/logout-icon.png", "black", "white");
    }

    // Updates the style of the button that was last clicked
    private void updateButtonStyle() {
        switch (latestButtonClicked) {
            case DASHBOARD:
                setButtonStyle(dashboardButton, dashboardLogo, "/assets/icon/dashboard-icon-2" +
                        ".png", "white", "black");
                break;
            case CATALOG:
                setButtonStyle(catalogButton, catalogLogo, "/assets/icon/catalog-icon-2.png", "white", "black");
                break;
            case BOOKS:
                setButtonStyle(booksButton, booksLogo, "/assets/icon/books-icon-2.png", "white", "black");
                break;
            case LOGOUT:
                // Handle logout style change if needed
                break;
        }
    }

    // Helper method to set button style and logo
    private void setButtonStyle(JFXButton button, ImageView logo, String pathToLogo,
                                String backgroundColor, String textColor) {
        Image image = new Image(getClass().getResource(pathToLogo).toExternalForm());
        button.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: " + textColor + ";");
        logo.setImage(image);
    }


    // Determines the button type based on the clicked button
    private EnumUtils.NavigationButton getButtonType(JFXButton button) {
        if (button.equals(dashboardButton)) return EnumUtils.NavigationButton.DASHBOARD;
        if (button.equals(catalogButton)) return EnumUtils.NavigationButton.CATALOG;
        if (button.equals(booksButton)) return EnumUtils.NavigationButton.BOOKS;
        if (button.equals(logoutButton)) return EnumUtils.NavigationButton.LOGOUT;
        return null;
    }

    // Navigation button click handlers
    @FXML
    public void dashboardButtonClicked(MouseEvent event) throws IOException {
//        handleNavigation(EnumUtils.NavigationButton.DASHBOARD, "admin-dashboard.fxml", dashboardButton);
    }

    @FXML
    public void catalogButtonClicked(MouseEvent event) throws IOException {
//        handleNavigation(EnumUtils.NavigationButton.CATALOG, "admin-borrowed-books-form.fxml", catalogButton);
    }

    @FXML
    public void booksButtonClicked(MouseEvent event) throws IOException {
//        handleNavigation(EnumUtils.NavigationButton.BOOKS, "admin-books-form.fxml", booksButton);
    }

    @FXML
    public void logOutButtonClicked(MouseEvent event) throws IOException {
        if (latestButtonClicked == EnumUtils.NavigationButton.LOGOUT) return;
        ChangeScene.openAdminPopUp(AdminGlobalController.getInstance().getStackPaneContainer(), "/fxml/logout-dialog.fxml");
        handleEffectButtonClicked(logoutButton);
    }

    // Handles the navigation between scenes
    private void handleNavigation(EnumUtils.NavigationButton buttonType, String fxmlFile, JFXButton button) throws IOException {
        if (latestButtonClicked == buttonType) return;
        ChangeScene.navigateToScene(fxmlFile);
        handleEffectButtonClicked(button);
    }
}
