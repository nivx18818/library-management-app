package app.libmgmt.view.controller.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.EnumUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UserCatalogController {

    private static UserCatalogController controller;

    @FXML
    private JFXButton borrowedBooksButton;

    @FXML
    private Label borrowedBooksLabel;

    @FXML
    private Pane borrowedBooksPane;

    @FXML
    private Pane refreshPaneButton;

    @FXML
    private JFXButton returnedBooksButton;

    @FXML
    private Label returnedBooksLabel;

    @FXML
    private Pane returnedBooksPane;

    @FXML
    private Pane searchPane;

    @FXML
    private StackPane stackPaneContainer;

    @FXML
    private TextField textSearch;

    @FXML
    private VBox vBoxBooksList;

    @FXML
    private Text bookWormText;

    // External controllers and data
    private final UserGlobalController userGlobalController = UserGlobalController.getInstance();
    private final List<String[]> borrowedBooksData = userGlobalController.getBorrowedBooksData();
    private final List<String[]> returnedBooksData = getReturnedBooksData();

    // Constructor and Singleton Pattern
    public UserCatalogController() {
            controller = this;
        }

    public static UserCatalogController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        System.out.println("User Catalog initialized");

        if (EnumUtils.currentStateUserCatalog == EnumUtils.CATALOG_STATE.BORROWED) {
            updateStatusUI(EnumUtils.CATALOG_STATE.BORROWED);
            showBorrowedBooksList();
        } else if (EnumUtils.currentStateUserCatalog == EnumUtils.CATALOG_STATE.RETURNED) {
            updateStatusUI(EnumUtils.CATALOG_STATE.RETURNED);
            showReturnedBooksList();
        }
    }

    @FXML
    void btnBorrowedBooksOnAction(ActionEvent event) {
        if (EnumUtils.currentStateUserCatalog == EnumUtils.CATALOG_STATE.BORROWED) {
            return;
        }

        updateStatusUI(EnumUtils.CATALOG_STATE.BORROWED);
        showBorrowedBooksList();
    }

    @FXML
    void btnRefreshTableOnAction(ActionEvent event) {
        if (EnumUtils.currentStateUserCatalog == EnumUtils.CATALOG_STATE.BORROWED) {
            showBorrowedBooksList();
        } else if (EnumUtils.currentStateUserCatalog == EnumUtils.CATALOG_STATE.RETURNED) {
            showReturnedBooksList();
        }
        textSearch.clear();
        textSearch.setEditable(true);
    }

    @FXML
    void btnReturnedBooksOnAction(ActionEvent event) {
        if (EnumUtils.currentStateUserCatalog == EnumUtils.CATALOG_STATE.RETURNED) {
            return;
        }

        updateStatusUI(EnumUtils.CATALOG_STATE.RETURNED);
        showReturnedBooksList();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        // TODO: Implement search functionality by book's name

        textSearch.setEditable(false);
    }

    // Data Display
    public void showBorrowedBooksList() {
        vBoxBooksList.getChildren().clear();
        preloadData(borrowedBooksData, EnumUtils.CATALOG_STATE.BORROWED);
    }

    public void showReturnedBooksList() {
        vBoxBooksList.getChildren().clear();
        preloadData(returnedBooksData, EnumUtils.CATALOG_STATE.RETURNED);
    }

    // Data Preloading
    public void preloadData(List<String[]> data, EnumUtils.CATALOG_STATE currentStatus) {
        Task<Void> preloadTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    for (String[] d : data) {
                        loadBorrowedBookBar(d, currentStatus);
                    }
                } catch (Exception e) {
                    System.out.println("Error loading data table: " + e.getMessage());
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            protected void failed() {
                // Xử lý khi tác vụ thất bại
                System.out.println("Task failed: " + getException().getMessage());
            }
        };

        new Thread(preloadTask).start();
    }

    // Load a single borrowed book bar
    public void loadBorrowedBookBar(String[] d, EnumUtils.CATALOG_STATE currentStatus) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/fxml/user/user-catalog-borowed-books-bar.fxml"));
            Pane scene = fxmlLoader.load();
            UserCatalogBorrowedBookBarController controller = fxmlLoader.getController();
            controller.setData(d);
            if (currentStatus == EnumUtils.CATALOG_STATE.BORROWED) {
                controller.setVisibleAction(true);
            } else {
                controller.setVisibleAction(false);
            }

            // Add to VBox and animate
            Platform.runLater(() -> {
                vBoxBooksList.getChildren().add(scene);
                AnimationUtils.zoomIn(scene, 1.0);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String[]> getReturnedBooksData() {
        // TODO: Load returned books data from database

        List<String[]> data = new ArrayList<>();
        data.add(new String[] { "1",
                "https://th.bing.com/th/id/OIP.FTtvbIR52uSVcUrvWBENsAHaLG?w=1170&h=1753&rs=1&pid=ImgDetMain",
                "Story Book", "2", "11/11/2024", "17/11/2024" });
        data.add(new String[] { "1",
                "https://cdn11.bigcommerce.com/s-ep2pzxabtm/images/stencil/1280w/products/395/816/healinggrief__40416.1554653848.jpg?c=2",
                "Story Book", "2", "11/11/2024", "17/11/2024" });
        return data;
    }

    // UI Update Methods
    private void setDefaultStyle() {
        borrowedBooksLabel.setStyle("-fx-text-fill: black;");
        returnedBooksLabel.setStyle("-fx-text-fill: black;");
        borrowedBooksPane.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 12px;");
        returnedBooksPane.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 12px;");
    }

    private void updateStatusUI(EnumUtils.CATALOG_STATE newStatus) {
        setDefaultStyle();
        if (newStatus == EnumUtils.CATALOG_STATE.BORROWED) {
            borrowedBooksLabel.setStyle("-fx-text-fill: white;");
            borrowedBooksPane.setStyle("-fx-background-color: black; -fx-background-radius: 12px;");
        } else {
            returnedBooksLabel.setStyle("-fx-text-fill: white;");
            returnedBooksPane.setStyle("-fx-background-color: black; -fx-background-radius: 12px;");
        }
        EnumUtils.currentStateUserCatalog = newStatus;
    }
}
