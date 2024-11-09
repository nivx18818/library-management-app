package view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.AnimationUtils;
import util.ChangeScene;

import java.util.List;

public class AdminBorrowedBookViewDialogController {

    AdminBorrowedBooksBarController adminBorrowedBooksBar = AdminBorrowedBooksBarController.getInstance();
    @FXML
    private Pane closePane;
    @FXML
    private Label closeLabel;
    @FXML
    private Label lblId;
    @FXML
    private Label lblTotalBooks;
    @FXML
    private VBox vBox;
    @FXML
    private JFXButton closeButton;
    private List<String[]> data = setExampleData();

    public void initialize() {
        System.out.println("AdminBorrowedBookViewDialogController initialized");
        setExampleData();
        preLoadData();
    }

    public List<String[]> setExampleData() {
        String[] exampleData = {"https://th.bing.com/th/id/OIP" +
                ".aQ3e1NxnNQVFCXiJJesFZwDMEx?rs=1&pid=ImgDetMain", "Book Title", "Author", "2021-01-01"};
        String[] exampleData1 = {"https://th.bing.com/th/id/OIP" +
                ".aQ3e1NxnNQVFCXiJJesFZwDMEx?rs=1&pid=ImgDetMain", "Book Title", "Author", "2021-01-01"};
        String[] exampleData2 = {"https://th.bing.com/th/id/OIP" +
                ".aQ3e1NxnNQVFCXiJJesFZwDMEx?rs=1&pid=ImgDetMain", "Book Title", "Author", "2021-01-01"};
        List<String[]> data = List.of(exampleData, exampleData1, exampleData2);
        return data;
    }

    private void preLoadData() {
        // TODO: Load data from database
        Task<Void> preloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (String[] d : data) {
                    FXMLLoader fxmlLoader = new FXMLLoader(AdminBooksLayoutController.class.getResource(
                            "/fxml/admin-borrowed-book-view-bar.fxml"));
                    Pane scene = fxmlLoader.load();
                    AdminBorrowedBookViewBarController controller = fxmlLoader.getController();
                    controller.setData(d[0], d[1], d[2], d[3]);
                    Platform.runLater(() -> {
                        vBox.getChildren().add(scene);
                        AnimationUtils.zoomIn(scene, 1.0);
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
    void btnCloseOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void btnCloseOnMouseEntered(MouseEvent event) {
        closePane.setStyle("-fx-background-color: #d7d7d7; -fx-background-radius: 12px");
        closeLabel.setStyle("-fx-text-fill: #000000");
    }

    @FXML
    void btnCloseOnMouseExited(MouseEvent event) {
        closePane.setStyle("-fx-background-color: #000000; -fx-background-radius: 12px");
        closeLabel.setStyle("-fx-text-fill: #ffffff");
    }

    public void setId(String id) {
        lblId.setText(id);
    }

}
