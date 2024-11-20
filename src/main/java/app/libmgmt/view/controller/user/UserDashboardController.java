package app.libmgmt.view.controller.user;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.EnumUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class UserDashboardController {

    UserNavigationController navigationController = UserNavigationController.getInstance();

    private static UserDashboardController controller;

    @FXML
    private PieChart pieChart;

    @FXML
    private Pane pieChartPane;

    @FXML
    private StackPane stackPaneContainer;

    @FXML
    private Text textQuote;

    @FXML
    private HBox hBoxBorrowedBookList;

    @FXML
    private HBox hBoxAvailableBookInvetory;

    @FXML
    private HBox hBoxReturnedBookList;

    @FXML
    private JFXButton borrowedBookButton;

    @FXML
    private JFXButton availableBookButton;

    @FXML
    private JFXButton returnedBookButton;

    public UserDashboardController() {
        controller = this;
    }

    public static UserDashboardController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        System.out.println("User Dashboard initialized");

        setPieChart();
    }

    private void setPieChart() {
        ObservableList<PieChart.Data> pieChartData = addPieChartData();
        pieChart.getData().clear();
        pieChart.getData().addAll(pieChartData);
        if (pieChart.getData().size() >= 2) {
            pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #3D3E3E;");
            pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #151619;");
        }
        pieChart.setLabelLineLength(0);
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(false);
        pieChart.setStartAngle(90);
        pieChart.setClockwise(true);
    }

    public ObservableList<PieChart.Data> addPieChartData() {
        int totalBorrowedBooks = getTotalBorrowedBooks();
        double percentageBorrowed = 0;
        // if (!booksData.isEmpty()) {
        // percentageBorrowed = ((double) totalBorrowedBooks / booksData.size()) * 100;
        // }
        percentageBorrowed = (double) totalBorrowedBooks / 10 * 100;

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.add(new PieChart.Data("Total Borrowed Books", percentageBorrowed));
        pieChartData.add(new PieChart.Data("Total Returned Books", 100 - percentageBorrowed));
        return pieChartData;
    }

    private int getTotalBorrowedBooks() {
        return 6;
    }

    @FXML
    void btnAvailableBookOnAction(ActionEvent event) throws IOException {
        navigationController.handleNavigation(EnumUtils.NavigationButton.BOOKS, "user-books-layout.fxml", navigationController.getBooksButton());
    }

    @FXML
    void btnBorrowedBookOnAction(ActionEvent event) throws IOException {
        navigationController.handleNavigation(EnumUtils.NavigationButton.CATALOG, "user-catalog-form.fxml", navigationController.getCatalogButton());
    }

    @FXML
    void btnReturnedBookOnAction(ActionEvent event) throws IOException {
        EnumUtils.currentStateUserCatalog = EnumUtils.CATALOG_STATE.RETURNED;
        navigationController.handleNavigation(EnumUtils.NavigationButton.CATALOG, "user-catalog-form.fxml", navigationController.getCatalogButton());
    }

    @FXML
    void btnOnMouseEntered(MouseEvent event) {
        if (event.getSource() == borrowedBookButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.HOVER_SCALE, hBoxBorrowedBookList).play();
        } else if (event.getSource() == availableBookButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.HOVER_SCALE, hBoxAvailableBookInvetory).play();
        } else if (event.getSource() == returnedBookButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.HOVER_SCALE, hBoxReturnedBookList).play();
        }
    }

    @FXML
    void btnOnMouseExited(MouseEvent event) {
        if (event.getSource() == borrowedBookButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, hBoxBorrowedBookList).play();
        } else if (event.getSource() == availableBookButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, hBoxAvailableBookInvetory).play();
        } else if (event.getSource() == returnedBookButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, hBoxReturnedBookList).play();
        }
    }

}
