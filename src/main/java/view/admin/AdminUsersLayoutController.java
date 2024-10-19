package view.admin;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import util.Animation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AdminUsersLayoutController {

    private static AdminUsersLayoutController controller;
    private final AdminGlobalFormController adminGlobalFormController =
            AdminGlobalFormController.getInstance();
    @FXML
    private HBox hBoxGuest;
    @FXML
    private HBox hBoxStudent;
    @FXML
    private Pane studentPane;
    @FXML
    private Label studentLabel;
    @FXML
    private Pane guestPane;
    @FXML
    private Label guestLabel;
    @FXML
    private VBox vBoxUserList;
    @FXML
    private StackPane stackPaneContainer;
    private List<String[]> studentsData = new ArrayList<>();
    private List<String[]> guestsData = new ArrayList<>();
    private String status = "student";

    public AdminUsersLayoutController() {
        controller = this;
    }

    public static AdminUsersLayoutController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);
        System.out.println("Initialize Catalog Layout");

        setVisibility(true, false);
        setStudentsData(adminGlobalFormController.getUsersData());
        setGuestsData(adminGlobalFormController.getUsersData());
        showStudentsList();
    }

    public void setVisibility(boolean visibilityStudent, boolean visibilityGuest) {
        hBoxStudent.setVisible(visibilityStudent);
        hBoxGuest.setVisible(visibilityGuest);
    }

    private void showStudentsList() {
        vBoxUserList.getChildren().clear();
        preloadData(studentsData, "admin-users-student-bar.fxml", "reset");
    }

    public void preloadData(List<String[]> allUsersData, String path, String resetOrAdd) {
        if (!vBoxUserList.getChildren().isEmpty() && resetOrAdd.equals("reset")) {
            vBoxUserList.getChildren().clear();
        }

        Task<Void> preloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (String[] d : allUsersData) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(AdminCatalogBorrowedBooksLayout.class.getResource(
                                "/fxml/" + path));

                        Pane scene = fxmlLoader.load();

                        switch (path) {
                            case "admin-users-student-bar.fxml": {
                                AdminUsersStudentBarController controller = fxmlLoader.getController();
                                controller.setData(d[4], d[1], d[2], d[3]);
                                break;
                            }
                            case "admin-users-guest-bar.fxml": {
                                AdminUsersGuestBarController controller = fxmlLoader.getController();
                                controller.setData(d[4], d[1], d[2], d[3]);
                                break;
                            }
                        }

                        Platform.runLater(() -> vBoxUserList.getChildren().add(scene));
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
    void studentButtonOnAction(ActionEvent event) {
        if (status.equals("student")) {
            return;
        }
        status = "student";
        setDefaultStyle();
        studentPane.setStyle("-fx-background-color: #E3E3E3; -fx-background-radius: 12px;");
        studentLabel.setStyle("-fx-text-fill: black;");
        setVisibility(true,  false);
        Animation.zoomIn(hBoxStudent, 1.0);
        showStudentsList();
    }

    @FXML
    void guestButtonOnAction(ActionEvent event) {
        if (status.equals("guest")) {
            return;
        }
        status = "guest";
        setDefaultStyle();
        guestPane.setStyle("-fx-background-color: #E3E3E3; -fx-background-radius: 12px;");
        guestLabel.setStyle("-fx-text-fill: black;");
        setVisibility(false, true);
        Animation.zoomIn(hBoxGuest, 1.0);
        showGuestsList();
    }

    private void showGuestsList() {
        vBoxUserList.getChildren().clear();
        preloadData(guestsData, "admin-users-guest-bar.fxml", "reset");
    }

    @FXML
    void addUserButtonOnAction(ActionEvent event) {
        Platform.runLater(() -> {
            AdminNavigationController.openPopUp(stackPaneContainer, "/fxml/admin-add-user-dialog.fxml");
        });
    }

    @FXML
    void btnRefreshTableOnAction(ActionEvent event) {
        if (status.equals("student")) {
            showStudentsList();
        } else {
            showGuestsList();
        }
    }

    @FXML
    void btnRefreshTableOnMouseEntered(MouseEvent event) {

    }

    @FXML
    void btnRefreshTableOnMouseExited(MouseEvent event) {

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {

    }

    @FXML
    void txtSearchOnMouseMoved(MouseEvent event) {

    }

    public void setDefaultStyle() {
        studentPane.setStyle("-fx-background-color: #000; -fx-background-radius: 12px;");
        studentLabel.setStyle("-fx-text-fill: #fff");
        guestPane.setStyle("-fx-background-color: #000; -fx-background-radius: 12px;");
        guestLabel.setStyle("-fx-text-fill: #fff");
    }

    public void setStudentsData(List<String[]> usersData) {
        for (String[] d : usersData) {
            if (d[0].equals("Student")) {
                studentsData.add(d);
            }
        }
    }

    public void setGuestsData(List<String[]> usersData) {
        for (String[] d : usersData) {
            if (d[0].equals("External Borrower")) {
                guestsData.add(d);
            }
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<String[]> getStudentsData() {
        return studentsData;
    }

    public List<String[]> getGuestsData() {
        return guestsData;
    }

}
