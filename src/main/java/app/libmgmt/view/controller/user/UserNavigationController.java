package app.libmgmt.view.controller.user;

import com.jfoenix.controls.JFXButton;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
// import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.util.EnumUtils.UserType;
import app.libmgmt.view.controller.LogoutDialogController;

import java.io.IOException;
import java.util.logging.Logger;

public class UserNavigationController {

    UserGlobalController globalController = UserGlobalController.getInstance();

    private static EnumUtils.NavigationButton latestButtonClicked = EnumUtils.NavigationButton.DASHBOARD;
    private static UserNavigationController controller;

    @FXML
    private JFXButton dashboardButton, catalogButton, booksButton, logoutButton;
    @FXML
    private ImageView dashboardLogo, catalogLogo, booksLogo, logoutLogo;
    @FXML
    private VBox navigationContainer;
    private static boolean uploadedBooksData = false;

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
        System.out.println("Navigation Controller initialized ");
        AnimationUtils.fadeInLeft(navigationContainer);
    }

    // Handles the click effect for navigation buttons
    public void handleEffectButtonClicked(JFXButton button) {
        latestButtonClicked = getButtonType(button);
        if (latestButtonClicked == EnumUtils.NavigationButton.LOGOUT) {
            return;
        }
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
            default:
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
        if (button.equals(dashboardButton))
            return EnumUtils.NavigationButton.DASHBOARD;
        if (button.equals(catalogButton))
            return EnumUtils.NavigationButton.CATALOG;
        if (button.equals(booksButton))
            return EnumUtils.NavigationButton.BOOKS;
        if (button.equals(logoutButton))
            return EnumUtils.NavigationButton.LOGOUT;
        return null;
    }

    // Navigation button click handlers
    @FXML
    public void dashboardButtonClicked(MouseEvent event) throws IOException {
        handleNavigation(EnumUtils.NavigationButton.DASHBOARD, "user-dashboard.fxml", dashboardButton);
    }

    @FXML
    public void catalogButtonClicked(MouseEvent event) throws IOException {
        handleNavigation(EnumUtils.NavigationButton.CATALOG, "user-catalog-form.fxml", catalogButton);
    }

    @FXML
    public void booksButtonClicked(MouseEvent event) throws IOException {
        if (!uploadedBooksData) {
            globalController.preLoadBooksData(
                    books -> {
                        globalController.setObservableBookData(FXCollections.observableArrayList(books));
                        uploadedBooksData = true;
                        try {
                            handleNavigation(EnumUtils.NavigationButton.BOOKS, "user-books-layout.fxml", booksButton);
                        } catch (IOException e) {
                            showErrorDialog("Error", "Failed to navigate: " + e.getMessage());
                        }
                    },
                    error -> {
                        showErrorDialog("Error", "Failed to load books: " + error.getMessage());
                    });
        } else {
            handleNavigation(EnumUtils.NavigationButton.BOOKS, "user-books-layout.fxml", booksButton);
        }
    }

    @FXML
    public void logOutButtonClicked(MouseEvent event) throws IOException {
        ChangeScene.openAdminPopUp(UserGlobalController.getInstance().getStackPaneContainer(),
                "/fxml/logout-dialog.fxml", EnumUtils.PopupList.LOGOUT);
        LogoutDialogController.getInstance().setUserType(UserType.STUDENT);
        handleEffectButtonClicked(logoutButton);
    }

    // Handles the navigation between scenes
    public void handleNavigation(EnumUtils.NavigationButton buttonType, String fxmlFile, JFXButton button)
            throws IOException {
        if (latestButtonClicked == buttonType)
            return;
        ChangeScene.navigateToScene(fxmlFile, UserType.STUDENT);
        handleEffectButtonClicked(button);
    }

    // Getters for the navigation buttons
    public JFXButton getCatalogButton() {
        return catalogButton;
    }

    public JFXButton getBooksButton() {
        return booksButton;
    }

    public static boolean isUploadedBooksData() {
        return uploadedBooksData;
    }

    public static void setUploadedBooksData(boolean uploadedBooksData) {
        UserNavigationController.uploadedBooksData = uploadedBooksData;
    }

    public void setLastButtonClicked(EnumUtils.NavigationButton button) {
        latestButtonClicked = button;
    }

    public void setUploadedData(boolean uploadedBooksData) {
        UserNavigationController.uploadedBooksData = uploadedBooksData;
    }

    public void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
