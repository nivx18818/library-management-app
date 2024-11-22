package app.libmgmt.view.controller.admin;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import app.libmgmt.model.Book;
import app.libmgmt.util.DateUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AdminDashboardController {

    private static AdminDashboardController controller;
    @FXML
    public StackPane stackPaneContainer;
    AdminGlobalController adminGlobalController = AdminGlobalController.getInstance();
    @FXML
    private PieChart pieChart;
    @FXML
    private VBox vBoxOverdue;
    @FXML
    private Text totalBook;
    @FXML
    private Text totalUser;
    @FXML
    private VBox vBoxAdmin;
    private List<String[]> borrowedBooksData = adminGlobalController.getBorrowedBooksData();
    private List<Book> booksData = adminGlobalController.getObservableBookData(); // TODO: Can be deleted after using database
    private List<String[]> adminData = adminGlobalController.getAdminData();

    public AdminDashboardController() {
        controller = this;
    }

    public static AdminDashboardController getInstance() {
        return controller;
    }

    public void initialize() {
        System.out.println("Dashboard initialized");

        // TODO: Set the total book and user count from database
        totalBook.setText(String.valueOf(booksData.size()));
        totalUser.setText(String.valueOf(AdminGlobalController.getInstance().getTotalUsersCount()));

        setPieChart();
        getOverdueData();
        getVBoxAdmin();
    }

    public ObservableList<PieChart.Data> addPieChartData() {
        int totalBorrowedBooks = getTotalBorrowedBooks();
        double percentageBorrowed = 0;
        int totalBooks = Integer.parseInt(totalBook.getText());
        if (totalBooks != 0) {
            percentageBorrowed = ((double) totalBorrowedBooks / totalBooks) * 100;
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.add(new PieChart.Data("Total Borrowed Books", percentageBorrowed));
        pieChartData.add(new PieChart.Data("Total Returned Books", 100 - percentageBorrowed));
        return pieChartData;
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

    public int getTotalBorrowedBooks() {
        int res = 0;

        for (String[] data : borrowedBooksData) {
            res += Integer.parseInt(data[2]);
        }

        return res;
    }

    public void getOverdueData() {
        // TODO: Replaceable by filtering from database
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (String[] borrowedData : borrowedBooksData) {
                    String dueDate = borrowedData[3];

                    LocalDate dueDateParsed = LocalDate.parse(dueDate, DateUtils.dateTimeFormatter);

                    if (dueDateParsed.isBefore(DateUtils.currentLocalTime)) {
                        String name = borrowedData[0];
                        String id = borrowedData[1];

                        Platform.runLater(() -> loadOverdueDataTable(name, id));
                        Thread.sleep(50);
                    }
                }

                return null;
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
            }
        };

        new Thread(task).start();
    }

    public void loadOverdueDataTable(String idText, String uidText) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminDashboardController.class.getResource(
                    "/fxml/admin/admin-dashboard-overdue-bar.fxml"));

            Parent scene = fxmlLoader.load();

            AdminDashboardOverdueBarController controller = fxmlLoader.getController();
            controller.setData(idText, uidText);

            vBoxOverdue.getChildren().add(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getVBoxAdmin() {
        for (String[] data : adminData) {
            String name = data[0];
            String email = data[1];

            loadAdminDataTable(name, email);
        }
    }

    public void loadAdminDataTable(String name, String email) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminDashboardController.class.getResource(
                    "/fxml/bookworm-admin-bar.fxml"));

            Parent scene = fxmlLoader.load();

            BookwormAdminBarController controller = fxmlLoader.getController();
            controller.setData(name, email);

            vBoxAdmin.getChildren().add(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String[]> getAdminData() {
        return adminData;
    }

}
