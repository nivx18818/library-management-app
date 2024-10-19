package view;

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
import util.Animation;

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
    private StackPane stackPaneContainer;

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
    }

    public void preloadData(List<String[]> data) {
        Task<Void> preloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (String[] d : data) {
                    FXMLLoader fxmlLoader = new FXMLLoader(AdminBooksLayoutController.class.getResource(
                            "/fxml/admin-book-bar.fxml"));
                    Pane scene = fxmlLoader.load();
                    AdminBookBarController controller = fxmlLoader.getController();
                    controller.setData(d[0], d[1], d[2], d[3], d[4], d[5]);
                    Platform.runLater(() -> {
                        vBoxBooksList.getChildren().add(scene);
                        Animation.zoomIn(scene, 1.0);
                    });
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

    @FXML
    void addBookButtonClicked(MouseEvent event) throws IOException{
        NavigationController.openPopUp(stackPaneContainer, "/fxml/admin-add-book-dialog.fxml");
    }

    @FXML
    void btnRefreshTableOnAction(ActionEvent event) {
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

    public void showFilteredData(String text) {
        //TODO: Implement search functionality
    }

    @FXML
    void txtSearchOnMouseMoved(MouseEvent event) {

    }

}
