package app.libmgmt.view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.DateTimeUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AdminBorrowedBooksLayoutController {

    enum STATE {
        BORROWED, OVERDUE
    }

    // Singleton instance of the controller
    private static AdminBorrowedBooksLayoutController controller;

    @FXML
    private StackPane stackPaneContainer;
    @FXML
    private VBox vBoxBorrowedBooks;
    @FXML
    private TextField textSearch;
    @FXML
    private JFXButton borrowedBooksButton;
    @FXML
    private JFXButton overdueBorrowersButton;
    @FXML
    private Label borrowedBooksLabel;
    @FXML
    private Label overdueBorrowersLabel;
    @FXML
    private Pane borrowedBooksPane;
    @FXML
    private Pane overdueBorrowersPane;

    // External controllers and data
    private final AdminGlobalController adminGlobalController = AdminGlobalController.getInstance();
    private final List<String[]> borrowedBooksData = adminGlobalController.getBorrowedBooksData();
    private final List<String[]> overdueData = loadOverdueBorrowersList();
    private STATE status = STATE.BORROWED;

    // Constructor and Singleton Access
    public AdminBorrowedBooksLayoutController() {
        controller = this;
    }

    public static AdminBorrowedBooksLayoutController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);
        showBorrowedBooksList();
    }

    // Data Preloading
    public void preloadData(List<String[]> data) {
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (String[] d : data) {
                    loadBorrowedBookBar(d);
                    Thread.sleep(10);
                }
                return null;
            }

            @Override
            protected void failed() {
                System.out.println("Error during data table loading: " + getException().getMessage());
            }
        };
        new Thread(preloadTask).start();
    }

    // Load a single borrowed book bar
    private void loadBorrowedBookBar(String[] d) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/admin-borrowed-book-bar.fxml"));
            Pane scene = fxmlLoader.load();
            AdminBorrowedBooksBarController controller = fxmlLoader.getController();
            controller.setData(d[0], d[1], Integer.parseInt(d[2]), d[3], d[4]);

            // Add to VBox and animate
            Platform.runLater(() -> {
                vBoxBorrowedBooks.getChildren().add(scene);
                AnimationUtils.zoomIn(scene, 1.0);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // UI Update Methods
    private void setDefaultStyle() {
        overdueBorrowersLabel.setStyle("-fx-text-fill: black;");
        borrowedBooksLabel.setStyle("-fx-text-fill: black;");
        overdueBorrowersPane.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 12px;");
        borrowedBooksPane.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 12px;");
    }

    private void updateStatusUI(STATE newStatus) {
        setDefaultStyle();
        if (newStatus == STATE.OVERDUE) {
            overdueBorrowersLabel.setStyle("-fx-text-fill: white;");
            overdueBorrowersPane.setStyle("-fx-background-color: #000; -fx-background-radius: 12px;");
        } else {
            borrowedBooksLabel.setStyle("-fx-text-fill: white;");
            borrowedBooksPane.setStyle("-fx-background-color: #000; -fx-background-radius: 12px;");
        }
        status = newStatus;
    }

    // Data Display
    public void showBorrowedBooksList() {
        vBoxBorrowedBooks.getChildren().clear();
        preloadData(borrowedBooksData);
    }

    public void showOverdueBorrowersList() {
        vBoxBorrowedBooks.getChildren().clear();
        preloadData(overdueData);
    }

    private List<String[]> loadOverdueBorrowersList() {
        List<String[]> overdueList = new ArrayList<>();
        for (String[] borrowedData : borrowedBooksData) {
            try {
                LocalDate dueDateParsed = LocalDate.parse(borrowedData[3], DateTimeUtils.dateTimeFormatter);
                if (dueDateParsed.isBefore(DateTimeUtils.currentLocalTime)) {
                    overdueList.add(borrowedData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return overdueList;
    }

    // Event Handlers
    @FXML
    void btnOverdueBorrowersOnAction(ActionEvent event) {
        if (event.getSource() == overdueBorrowersButton && !(status == STATE.OVERDUE)) {
            updateStatusUI(STATE.OVERDUE);
            showOverdueBorrowersList();
        } else if (event.getSource() == borrowedBooksButton && !(status == STATE.BORROWED)) {
            updateStatusUI(STATE.BORROWED);
            showBorrowedBooksList();
        }
    }

    @FXML
    void btnRefreshTableOnAction(ActionEvent event) {
        if (status == STATE.BORROWED) {
            showBorrowedBooksList();
        } else if (status == STATE.OVERDUE) {
            showOverdueBorrowersList();
        }
        textSearch.clear();
        textSearch.setEditable(true);
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String searchText = textSearch.getText();
        if (searchText.isEmpty()) {
            if (status == STATE.BORROWED) {
                showBorrowedBooksList();
            } else if (status == STATE.OVERDUE) {
                showOverdueBorrowersList();
            }
        } else {
            showFilteredData(searchText);
        }
        textSearch.setEditable(false);
    }

    // Filtering Logic
    public void showFilteredData(String searchText) {
        List<HBox> filteredList = new ArrayList<>();
        boolean isNumericSearch = searchText.matches(".*\\d.*");

        for (var node : vBoxBorrowedBooks.getChildren()) {
            if (node instanceof HBox data) {
                String identifier = isNumericSearch
                        ? ((Label) data.getChildren().get(1)).getText()
                        : ((Label) data.getChildren().get(0)).getText();

                if (identifier.toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(data);
                }
            }
        }
        vBoxBorrowedBooks.getChildren().setAll(filteredList);
    }

    public StackPane getStackPaneContainer() {
        return stackPaneContainer;
    }
}
