package app.libmgmt.view.controller.admin;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import app.libmgmt.model.ExternalBorrower;
import app.libmgmt.model.Student;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AdminUsersLayoutController {

    private static AdminUsersLayoutController controller;
    private final AdminGlobalController adminGlobalController = AdminGlobalController.getInstance();
    private final List<Student> studentsData = adminGlobalController.getObservableStudentsData();
    private final List<ExternalBorrower> guestsData = adminGlobalController.getObservableExternalBorrowersData();
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
    @FXML
    private TextField textSearch;

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
        switch (userType) {
            case STUDENT:
                adminGlobalController.getObservableStudentsData()
                        .addListener((ListChangeListener.Change<? extends Student> change) -> {
                            while (change.next()) {
                                boolean isUpdate = change.wasRemoved()
                                        && change.getRemovedSize() == change.getAddedSize();

                                if (change.wasRemoved() && !isUpdate) {
                                    System.out.println("Student removed");

                                    for (Student removeUser : change.getRemoved()) {
                                        String userId = removeUser.getStudentId();
                                        removeUserFromVBox(userId);
                                    }
                                }
                            }
                        });
                break;
            case GUEST:
                adminGlobalController.getObservableExternalBorrowersData()
                        .addListener((ListChangeListener.Change<? extends ExternalBorrower> change) -> {
                            while (change.next()) {
                                boolean isUpdate = change.wasRemoved()
                                        && change.getRemovedSize() == change.getAddedSize();

                                if (change.wasRemoved() && !isUpdate) {
                                    System.out.println("Guest removed");

                                    for (ExternalBorrower removeUser : change.getRemoved()) {
                                        String userId = removeUser.getSocialId();
                                        removeUserFromVBox(userId);
                                    }
                                }
                            }
                        });
                break;
            default:
                break;
        }
    }

    public void setVisibility(boolean visibilityStudent, boolean visibilityGuest) {
        hBoxStudent.setVisible(visibilityStudent);
        hBoxGuest.setVisible(visibilityGuest);
    }

    public void preloadExternalBorrowerData(List<ExternalBorrower> allUsersData, String path, PreloadType preloadType) {
        if (!vBoxUserList.getChildren().isEmpty() && preloadType == PreloadType.RESET) {
            vBoxUserList.getChildren().clear();
        }

        Task<Void> preloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (ExternalBorrower externalBorrower : allUsersData) {
                    try {
                        String[] externalBorrowerData = new String[] {
                                externalBorrower.getUserRole(),
                                externalBorrower.getName(),
                                externalBorrower.getPhoneNumber(),
                                externalBorrower.getEmail(),
                                externalBorrower.getSocialId()
                        };

                        Pane scene = loadScene(path, externalBorrowerData);
                        scene.setId(externalBorrower.getSocialId());

                        Platform.runLater(() -> vBoxUserList.getChildren().add(scene));
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

    public void preLoadStudentsData(List<Student> students, String path, PreloadType preloadType) {
        if (!vBoxUserList.getChildren().isEmpty() && preloadType == PreloadType.RESET) {
            vBoxUserList.getChildren().clear();
        }

        Task<Void> preloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (Student student : students) {
                    try {
                        String[] studentData = new String[] {
                                student.getUserRole(),
                                student.getName(),
                                student.getMajor(),
                                student.getEmail(),
                                student.getStudentId()
                        };

                        Pane scene = loadScene(path, studentData);
                        scene.setId(student.getStudentId());

                        Platform.runLater(() -> vBoxUserList.getChildren().add(scene));
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/admin/" + path));
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
        studentPane.setStyle("-fx-background-color: #000; -fx-background-radius: 12px;");
        studentLabel.setStyle("-fx-text-fill: white;");
        setVisibility(true, false);
        showStudentsList();
    }

    public void showStudentsList() {
        vBoxUserList.getChildren().clear();
        preLoadStudentsData(studentsData, "admin-users-student-bar.fxml", PreloadType.RESET);
    }

    @FXML
    void guestButtonOnAction(ActionEvent event) {
        if (status == EnumUtils.UserType.GUEST) {
            return;
        }

        status = EnumUtils.UserType.GUEST;
        setDefaultStyle();
        guestPane.setStyle("-fx-background-color: #000; -fx-background-radius: 12px;");
        guestLabel.setStyle("-fx-text-fill: white;");
        setVisibility(false, true);
        showGuestsList();
    }

    private void showGuestsList() {
        vBoxUserList.getChildren().clear();
        preloadExternalBorrowerData(guestsData, "admin-users-guest-bar.fxml", PreloadType.RESET);
    }

    @FXML
    void addUserButtonOnAction(ActionEvent event) {
        Platform.runLater(() -> {
            ChangeScene.openAdminPopUp(stackPaneContainer, "/fxml/admin/admin-add-user-dialog.fxml",
                    EnumUtils.PopupList.ADD_USER);
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
        String searchText = textSearch.getText();
        if (searchText.isEmpty()) {
            if (status == EnumUtils.UserType.STUDENT) {
                showStudentsList();
            } else if (status == EnumUtils.UserType.GUEST) {
                showGuestsList();
            }
        } else {
            showFilteredData(searchText);
        }
        textSearch.setEditable(true);
    }

    // Filtering Logic
    public void showFilteredData(String searchText) {
        vBoxUserList.getChildren().clear();
        if (status == EnumUtils.UserType.STUDENT) {
            adminGlobalController.getObservableStudentsData().stream()
                    .filter(student -> student.getName().toLowerCase().contains(searchText.toLowerCase()))
                    .forEach(student -> {
                        try {
                            String[] studentData = new String[] {
                                    student.getUserRole(),
                                    student.getName(),
                                    student.getMajor(),
                                    student.getEmail(),
                                    student.getStudentId()
                            };
                            Pane scene = loadScene("admin-users-student-bar.fxml", studentData);
                            scene.setId(student.getStudentId());

                            Platform.runLater(() -> vBoxUserList.getChildren().add(scene));
                        } catch (IOException e) {
                            throw new RuntimeException("Error loading FXML: " + e.getMessage(), e);
                        }

                    });
        } else {
            adminGlobalController.getObservableExternalBorrowersData().stream()
                    .filter(externalBorrower -> externalBorrower.getName().toLowerCase()
                            .contains(searchText.toLowerCase()))
                    .forEach(externalBorrower -> {
                        try {
                            String[] externalBorrowerData = new String[] {
                                    externalBorrower.getUserRole(),
                                    externalBorrower.getName(),
                                    externalBorrower.getPhoneNumber(),
                                    externalBorrower.getEmail(),
                                    externalBorrower.getSocialId()
                            };
                            Pane scene = loadScene("admin-users-guest-bar.fxml", externalBorrowerData);
                            scene.setId(externalBorrower.getSocialId());

                            Platform.runLater(() -> vBoxUserList.getChildren().add(scene));
                        } catch (IOException e) {
                            throw new RuntimeException("Error loading FXML: " + e.getMessage(), e);
                        }
                    });
        }
    }

    @FXML
    void txtSearchOnMouseMoved(MouseEvent event) {

    }

    public void setDefaultStyle() {
        studentPane.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 12px;");
        studentLabel.setStyle("-fx-text-fill: #000");
        guestPane.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 12px;");
        guestLabel.setStyle("-fx-text-fill: #000");
    }

    public EnumUtils.UserType getStatus() {
        return status;
    }

    public List<Student> getStudentsData() {
        return studentsData;
    }

    public List<ExternalBorrower> getGuestsData() {
        return guestsData;
    }

    public enum PreloadType {
        RESET, ADD
    }

}
