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

    // Singleton Instance
    private static UserDashboardController controller;
    private final UserNavigationController navigationController = UserNavigationController.getInstance();

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
    private HBox hBoxAvailableBookInventory;

    @FXML
    private HBox hBoxReturnedBookList;

    @FXML
    private JFXButton borrowedBookButton;

    @FXML
    private JFXButton availableBookButton;

    @FXML
    private JFXButton returnedBookButton;

    // Constructor and Singleton Pattern
    public UserDashboardController() {
        controller = this;
    }

    public static UserDashboardController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        System.out.println("User Dashboard initialized");
        setupPieChart();
    }

    /**
     * Initializes and configures the pie chart data.
     */
    private void setupPieChart() {
        ObservableList<PieChart.Data> pieChartData = generatePieChartData();
        pieChart.getData().clear();
        pieChart.getData().addAll(pieChartData);

        // Apply custom styles if pie chart has at least two segments.
        if (pieChartData.size() >= 2) {
            pieChartData.get(0).getNode().setStyle("-fx-pie-color: #3D3E3E;");
            pieChartData.get(1).getNode().setStyle("-fx-pie-color: #151619;");
        }

        // General pie chart configuration
        pieChart.setLabelLineLength(0);
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(false);
        pieChart.setStartAngle(90);
        pieChart.setClockwise(true);
    }

    /**
     * Generates the pie chart data based on book statistics.
     * 
     * @return ObservableList of PieChart.Data with borrowing and return stats.
     */
    public ObservableList<PieChart.Data> generatePieChartData() {
        int totalBorrowedBooks = getTotalBorrowedBooks();
        double percentageBorrowed = (double) totalBorrowedBooks / 10 * 100;

        // ObservableList to hold data for the pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Total Borrowed Books", percentageBorrowed),
            new PieChart.Data("Total Returned Books", 100 - percentageBorrowed)
        );

        return pieChartData;
    }

    /**
     * Placeholder method to get the total number of borrowed books.
     * 
     * @return Total number of borrowed books.
     */
    private int getTotalBorrowedBooks() {
        // Placeholder value; replace with real data retrieval logic.
        return 6;
    }

    @FXML
    private void btnAvailableBookOnAction(ActionEvent event) throws IOException {
        navigateTo(EnumUtils.NavigationButton.BOOKS, "user-books-layout.fxml", navigationController.getBooksButton());
    }

    @FXML
    private void btnBorrowedBookOnAction(ActionEvent event) throws IOException {
        UserCatalogController.currentStateUserCatalog = UserCatalogController.USER_CATALOG_STATE.BORROWED;
        navigateTo(EnumUtils.NavigationButton.CATALOG, "user-catalog-form.fxml", navigationController.getCatalogButton());
    }

    @FXML
    private void btnReturnedBookOnAction(ActionEvent event) throws IOException {
        UserCatalogController.currentStateUserCatalog = UserCatalogController.USER_CATALOG_STATE.RETURNED;
        navigateTo(EnumUtils.NavigationButton.CATALOG, "user-catalog-form.fxml", navigationController.getCatalogButton());
    }

    /**
     * Handles navigation to different layouts.
     * 
     * @param buttonType Enum specifying the navigation button type.
     * @param layout     The FXML layout to load.
     * @param button     The associated navigation button.
     * @throws IOException if the FXML file is not found.
     */
    private void navigateTo(EnumUtils.NavigationButton buttonType, String layout, JFXButton button) throws IOException {
        navigationController.handleNavigation(buttonType, layout, button);
    }

    @FXML
    private void btnOnMouseEntered(MouseEvent event) {
        if (event.getSource() == borrowedBookButton) {
            animateButtonHover(hBoxBorrowedBookList);
        } else if (event.getSource() == availableBookButton) {
            animateButtonHover(hBoxAvailableBookInventory);
        } else if (event.getSource() == returnedBookButton) {
            animateButtonHover(hBoxReturnedBookList);
        }
    }

    @FXML
    private void btnOnMouseExited(MouseEvent event) {
        if (event.getSource() == borrowedBookButton) {
            resetButtonHover(hBoxBorrowedBookList);
        } else if (event.getSource() == availableBookButton) {
            resetButtonHover(hBoxAvailableBookInventory);
        } else if (event.getSource() == returnedBookButton) {
            resetButtonHover(hBoxReturnedBookList);
        }
    }

    /**
     * Animates button hover state with scale transition.
     * 
     * @param target HBox target to animate.
     */
    private void animateButtonHover(HBox target) {
        AnimationUtils.createScaleTransition(AnimationUtils.HOVER_SCALE, target).play();
    }

    /**
     * Resets button hover state with default scale transition.
     * 
     * @param target HBox target to reset animation.
     */
    private void resetButtonHover(HBox target) {
        AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, target).play();
    }
}
