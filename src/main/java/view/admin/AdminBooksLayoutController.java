package view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import util.AnimationUtils;
import util.ChangeScene;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AdminBooksLayoutController {

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

    @FXML
    public StackPane stackPaneContainer;

    private final AdminGlobalFormController adminGlobalFormController = AdminGlobalFormController.getInstance();

    private List<String[]> booksData = adminGlobalFormController.getBooksData();

    private static AdminBooksLayoutController controller;

    public AdminBooksLayoutController() {
        controller = this;
    }

    public static AdminBooksLayoutController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);

        System.out.println("Admin Books Layout initialized");

        preloadData(booksData);

        stackPaneContainer.setOnMouseClicked(
            event -> {
                stackPaneContainer.requestFocus();
            }
        );
    }

    public void preloadData(List<String[]> data) {
        Task<Void> preloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (String[] d : data) {
                    loadBookBar(d);
                }
                return null;
            }
            @Override
            protected void failed() {
                System.out.println("Error loading data table: " + getException().getMessage());
                throw new RuntimeException(getException());
            }
        };
        new Thread(preloadTask).start();
    }

    public void loadBookBar(String[] data) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AdminBooksLayoutController.class.getResource(
                "/fxml/admin-book-bar.fxml"));
        Pane scene = fxmlLoader.load();
        AdminBookBarController controller = fxmlLoader.getController();
        controller.setData(data);
        Platform.runLater(() -> {
            vBoxBooksList.getChildren().add(scene);
            AnimationUtils.zoomIn(scene, 1.0);
        });
    }

    @FXML
    void addBookButtonClicked(MouseEvent event) throws IOException{
        ChangeScene.openAdminPopUp(stackPaneContainer, "/fxml/admin-add-book-dialog.fxml");
    }

    @FXML
    void btnRefreshTableOnAction(ActionEvent event) {
        refreshBooksList();
    }

    public void refreshBooksList() {
        vBoxBooksList.getChildren().clear();
        preloadData(booksData);

        textSearch.clear();
        textSearch.setEditable(true);
    }


    @FXML
    void btnRefreshTableOnMouseEntered(MouseEvent event) {

    }

    @FXML
    void btnRefreshTableOnMouseExited(MouseEvent event) {

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String text = textSearch.getText();

        if (text.isEmpty()) {
            return;
        }

        showFilteredData(text);
        textSearch.setEditable(false);
    }

    private void showFilteredData(String searchText) {
        vBoxBooksList.getChildren().clear();

        adminGlobalFormController.getBooksData().stream()
                .filter(data -> data[2].toLowerCase().contains(searchText.toLowerCase()))
                .forEach(data -> {
                    try {
                        loadBookBar(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }


    @FXML
    void txtSearchOnMouseMoved(MouseEvent event) {

    }

    public String getSearchText() {
        return textSearch.getText();
    }

    public void deleteBookBar(String id) {
    }

}
