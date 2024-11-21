package app.libmgmt.view.controller.admin;

import com.jfoenix.controls.JFXButton;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.util.EnumUtils.UserType;
import app.libmgmt.view.controller.LogoutDialogController;

import java.io.IOException;
import java.util.logging.Logger;

public class AdminNavigationController {

    AdminGlobalController globalController = AdminGlobalController.getInstance();

    private static EnumUtils.NavigationButton latestButtonClicked = EnumUtils.NavigationButton.DASHBOARD;
    private static AdminNavigationController controller;

    @FXML
    private JFXButton dashboardButton, catalogButton, booksButton, usersButton, logoutButton;
    @FXML
    private ImageView dashboardLogo, catalogLogo, booksLogo, usersLogo, logoutLogo;
    @FXML
    private VBox navigationContainer;

    private static boolean uploadedBooksData = false, uploadedUsersData = false;

    // Constructor to set the controller instance
    public AdminNavigationController() {
        controller = this;
    }

    // Singleton pattern to get the current instance
    public static AdminNavigationController getInstance() {
        return controller;
    }

    // Initialization method called when the view is loaded
    @FXML
    public void initialize() {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);
        System.out.println("Navigation Controller initialized times");
        AnimationUtils.fadeInLeft(navigationContainer);
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
        setButtonStyle(usersButton, usersLogo, "/assets/icon/people-icon.png", "black", "white");
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
            case USERS:
                setButtonStyle(usersButton, usersLogo, "/assets/icon/people-icon-2.png", "white", "black");
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
        if (button.equals(dashboardButton)) {
            return EnumUtils.NavigationButton.DASHBOARD;
        }

        if (button.equals(catalogButton)) {
            return EnumUtils.NavigationButton.CATALOG;
        }

        if (button.equals(booksButton)) {
            return EnumUtils.NavigationButton.BOOKS;
        }

        if (button.equals(usersButton)) {
            return EnumUtils.NavigationButton.USERS;
        }

        if (button.equals(logoutButton)) {
            return EnumUtils.NavigationButton.LOGOUT;
        }

        return null;
    }

    // Navigation button click handlers
    @FXML
    public void dashboardButtonClicked(MouseEvent event) throws IOException {
        handleNavigation(EnumUtils.NavigationButton.DASHBOARD, "admin-dashboard.fxml", dashboardButton);
    }

    @FXML
    public void catalogButtonClicked(MouseEvent event) throws IOException {
        handleNavigation(EnumUtils.NavigationButton.CATALOG, "admin-borrowed-books-form.fxml", catalogButton);
    }

    @FXML
    public void booksButtonClicked(MouseEvent event) throws IOException {
        // Preload books data
        if (!uploadedBooksData) {
            globalController
                    .setObservableBookData(FXCollections.observableArrayList(globalController.preLoadBooksData()));
            uploadedBooksData = true;
        }
        handleNavigation(EnumUtils.NavigationButton.BOOKS, "admin-books-form.fxml", booksButton);
    }

    @FXML
    public void userButtonClicked(MouseEvent event) throws IOException {
        // Preload users data
        if (!uploadedUsersData) {
            globalController
                    .setStudentsData((FXCollections.observableArrayList(globalController.preLoadStudentsData())));
            globalController.setGuestsData((FXCollections.observableArrayList(globalController.preLoadGuestsData())));

            uploadedUsersData = true;
        }
        handleNavigation(EnumUtils.NavigationButton.USERS, "admin-users-form.fxml", usersButton);
    }

    @FXML
    public void logOutButtonClicked(MouseEvent event) throws IOException {
        if (latestButtonClicked == EnumUtils.NavigationButton.LOGOUT) {
            return;
        }

        ChangeScene.openAdminPopUp(AdminGlobalController.getInstance().getStackPaneContainer(),
                "/fxml/logout-dialog.fxml", null, EnumUtils.PopupList.LOGOUT);
        LogoutDialogController.getInstance().setUserType(UserType.ADMIN);
        handleEffectButtonClicked(logoutButton);
    }

    // Handles the navigation between scenes
    private void handleNavigation(EnumUtils.NavigationButton buttonType, String fxmlFile, JFXButton button)
            throws IOException {
        if (latestButtonClicked == buttonType) {
            return;
        }

        ChangeScene.navigateToScene(fxmlFile, UserType.ADMIN);
        handleEffectButtonClicked(button);
    }

    public void setLastButtonClicked(EnumUtils.NavigationButton button) {
        latestButtonClicked = button;
    }

    // Getter and Setter Methods
    public void setUploadedData(boolean uploadedBooksData, boolean uploadedUsersData) {
        AdminNavigationController.uploadedBooksData = uploadedBooksData;
        AdminNavigationController.uploadedUsersData = uploadedUsersData;
    }
}
