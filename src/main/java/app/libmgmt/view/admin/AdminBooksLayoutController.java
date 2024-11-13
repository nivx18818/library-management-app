package app.libmgmt.view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AdminBooksLayoutController {

    private static AdminBooksLayoutController controller;
    private final AdminGlobalController adminGlobalController = AdminGlobalController.getInstance();
    @FXML
    public StackPane stackPaneContainer;
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
    private final List<String[]> observableBooksData = adminGlobalController.getObservableBookData();

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

        preloadData(observableBooksData);

        stackPaneContainer.setOnMouseClicked(
                event -> {
                    stackPaneContainer.requestFocus();
                }
        );

        listenBookDataChanges();
    }

    private void listenBookDataChanges() {
        adminGlobalController.getObservableBookData().addListener((ListChangeListener.Change<? extends String[]> change) -> {
            while (change.next()) {
                boolean isUpdate = change.wasRemoved() && change.getRemovedSize() == change.getAddedSize();

                if (change.wasRemoved() && !isUpdate) {
                    for (String[] removeBook : change.getRemoved()) {
                        String bookId = removeBook[0];
                        removeBookFromVBox(bookId);
                    }
                }
            }
        });
    }

    private void removeBookFromVBox(String bookId) {
        for (int i = 0; i < vBoxBooksList.getChildren().size(); i++) {
            if (vBoxBooksList.getChildren().get(i).getId() != null && vBoxBooksList.getChildren().get(i).getId().equals(bookId)) {
                int finalI = i;
                Platform.runLater(() -> vBoxBooksList.getChildren().remove(finalI));
                return;
            }
        }
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
        scene.setId(data[0]);
        AdminBookBarController controller = fxmlLoader.getController();
        controller.setData(data);
        Platform.runLater(() -> {
            vBoxBooksList.getChildren().add(scene);
            AnimationUtils.zoomIn(scene, 1.0);
        });
    }

    @FXML
    void addBookButtonClicked(MouseEvent event) throws IOException {
        ChangeScene.openAdminPopUp(stackPaneContainer, "/fxml/admin-add-book-dialog.fxml");
    }

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

        adminGlobalController.getObservableBookData().stream()
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

}
