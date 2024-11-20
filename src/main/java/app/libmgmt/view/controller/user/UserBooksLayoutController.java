package app.libmgmt.view.controller.user;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.util.AnimationUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UserBooksLayoutController {

    private static UserBooksLayoutController controller;
    private final UserGlobalController userGlobalController = UserGlobalController.getInstance();

    @FXML
    private JFXButton acquireButton;

    @FXML
    private ImageView acquireImage;

    @FXML
    private Label acquireLabel;

    @FXML
    private HBox hBoxAcquire;

    @FXML
    private Pane refreshPaneButton;

    @FXML
    private Pane searchPane;

    @FXML
    private StackPane stackPaneContainer;

    @FXML
    private TextField textSearch;

    @FXML
    private VBox vBoxBooksList;

    private final String hoverAcquireLogo = "/assets/icon/acquire-logo-1.png";
    private final String acquireLogo = "/assets/icon/add-circle 1.png";

    private final List<String[]> observableBooksData = userGlobalController.getObservableBooksData();

    public UserBooksLayoutController() {
        controller = this;
    }

    public static UserBooksLayoutController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);
        System.out.println("User Books Layout initialized");

        preloadData(observableBooksData);
        stackPaneContainer.setOnMouseClicked(event -> stackPaneContainer.requestFocus());
    }

    // Data Preloading
    public void preloadData(List<String[]> data) {
        Task<Void> preloadTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    for (String[] d : data) {
                        loadBookBar(d);
                    }
                } catch (Exception e) {
                    System.out.println("Error loading data table: " + e.getMessage());
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            protected void failed() {
                // Handle when task fails
                System.out.println("Task failed: " + getException().getMessage());
            }
        };

        new Thread(preloadTask).start();
    }

    private void loadBookBar(String[] data) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    UserBooksLayoutController.class.getResource("/fxml/user/user-book-bar.fxml"));
            Pane scene = fxmlLoader.load();
            scene.setId(data[0]);
            UserBookBarController controller = fxmlLoader.getController();
            controller.setData(data);
            // Set the controller as UserData for easy access later
            scene.setUserData(controller);

            Platform.runLater(() -> {
                vBoxBooksList.getChildren().add(scene);
                AnimationUtils.zoomIn(scene, 1.0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnRefreshTableOnAction(ActionEvent event) {

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnAcquireOnAction(ActionEvent event) {

    }

    @FXML
    void btnAcquireOnMouseEntered(MouseEvent event) {
        hBoxAcquire.setStyle(
                "-fx-background-color: #F2F2F2; -fx-background-radius: 12px; -fx-border-color: #000; -fx-border-radius: 12px; -fx-border-width: 1.2px;");
        acquireImage.setImage(new Image(getClass().getResource(hoverAcquireLogo).toExternalForm()));
        acquireLabel.setStyle("-fx-text-fill: #000;");
    }

    @FXML
    void btnAcquireOnMouseExited(MouseEvent event) {
        hBoxAcquire.setStyle("-fx-background-color: #000; -fx-background-radius: 12px;");
        acquireImage.setImage(new Image(getClass().getResource(acquireLogo).toExternalForm()));
        acquireLabel.setStyle("-fx-text-fill: #F2F2F2;");
    }

}
