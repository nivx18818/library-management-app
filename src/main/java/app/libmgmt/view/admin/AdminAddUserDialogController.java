package app.libmgmt.view.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.util.RegExPatterns;

import java.util.ArrayList;
import java.util.List;

public class AdminAddUserDialogController {

    private final String[] majorList = EnumUtils.UETMajor;
    AdminUsersLayoutController adminUsersLayoutController = AdminUsersLayoutController.getInstance();
    AdminGlobalController adminGlobalController = AdminGlobalController.getInstance();

    // FXML UI components
    @FXML private ToggleGroup addUserType;
    @FXML private Pane adminPane, container, guestPane, studentPane;
    @FXML private JFXRadioButton adminRadioBtn, guestRadioBtn, studentRadioBtn;
    @FXML private Label notificationLabel;
    @FXML private JFXComboBox<String> cbbMajorStudent;
    @FXML private PasswordField txtCfPasswordAdmin, txtCfPasswordGuest, txtCfPasswordStudent;
    @FXML private TextField txtEmailAdmin, txtEmailGuest, txtEmailStudent, txtIDStudent, txtIdGuest,
            txtNameAdmin, txtNameGuest, txtNameStudent, txtPhoneNumberGuest;
    @FXML private PasswordField txtPasswordAdmin, txtPasswordGuest, txtPasswordStudent;
    @FXML private JFXButton closeDialogButton;
    @FXML private ImageView imgClose;

    public void initialize() {
        System.out.println("Initialize Add User Dialog");

        cbbMajorStudent.getItems().addAll(majorList);

        container.setOnMouseClicked(event -> container.requestFocus());

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);

