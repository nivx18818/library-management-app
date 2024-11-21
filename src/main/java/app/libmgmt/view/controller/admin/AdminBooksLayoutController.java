package app.libmgmt.view.controller.admin;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils.PopupList;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AdminBooksLayoutController {

    private static AdminBooksLayoutController controller;
    private final AdminGlobalController adminGlobalController = AdminGlobalController.getInstance();

    @FXML
    private StackPane stackPaneContainer;
    @FXML
    private JFXButton addBookButton;
    @FXML
    private Pane refreshPaneButton;
    @FXML
    private Pane searchPane;
    @FXML
    private TextField textSearch;
    @FXML
    private VBox vBoxBooksList;

    private String deletedOrderNumber;

    private final List<String[]> observableBooksData = adminGlobalController.getObservableBookData();

    public AdminBooksLayoutController() {
        controller = this;
    }

    public static AdminBooksLayoutController getInstance() {
        return controller;
    }

    // Initializer
    @FXML
    public void initialize() {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);
        System.out.println("Admin Books Layout initialized");

        preloadData(observableBooksData);
        stackPaneContainer.setOnMouseClicked(event -> stackPaneContainer.requestFocus());
        listenBookDataChanges();
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
                    AdminBooksLayoutController.class.getResource("/fxml/admin/admin-book-bar.fxml"));
            Pane scene = fxmlLoader.load();
            scene.setId(data[0]);
            AdminBookBarController controller = fxmlLoader.getController();
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

    // Book Data Changes Listener
    private void listenBookDataChanges() {
        adminGlobalController.getObservableBookData()
                .addListener((ListChangeListener.Change<? extends String[]> change) -> {
                    while (change.next()) {
                        if (change.wasRemoved() && change.getRemovedSize() != change.getAddedSize()) {
                            String[] book = change.getRemoved().get(0);
                            if (book != null && book.length > 0) {
                                String bookId = book[0];
                                removeBookFromVBox(bookId);
                            }
                        }
                    }
                });
    }

    private void removeBookFromVBox(String bookId) {
        vBoxBooksList.getChildren().stream()
                .filter(child -> child.getId() != null && child.getId().equals(bookId))
                .findFirst()
                .ifPresent(child -> Platform.runLater(() -> {
                    vBoxBooksList.getChildren().remove(child);
                    handleChangeOrderNumber(deletedOrderNumber);
                }));
    }

    private void handleChangeOrderNumber(String deletedOrderNumber) {
        int orderNumber = Integer.parseInt(deletedOrderNumber);
        for (int i = orderNumber - 1; i < vBoxBooksList.getChildren().size(); i++) {
            Pane bookBar = (Pane) vBoxBooksList.getChildren().get(i);
            AdminBookBarController controller = (AdminBookBarController) bookBar.getUserData();
            if (controller != null) {
                controller.setOrderNumber(String.valueOf(i + 1));
            } else {
                System.out.println("Controller property is missing or not of type AdminBookBarController");
            }
        }
    }

    // Refresh Table
    @FXML
    void btnRefreshTableOnAction(ActionEvent event) {
        refreshBooksList();
    }

    public void refreshBooksList() {
        vBoxBooksList.getChildren().clear();
        preloadData(observableBooksData);
        textSearch.clear();
        textSearch.setEditable(true);
    }

    // Search Functionality
    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String searchText = textSearch.getText();
        if (!searchText.isEmpty()) {
            showFilteredData(searchText);
            textSearch.setEditable(false);
        }
    }

    private void showFilteredData(String searchText) {
        vBoxBooksList.getChildren().clear();
        adminGlobalController.getObservableBookData().stream()
                .filter(data -> data[2].toLowerCase().contains(searchText.toLowerCase()))
                .forEach(this::loadBookBar);
    }

    @FXML
    void txtSearchOnMouseMoved(MouseEvent event) {
    }

    public String getSearchText() {
        return textSearch.getText();
    }

    // Button Actions
    @FXML
    void addBookButtonClicked(MouseEvent event) throws IOException {
        ChangeScene.openAdminPopUp(stackPaneContainer, "/fxml/admin/admin-add-book-dialog.fxml", null, PopupList.ADD_BOOK);
    }

    @FXML
    void btnRefreshTableOnMouseEntered(MouseEvent event) {
    }

    @FXML
    void btnRefreshTableOnMouseExited(MouseEvent event) {
    }

    public StackPane getStackPaneContainer() {
        return stackPaneContainer;
    }

    public void setDeletedOrderNumber(String deletedOrderNumber) {
        this.deletedOrderNumber = deletedOrderNumber;
    }
}
