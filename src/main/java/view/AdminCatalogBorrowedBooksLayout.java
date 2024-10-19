package view;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.Animation;
import util.DateTimeUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class AdminCatalogBorrowedBooksLayout {


    AdminGlobalFormController adminGlobalFormController = AdminGlobalFormController.getInstance();
    private final List<String[]> borrowedBooksData = adminGlobalFormController.getBorrowedBooksData();
    private final List<String[]> overdueData = loadOverdueBorrowersList();
    @FXML
    private JFXButton borrowedBooksButton;
    @FXML
    private Label borrowedBooksLabel;
    @FXML
    private Pane borrowedBooksPane;
    @FXML
    private JFXButton overdueBorrowersButton;
    @FXML
    private Label overdueBorrowersLabel;
    @FXML
    private Pane overdueBorrowersPane;
    @FXML
    private TextField textSearch;
    @FXML
    private VBox vBoxBorrowedBooks;
    private String status = "borrowed";

    @FXML
    public void initialize() {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);
        System.out.println("Initialize Catalog Layout");

        showBorrowedBooksList();
    }


    public void preloadData(List<String[]> data) {
        Task<Void> preloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (String[] d : data) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(AdminCatalogBorrowedBooksLayout.class.getResource(
                                "/fxml/admin-borrowed-book-bar.fxml"));

                        Pane scene = fxmlLoader.load();

                        AdminBorrowedBooksBar controller = fxmlLoader.getController();
                        controller.setData(d[0], d[1], Integer.parseInt(d[2]), d[3], d[4]);

                        Platform.runLater(() -> vBoxBorrowedBooks.getChildren().add(scene));
                        Animation.zoomIn(scene, 1.0);
                        Thread.sleep(10);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

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

    @FXML
    void btnOverdueBorrowersOnAction(ActionEvent event) {
        if (event.getSource() == overdueBorrowersButton) {
            setDefaultStyle();
            overdueBorrowersLabel.setStyle("-fx-text-fill: black;");
            overdueBorrowersPane.setStyle("-fx-background-color: #E3E3E3; -fx-background-radius: "
                    + "12px;");

            if (!status.equals("overdue")) {
                showOverdueBorrowersList();
            }

            status = "overdue";
        } else if (event.getSource() == borrowedBooksButton) {
            setDefaultStyle();
            borrowedBooksLabel.setStyle("-fx-text-fill: black;");
            borrowedBooksPane.setStyle("-fx-background-color: #E3E3E3; -fx-background-radius: "
                    + "12px;");

            if (!status.equals("borrowed")) {
                showBorrowedBooksList();
            }

            status = "borrowed";
        }
    }

    public void setDefaultStyle() {
        overdueBorrowersLabel.setStyle("-fx-text-fill: white;");
        borrowedBooksLabel.setStyle("-fx-text-fill: white;");
        overdueBorrowersPane.setStyle("-fx-background-color: black; -fx-background-radius: 12px;");
        borrowedBooksPane.setStyle("-fx-background-color: black; -fx-background-radius: 12px;");
    }

    public void showBorrowedBooksList() {
        vBoxBorrowedBooks.getChildren().clear();
        preloadData(borrowedBooksData);
    }

    public List<String[]> loadOverdueBorrowersList() {

        List<String[]> overdueList = new ArrayList<>();

        for (String[] borrowedData : borrowedBooksData) {
            String dueDate = borrowedData[3];

            LocalDate dueDateParsed = LocalDate.parse(dueDate, DateTimeUtils.dateTimeFormatter);

            try {
                if (dueDateParsed.isBefore(DateTimeUtils.currentLocalTime)) {
                    overdueList.add(borrowedData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return overdueList;
    }

    public void showOverdueBorrowersList() {
        vBoxBorrowedBooks.getChildren().clear();
        preloadData(overdueData);
    }

    @FXML
    void btnOverdueBorrowersOnMouseEntered(MouseEvent event) {

    }

    @FXML
    void btnOverdueBorrowersOnMouseExited(MouseEvent event) {

    }

    @FXML
    void btnRefreshTableOnAction(ActionEvent event) {
        if (status.equals("borrowed")) {
            showBorrowedBooksList();
        } else if (status.equals("overdue")) {
            showOverdueBorrowersList();
        }

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
            if (status.equals("borrowed")) {
                showBorrowedBooksList();
            } else if (status.equals("overdue")) {
                showOverdueBorrowersList();
            }
        }

        showFilteredData(text);
        textSearch.setEditable(false);
    }

    public void showFilteredData(String text) {
        List<HBox> filteredList = new ArrayList<>();

        Predicate<String> checkType = (input) -> input.matches(".*\\d.*");

        boolean type = checkType.test(text);

        int dataSize = vBoxBorrowedBooks.getChildren().size();

        for (int i = 0; i < dataSize; i++) {
            HBox data = (HBox) vBoxBorrowedBooks.getChildren().get(i);

            String nameOrID = type ? ((Label) data.getChildren().get(1)).getText() : ((Label) data.getChildren().getFirst()).getText();
            if (nameOrID.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(data);
            }
        }

        vBoxBorrowedBooks.getChildren().clear();
        vBoxBorrowedBooks.getChildren().addAll(filteredList);
    }

    @FXML
    void txtSearchOnMouseMoved(MouseEvent event) {

    }
}
