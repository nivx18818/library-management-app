package app.libmgmt.view.controller.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.model.Book;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils.PopupList;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

    private final List<Book> observableBooksData = userGlobalController.getObservableBooksData();

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
    public void preloadData(List<Book> data) {
        Task<Void> preloadTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    for (Book d : data) {
                        String authorsString = String.join(", ", d.getAuthors());
                        String categoriesString = String.join(", ", d.getCategories());
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String publishedDateStr = (d.getPublishedDate() != null) ?
                                            outputFormat.format(d.getPublishedDate()) : "Not Available";

                        String[] data = new String[] { d.getIsbn(), d.getCoverUrl(), d.getTitle(), categoriesString, authorsString,
                                String.valueOf(d.getAvailableCopies()), d.getPublisher(), publishedDateStr, d.getWebReaderUrl() };
                        loadBookBar(data);
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
            UserBookBarController controller = fxmlLoader.getController();
            controller.setData(data);
            // Set the controller as UserData for easy access later
            scene.setId(data[0]);
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
        refreshBooksList();
    }

    public void refreshBooksList() {
        vBoxBooksList.getChildren().clear();
        preloadData(observableBooksData);
        textSearch.clear();
        textSearch.setEditable(true);
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnAcquireOnAction(ActionEvent event) {
        if (!getSelectedBooksList().isEmpty()) {
            ChangeScene.openAdminPopUp(stackPaneContainer, "/fxml/user/user-borrowed-books-confirmation-dialog.fxml", PopupList.ACQUIRE_BOOK);
        } else {
            ChangeScene.openAdminPopUp(stackPaneContainer, "/fxml/empty-data-notification-dialog.fxml", PopupList.EMPTY_DATA_NOTIFICATION);
        }
    }

    public List<Book> getSelectedBooksList() {
        List<Book> selectedBooks = new ArrayList<>();

        for (Node node : vBoxBooksList.getChildren()) {
            if (node instanceof Pane) {
                Pane bookBar = (Pane) node;
                UserBookBarController controller = (UserBookBarController) bookBar.getUserData();
                
                try {
                    if (controller.getCheckBoxButton().isSelected()) {
                        Book book = new Book(controller.getData());
                        selectedBooks.add(book);
                    }
                } catch (Exception e) {
                    System.out.println("Error getting selected books: " + e.getMessage());
                }
            }
        }

        return selectedBooks;
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
