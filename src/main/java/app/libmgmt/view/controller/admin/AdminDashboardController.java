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
import app.libmgmt.model.Admin;
import app.libmgmt.model.Loan;

import java.io.IOException;
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
    private List<Loan> borrowedBooksData = adminGlobalController.getBorrowedBooksData();
    private List<Admin> adminData = adminGlobalController.getAdminData();

    public AdminDashboardController() {
        controller = this;
    }

    public static AdminDashboardController getInstance() {
        return controller;
    }

    public void initialize() {
        System.out.println("Dashboard initialized");

        // TODO: Set the total book and user count from database
        totalBook.setText(String.valueOf(AdminGlobalController.getInstance().getTotalBooksCount()));
        totalUser.setText(String.valueOf(AdminGlobalController.getInstance().getTotalUsersCount()));

        setPieChart();
        getOverdueData();
        getVBoxAdmin();
    }

    public ObservableList<PieChart.Data> addPieChartData() {
        int totalBorrowedBooks = AdminGlobalController.getInstance().getTotalBorrowedBooks();
        double percentageBorrowed = 0;
        int totalBooks = Integer.parseInt(totalBook.getText());
        if (totalBooks != 0) {
            percentageBorrowed = ((double) totalBorrowedBooks / totalBooks) * 100;
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.add(new PieChart.Data("Total Borrowed Books", percentageBorrowed));
        pieChartData.add(new PieChart.Data("Total Available Books", 100 - percentageBorrowed));
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
        int res = borrowedBooksData.size();

        return res;
    }

    public void getOverdueData() {
        List<Loan> OverdueData = adminGlobalController.getOverDueLoans();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (Loan borrowedData : OverdueData) {
                    //cover url, name, author, due_date
                    Platform.runLater(() -> loadOverdueDataTable(borrowedData));
                    Thread.sleep(50);
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

    public void loadOverdueDataTable(Loan data) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminDashboardController.class.getResource(
                    "/fxml/admin/admin-dashboard-overdue-bar.fxml"));

            Parent scene = fxmlLoader.load();

            AdminDashboardOverdueBarController controller = fxmlLoader.getController();
            controller.setData(data);

            vBoxOverdue.getChildren().add(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getVBoxAdmin() {
        for (Admin data : adminData) {
            String name = data.getName();
            String email = data.getEmail();
            String username = data.getUserId();

            loadAdminDataTable(name, email, username);
        }
    }

    public void loadAdminDataTable(String name, String email, String username) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminDashboardController.class.getResource(
                    "/fxml/bookworm-admin-bar.fxml"));

            Parent scene = fxmlLoader.load();

            BookwormAdminBarController controller = fxmlLoader.getController();
            controller.setData(name, email, username);

            vBoxAdmin.getChildren().add(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Admin> getAdminData() {
        return adminData;
    }

}