        setDefaultPane();
        studentPane.setVisible(true);
    }

    // --- Helper Methods ---
    private void setDefaultPane() {
        studentPane.setVisible(false);
        guestPane.setVisible(false);
        adminPane.setVisible(false);
    }

    private void setDefaultContent() {
        txtNameAdmin.setText("");
        txtEmailAdmin.setText("");
        txtPasswordAdmin.setText("");
        txtCfPasswordAdmin.setText("");
        txtNameStudent.setText("");
        txtEmailStudent.setText("");
        txtPasswordStudent.setText("");
        txtCfPasswordStudent.setText("");
        txtIDStudent.setText("");
        cbbMajorStudent.getSelectionModel().clearSelection();
        txtNameGuest.setText("");
        txtEmailGuest.setText("");
        txtPasswordGuest.setText("");
        txtCfPasswordGuest.setText("");
        txtPhoneNumberGuest.setText("");
        txtIdGuest.setText("");
    }

    private void showNotification(String message, String color) {
        notificationLabel.setText(message);
        AnimationUtils.playNotificationTimeline(notificationLabel, 3, color);
    }

    private boolean isFieldEmpty(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) return true;
        }
        return false;
    }

    // --- Validation Methods ---
    private boolean checkValidAdmin(String[] adminInfo, EnumUtils.UserType userType) {
        String name = adminInfo[0];
        String email = adminInfo[1];
        String password = adminInfo[2];
        String cfPassword = adminInfo[3];

        if (isFieldEmpty(name, email, password, cfPassword)) {
            showNotification("Please fill in all fields.", "red");
            return false;
        }
        if (!password.equals(cfPassword)) {
            showNotification("Password does not match.", "red");
            return false;
        }
        if (!RegExPatterns.emailPattern(email)) {
            showNotification("Invalid email.", "red");
            return false;
        }
        if (!RegExPatterns.passwordPattern(password)) {
            showNotification("Password must contain at least 8 characters.", "red");
            return false;
        }
        return true;
    }

    private boolean checkValidUser(String[] userInfo, EnumUtils.UserType userType) {
        String name = userInfo[0];
        String majorOrContact = userInfo[1];
        String email = userInfo[2];
        String id = userInfo[3];
        String password = userInfo[4];
        String cfPassword = userInfo[5];

        if (isFieldEmpty(name, email, password, cfPassword, id, majorOrContact)) {
            showNotification("Please fill in all fields.", "red");
            return false;
        }
        if (!password.equals(cfPassword)) {
            showNotification("Password does not match.", "red");
            return false;
        }
        if (userType == EnumUtils.UserType.STUDENT) {
            if (!RegExPatterns.studentIDPattern(id)) {
                showNotification("Invalid student ID.", "red");
                return false;
            }
            if (majorOrContact == null) {
                showNotification("Please select a major.", "red");
                return false;
            }
        }
        if (userType == EnumUtils.UserType.GUEST) {
            if (!RegExPatterns.citizenIDPattern(id)) {
                showNotification("Invalid guest ID.", "red");
                return false;
            }
            if (!RegExPatterns.phoneNumberPattern(majorOrContact)) {
                showNotification("Invalid phone number.", "red");
                return false;
            }
        }
        if (!RegExPatterns.emailPattern(email)) {
            showNotification("Invalid email.", "red");
            return false;
        }
        if (!RegExPatterns.passwordPattern(password)) {
            showNotification("Password must contain at least 8 characters.", "red");
            return false;
        }
        return true;
    }

    // --- User Add Methods ---
    private void addAdmin(String[] adminInfo) {
        if (checkValidAdmin(adminInfo, EnumUtils.UserType.ADMIN)) {
            AdminDashboardController adminDashboardController = AdminDashboardController.getInstance();
            adminDashboardController.getAdminData().add(adminInfo);
            adminDashboardController.loadAdminDataTable(adminInfo[0], adminInfo[1]);
            showNotification("Added successfully", "#08a80d");
            setDefaultContent();
        }
    }

    private void addStudent(String[] studentInfo) {
        if (checkValidUser(studentInfo, EnumUtils.UserType.STUDENT)) {
            List<String[]> userData = new ArrayList<>();
            String[] newUser = {"Student", studentInfo[0], studentInfo[1], studentInfo[2], studentInfo[3], studentInfo[4]};
            System.out.println("Adding student");
            userData.add(newUser);
            if (adminUsersLayoutController.getStatus() == EnumUtils.UserType.STUDENT) {
                adminUsersLayoutController.preloadData(userData, "admin-users-student-bar.fxml", AdminUsersLayoutController.PreloadType.ADD);
            }
            adminGlobalController.getObservableUsersData(EnumUtils.UserType.STUDENT).add(newUser);
            adminUsersLayoutController.getStudentsData().add(newUser);
            showNotification("Added successfully", "#08a80d");
        }
    }

    private void addGuest(String[] guestInfo) {
        if (checkValidUser(guestInfo, EnumUtils.UserType.GUEST)) {
            List<String[]> userData = new ArrayList<>();
            String[] newUser = {"External Borrower", guestInfo[0], guestInfo[1], guestInfo[2], guestInfo[3], guestInfo[4]};
            System.out.println("Adding guest");
            userData.add(newUser);
            if (adminUsersLayoutController.getStatus() == EnumUtils.UserType.GUEST) {
                adminUsersLayoutController.preloadData(userData, "admin-users-guest-bar.fxml", AdminUsersLayoutController.PreloadType.ADD);
            }
            adminGlobalController.getObservableUsersData(EnumUtils.UserType.GUEST).add(newUser);
            adminUsersLayoutController.getGuestsData().add(newUser);
            showNotification("Added successfully", "#08a80d");
        }
    }

    // --- Event Handlers ---
    @FXML
    void studentRadioBtnOnAction(ActionEvent event) {
        setDefaultPane();
        setDefaultContent();
        studentPane.setVisible(true);
        AnimationUtils.zoomIn(studentPane, 1);
    }

    @FXML
    void guestRadioBtnOnAction(ActionEvent event) {
        setDefaultPane();
        setDefaultContent();
        guestPane.setVisible(true);
        AnimationUtils.zoomIn(guestPane, 1);
    }

    @FXML
    void adminRadioBtnOnAction(ActionEvent event) {
        setDefaultPane();
        setDefaultContent();
        adminPane.setVisible(true);
        AnimationUtils.zoomIn(adminPane, 1);
    }

    @FXML
    void addButtonOnAction(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) addUserType.getSelectedToggle();
        if (selectedRadioButton == studentRadioBtn) {
            System.out.println("Add Student");
            String[] studentInfo = {txtNameStudent.getText(), cbbMajorStudent.getSelectionModel().getSelectedItem(),
                    txtEmailStudent.getText(), txtIDStudent.getText(), txtPasswordStudent.getText(), txtCfPasswordStudent.getText()};
            addStudent(studentInfo);
        } else if (selectedRadioButton == guestRadioBtn) {
            System.out.println("Add Guest");
            String[] guestInfo = {txtNameGuest.getText(), txtPhoneNumberGuest.getText(),
                    txtEmailGuest.getText(), txtIdGuest.getText(), txtPasswordGuest.getText(), txtCfPasswordGuest.getText()};
            addGuest(guestInfo);
        } else if (selectedRadioButton == adminRadioBtn) {
            System.out.println("Add Admin");
            String[] adminInfo = {txtNameAdmin.getText(), txtEmailAdmin.getText(),
                    txtPasswordAdmin.getText(), txtCfPasswordAdmin.getText()};
            addAdmin(adminInfo);
        }
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        setDefaultContent();
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }
}
