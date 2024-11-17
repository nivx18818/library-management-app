package app.libmgmt.view.user;

import app.libmgmt.util.AnimationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class UserDashboardController {

    private static UserDashboardController controller;

    @FXML
    private PieChart pieChart;

    @FXML
    private Pane pieChartPane;

    @FXML
    private StackPane stackPaneContainer;

    @FXML
    private Text textQuote;

    public UserDashboardController() {
        controller = this;
    }

    public static UserDashboardController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        System.out.println("User Dashboard initialized");

        AnimationUtils.fadeInRight(stackPaneContainer, 1);

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

    @SuppressWarnings("exports")
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
    void btnAvailableBookOnAction(ActionEvent event) {

    }

    @FXML
    void btnBorrowedBookOnAction(ActionEvent event) {

    }

    @FXML
    void btnBorrowedBookOnMouseEntered(MouseEvent event) {

    }

    @FXML
    void btnBorrowedBookOnMouseExited(MouseEvent event) {

    }

    @FXML
    void btnReturnedBookOnAction(ActionEvent event) {

    }

}
