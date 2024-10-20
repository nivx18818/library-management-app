package view.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import util.Animation;

import java.io.IOException;
import java.util.logging.Logger;

public class AdminNavigationController {

    public static JFXDialog dialog;
    private static String latestButtonClicked = "dashboard";
    private static int initializedTimes = 0;
    @FXML
    private JFXButton booksButton;
    @FXML
    private ImageView booksLogo;
    @FXML
    private JFXButton catalogButton;
    @FXML
    private ImageView catalogLogo;
    @FXML
    private JFXButton dashboardButton;
    @FXML
    private ImageView dashboardLogo;
    @FXML
    private JFXButton logOutButton;
    @FXML
    private ImageView logOutLogo;
    @FXML
    private JFXButton usersButton;
    @FXML
    private ImageView usersLogo;
    @FXML
    private VBox navigationContainer;

    public static void openPopUp(StackPane stackPane, String path) {
        try {
            FXMLLoader loader = new FXMLLoader(AdminNavigationController.class.getResource(path));
            Pane content = loader.load();

            dialog = new JFXDialog(stackPane, content,
                    JFXDialog.DialogTransition.CENTER);

            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closePopUp() {
        dialog.close();
    }

    @FXML
    public void initialize() {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);
        System.out.println("Navigation Controller initialized " + ++initializedTimes + " times");

        showAnimation();

        navigationBarButtonClick();
    }

    public void navigationBarButtonClick() {
        switch (latestButtonClicked) {
            case "dashboard":
                changeButtonLayout("/assets/icon/dashboard-icon-2.png", dashboardButton, dashboardLogo);
                break;
            case "catalog":
                changeButtonLayout("/assets/icon/catalog-icon-2.png", catalogButton, catalogLogo);
                break;
            case "books":
                changeButtonLayout("/assets/icon/books-icon-2.png", booksButton, booksLogo);
                break;
            case "users":
                changeButtonLayout("/assets/icon/people-icon-2.png", usersButton, usersLogo);
                break;
            case "logout":
                changeButtonLayout("/assets/icon/logout-icon-2.png", logOutButton, logOutLogo);
                break;
        }
    }

    public void changeButtonLayout(String pathToLogo, JFXButton button, ImageView logo) {
        Image image = new Image(getClass().getResource(pathToLogo).toExternalForm());
        button.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        logo.setImage(image);
    }

    public void setDefaultButtons() {
        Image defaultDashboardLogo = new Image(getClass().getResource("/assets/icon/dashboard-icon.png").toExternalForm());
        Image defaultBooksLogo = new Image(getClass().getResource("/assets/icon/books-icon.png").toExternalForm());
        Image defaultCatalogLogo = new Image(getClass().getResource("/assets/icon/catalog-icon.png").toExternalForm());
        Image defaultUsersLogo = new Image(getClass().getResource("/assets/icon/people-icon.png").toExternalForm());
        Image defaultLogOutLogo = new Image(getClass().getResource("/assets/icon/logout-icon.png").toExternalForm());

        dashboardLogo.setImage(defaultDashboardLogo);
        booksLogo.setImage(defaultBooksLogo);
        catalogLogo.setImage(defaultCatalogLogo);
        usersLogo.setImage(defaultUsersLogo);
        logOutLogo.setImage(defaultLogOutLogo);

        dashboardButton.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        booksButton.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        catalogButton.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        usersButton.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        logOutButton.setStyle("-fx-background-color: black; -fx-text-fill: white;");
    }

    public void showAnimation() {
        if (initializedTimes == 1) {
            Animation.fadeInLeft(navigationContainer);
        }
        if (latestButtonClicked.equals("logout")) {
            Animation.fadeInRight(navigationContainer);
        }
    }

    @FXML
    public void dashboardButtonClicked(MouseEvent event) throws IOException {
        navigateToScene(dashboardButton, dashboardLogo, "/assets/icon/dashboard-icon-2.png",
                "admin-dashboard.fxml");
    }

    @FXML
    public void catalogButtonClicked(MouseEvent event) throws IOException {
        navigateToScene(catalogButton, catalogLogo, "/assets/icon/catalog-icon-2.png", "admin" +
                "-borrowed-books-form.fxml");
    }

    @FXML
    public void booksButtonClicked(MouseEvent event) throws IOException {
        navigateToScene(booksButton, booksLogo, "/assets/icon/books-icon-2.png", "admin" +
                "-books-form.fxml");
    }

    @FXML
    public void userButtonClicked(MouseEvent event) throws IOException {
        navigateToScene(usersButton, usersLogo, "/assets/icon/people-icon-2.png", "admin" +
                "-users-form.fxml");
    }

    public void navigateToScene(JFXButton button, ImageView logo, String hoverLogoPath,
                                String fxmlPath) throws IOException {
        if (latestButtonClicked.equals(button.getText().toLowerCase())) {
            return;
        }

        AdminGlobalFormController controller = AdminGlobalFormController.getInstance();

        latestButtonClicked = button.getText().toLowerCase();

        setDefaultButtons();
        navigationBarButtonClick();

        Pane pane = controller.getPagingPane();
        pane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(AdminNavigationController.class.getResource("/fxml/" + fxmlPath));
        Parent root = loader.load();
        pane.getChildren().add(root);
        Animation.zoomIn(controller.getPagingPane(), 1.0);
    }

}
