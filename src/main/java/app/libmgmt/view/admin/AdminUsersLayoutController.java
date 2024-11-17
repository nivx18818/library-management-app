package app.libmgmt.view.admin;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
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
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AdminUsersLayoutController {

    private static AdminUsersLayoutController controller;
    private final AdminGlobalController adminGlobalController = AdminGlobalController.getInstance();
    private final List<String[]> studentsData = adminGlobalController
            .getObservableUsersData(EnumUtils.UserType.STUDENT);
    private final List<String[]> guestsData = adminGlobalController.getObservableUsersData(EnumUtils.UserType.GUEST);
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
        showStudentsList();

        // Listen for changes in user data for both students and guests
        listenUserListChange(EnumUtils.UserType.STUDENT);
        listenUserListChange(EnumUtils.UserType.GUEST);
    }

    private void listenUserListChange(EnumUtils.UserType userType) {
        adminGlobalController.getObservableUsersData(userType)
                .addListener((ListChangeListener.Change<? extends String[]> change) -> {
                    while (change.next()) {
                        boolean isUpdate = change.wasRemoved() && change.getRemovedSize() == change.getAddedSize();

                        if (change.wasRemoved() && !isUpdate) {
                            System.out.println("User removed");

                            for (String[] removeUser : change.getRemoved()) {
                                String userId = removeUser[4];
                                removeUserFromVBox(userId);
                            }
                        }
                    }
                });
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
                        scene.setId(d[4]);

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
        String[] formattedData = new String[] { data[4], data[1], data[2], data[3] };

        switch (path) {
            case "admin-users-student-bar.fxml" -> {
                AdminUsersStudentBarController controller = fxmlLoader.getController();
                controller.setData(formattedData);
            }

            case "admin-users-guest-bar.fxml" -> {
                AdminUsersGuestBarController controller = fxmlLoader.getController();
                controller.setData(formattedData);
            }

            default -> throw new IllegalArgumentException("Unknown FXML path: " + path);
        }

        return scene;
    }

    private void removeUserFromVBox(String userId) {
        for (int i = 0; i < vBoxUserList.getChildren().size(); i++) {
            if (vBoxUserList.getChildren().get(i).getId() != null
                    && vBoxUserList.getChildren().get(i).getId().equals(userId)) {
                int finalI = i;
                Platform.runLater(() -> vBoxUserList.getChildren().remove(finalI));
                return;
            }
        }
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

    public List<String[]> getGuestsData() {
        return guestsData;
    }

    public enum PreloadType {
        RESET, ADD
    }

}
