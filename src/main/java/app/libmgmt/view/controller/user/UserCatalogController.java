package app.libmgmt.view.controller.user;

import java.io.IOException;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.EnumUtils;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UserCatalogController {

    private static UserCatalogController controller;

    @FXML
    private JFXButton borrowedBooksButton;

    @FXML
    private JFXButton refreshButton;

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

    private String deletedOrderNumber;

    // External controllers and data
    private final UserGlobalController userGlobalController = UserGlobalController.getInstance();
    private List<String[]> borrowedBooksData = userGlobalController.getBorrowedBooksData();
    private List<String[]> returnedBooksData = userGlobalController.getReturnedBooksData();

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

        listenReturnBookEvent();
    }

    private void listenReturnBookEvent() {
        userGlobalController.getReturnedBooksData()
                .addListener((ListChangeListener.Change<? extends String[]> change) -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            try {
                                Pane bookBar = (Pane) vBoxBooksList.getChildren()
                                        .get(Integer.parseInt(deletedOrderNumber) - 1);

                                // get scene of hbox and find controller
                                UserCatalogBorrowedBookBarController controller = (UserCatalogBorrowedBookBarController) bookBar
                                        .getUserData();
                                if (controller != null) {
                                    controller.setDisableReturnButton(true);
                                } else {
                                    System.err.println("Controller not found for book bar");
                                }
                            } catch (IndexOutOfBoundsException e) {
                                System.err.println("Invalid order number: " + deletedOrderNumber);
                            } catch (Exception e) {
                                System.err.println("Error updating return button: " + e.getMessage());
                            }
                        }
                    }
                });
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
    void btnOnMouseEntered(MouseEvent event) {
        if (event.getSource() == borrowedBooksButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.HOVER_SCALE, borrowedBooksPane).play();
        } else if (event.getSource() == returnedBooksButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.HOVER_SCALE, returnedBooksPane).play();
        } else if (event.getSource() == refreshButton) {
            AnimationUtils.createScaleTransition(1.15, refreshPaneButton).play();
        } else if (event.getSource() == searchPane) {
            AnimationUtils.createScaleTransition(1.05, searchPane).play();
        }
    }

    @FXML
    void btnOnMouseExited(MouseEvent event) {
        if (event.getSource() == borrowedBooksButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, borrowedBooksPane).play();
        } else if (event.getSource() == returnedBooksButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, returnedBooksPane).play();
        } else if (event.getSource() == refreshButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, refreshPaneButton).play();
        } else if (event.getSource() == searchPane) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, searchPane).play();
        }
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
            // Set the controller data for the scene, serve for find controller later
            scene.setUserData(controller);
            controller.setData(d);

            if (currentStatus == EnumUtils.CATALOG_STATE.BORROWED) {
                controller.setVisibleAction(true);
                // Check if the book is already returned, then disable the return button
                controller.setDisableReturnButton(userGlobalController.isBookReturned(d[0]));
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

    public void setDeletedOrderNumber(String deletedOrderNumber) {
        this.deletedOrderNumber = deletedOrderNumber;
    }
}
