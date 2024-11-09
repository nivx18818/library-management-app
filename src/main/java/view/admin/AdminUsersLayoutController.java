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
import util.AnimationUtils;
import util.ChangeScene;
import util.EnumUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AdminUsersLayoutController {

    private static AdminUsersLayoutController controller;
    private final AdminGlobalFormController adminGlobalFormController =
            AdminGlobalFormController.getInstance();
    private final List<String[]> studentsData = new ArrayList<>();
    private final List<String[]> guestsData = new ArrayList<>();
    @FXML
    public StackPane stackPaneContainer;
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
    private EnumUtils.UserType status = EnumUtils.UserType.STUDENT;
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

    public void preloadData(List<String[]> allUsersData, String path, PreloadType preloadType) {
        if (!vBoxUserList.getChildren().isEmpty() && preloadType == PreloadType.RESET) {
            vBoxUserList.getChildren().clear();
        }

        Task<Void> preloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (String[] d : allUsersData) {
                    try {
                        Pane scene = loadScene(path, d);

                        Platform.runLater(() -> vBoxUserList.getChildren().add(scene));
                        AnimationUtils.zoomIn(scene, 1.0);

                    } catch (IOException e) {
                        throw new RuntimeException("Error loading FXML: " + e.getMessage(), e);
                    }
                    Thread.sleep(10); // Optional delay for effect
                }
                return null;
            }

            @Override
            protected void failed() {
                System.err.println("Error during data table loading: " + getException().getMessage());
            }
        };

        new Thread(preloadTask).start();
    }

    private Pane loadScene(String path, String[] data) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + path));
        Pane scene = fxmlLoader.load();

        switch (path) {
            case "admin-users-student-bar.fxml" -> {
                AdminUsersStudentBarController controller = fxmlLoader.getController();
                controller.setData(data[4], data[1], data[2], data[3]);
            }
            case "admin-users-guest-bar.fxml" -> {
                AdminUsersGuestBarController controller = fxmlLoader.getController();
                controller.setData(data[4], data[1], data[2], data[3]);
            }
            default -> throw new IllegalArgumentException("Unknown FXML path: " + path);
        }
        return scene;
    }

    @FXML
    void studentButtonOnAction(ActionEvent event) {
        if (status == EnumUtils.UserType.STUDENT) {
            return;
        }
        status = EnumUtils.UserType.STUDENT;
        setDefaultStyle();
        studentPane.setStyle("-fx-background-color: #E3E3E3; -fx-background-radius: 12px;");
        studentLabel.setStyle("-fx-text-fill: black;");
        setVisibility(true, false);
        AnimationUtils.zoomIn(hBoxStudent, 1.0);
        showStudentsList();
    }

    public void showStudentsList() {
        vBoxUserList.getChildren().clear();
        preloadData(studentsData, "admin-users-student-bar.fxml", PreloadType.RESET);
    }

    @FXML
    void guestButtonOnAction(ActionEvent event) {
        if (status == EnumUtils.UserType.GUEST) {
            return;
        }
        status = EnumUtils.UserType.GUEST;
        setDefaultStyle();
        guestPane.setStyle("-fx-background-color: #E3E3E3; -fx-background-radius: 12px;");
        guestLabel.setStyle("-fx-text-fill: black;");
        setVisibility(false, true);
        AnimationUtils.zoomIn(hBoxGuest, 1.0);
        showGuestsList();
    }

    private void showGuestsList() {
        vBoxUserList.getChildren().clear();
        preloadData(guestsData, "admin-users-guest-bar.fxml", PreloadType.RESET);
    }

    @FXML
    void addUserButtonOnAction(ActionEvent event) {
        Platform.runLater(() -> {
            ChangeScene.openAdminPopUp(stackPaneContainer, "/fxml/admin-add-user-dialog.fxml");
        });
    }

    @FXML
    void btnRefreshTableOnAction(ActionEvent event) {
        refreshUsersList();
    }

    public void refreshUsersList() {
        vBoxUserList.getChildren().clear();
        if (status == EnumUtils.UserType.STUDENT) {
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

    public EnumUtils.UserType getStatus() {
        return status;
    }

    public List<String[]> getStudentsData() {
        return studentsData;
    }

    public void setStudentsData(List<String[]> usersData) {
        for (String[] d : usersData) {
            if (d[0].equals("Student")) {
                studentsData.add(d);
            }
        }
    }

    public List<String[]> getGuestsData() {
        return guestsData;
    }

    public void setGuestsData(List<String[]> usersData) {
        for (String[] d : usersData) {
            if (d[0].equals("External Borrower")) {
                guestsData.add(d);
            }
        }
    }

    public void deleteUserDataById(String id) {
        for (String[] data : studentsData) {
            if (data[4].equals(id)) {
                studentsData.remove(data);
                return;
            }
        }

        for (String[] data : guestsData) {
            if (data[4].equals(id)) {
                guestsData.remove(data);
                return;
            }
        }
    }

    public enum PreloadType {
        RESET, ADD
    }

}
