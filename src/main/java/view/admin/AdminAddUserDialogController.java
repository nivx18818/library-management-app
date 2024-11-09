package view.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import util.AnimationUtils;
import util.ChangeScene;
import util.EnumUtils;
import util.RegExPatterns;

import java.util.ArrayList;
import java.util.List;

public class AdminAddUserDialogController {
    private final String[] majorList = EnumUtils.UETMajor;
    AdminUsersLayoutController adminUsersLayoutController =
            AdminUsersLayoutController.getInstance();
    AdminGlobalFormController adminGlobalFormController =
            AdminGlobalFormController.getInstance();
    @FXML
    private ToggleGroup addUserType;
    @FXML
    private Pane adminPane;
    @FXML
    private JFXRadioButton adminRadioBtn;
    @FXML
    private JFXComboBox<String> cbbMajorStudent;
    @FXML
    private Pane container;
    @FXML
    private Pane guestPane;
    @FXML
    private JFXRadioButton guestRadioBtn;
    @FXML
    private Label notificationLabel;
    @FXML
    private Pane studentPane;
    @FXML
    private JFXRadioButton studentRadioBtn;
    @FXML
    private PasswordField txtCfPasswordAdmin;
    @FXML
    private PasswordField txtCfPasswordGuest;
    @FXML
    private PasswordField txtCfPasswordStudent;
    @FXML
    private TextField txtEmailAdmin;
    @FXML
    private TextField txtEmailGuest;
    @FXML
    private TextField txtEmailStudent;
    @FXML
    private TextField txtIDStudent;
    @FXML
    private TextField txtIdGuest;
    @FXML
    private TextField txtNameAdmin;
    @FXML
    private TextField txtNameGuest;
    @FXML
    private TextField txtNameStudent;
    @FXML
    private PasswordField txtPasswordAdmin;
    @FXML
    private PasswordField txtPasswordGuest;
    @FXML
    private PasswordField txtPasswordStudent;
    @FXML
    private TextField txtPhoneNumberGuest;
    @FXML
    private JFXButton closeDialogButton;
    @FXML
    private ImageView imgClose;

    public void initialize() {
        System.out.println("Initialize Add User Dialog");

        cbbMajorStudent.getItems().addAll(majorList);

        container.setOnMouseClicked(event -> {
            container.requestFocus();
        });

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);

        setDefaultPane();
        studentPane.setVisible(true);
    }

    public void setDefaultPane() {
        studentPane.setVisible(false);
        guestPane.setVisible(false);
        adminPane.setVisible(false);
    }

    public void setDefaultContent() {
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
            String name = txtNameStudent.getText();
            String email = txtEmailStudent.getText();
            String password = txtPasswordStudent.getText();
            String cfPassword = txtCfPasswordStudent.getText();
            String major = cbbMajorStudent.getSelectionModel().getSelectedItem();
            String id = txtIDStudent.getText();
            String[] studentInfo = {name, major, email, id, password, cfPassword};
            addStudent(studentInfo);
        } else if (selectedRadioButton == guestRadioBtn) {
            System.out.println("Guest");
            String name = txtNameGuest.getText();
            String email = txtEmailGuest.getText();
            String password = txtPasswordGuest.getText();
            String cfPassword = txtCfPasswordGuest.getText();
            String phoneNumber = txtPhoneNumberGuest.getText();
            String id = txtIdGuest.getText();
            String[] guestInfo = {name, phoneNumber, email, id, password, cfPassword};
            addGuest(guestInfo);
        } else if (selectedRadioButton == adminRadioBtn) {
            System.out.println("Admin");
            String name = txtNameAdmin.getText();
            String email = txtEmailAdmin.getText();
            String password = txtPasswordAdmin.getText();
            String cfPassword = txtCfPasswordAdmin.getText();
            String[] adminInfo = {name, email, password, cfPassword};
            addAdmin(adminInfo);
        }
    }

    public void addAdmin(String[] adminInfo) {
        if (checkValidAdmin(adminInfo)) {
            AdminDashboardController adminDashboardController = AdminDashboardController.getInstance();
            adminDashboardController.getAdminData().add(adminInfo);
            adminDashboardController.loadAdminDataTable(adminInfo[0], adminInfo[1]);
            showSuccessNotification();
        }
    }

    public void addStudent(String[] info) {
        if (checkValidUser(info)) {
            List<String[]> userData = new ArrayList<>();
            String[] newUser = {"Student", info[0], info[1], info[2], info[3], info[4]};
            System.out.println("Adding student");
            userData.add(newUser);
            if (adminUsersLayoutController.getStatus() == EnumUtils.UserType.STUDENT) {
                adminUsersLayoutController.preloadData(userData, "admin-users-student-bar.fxml",
                        AdminUsersLayoutController.PreloadType.ADD);
            }
            adminGlobalFormController.getUsersData().add(newUser);
            adminUsersLayoutController.getStudentsData().add(newUser);
            showSuccessNotification();
        }
    }

    public void addGuest(String[] info) {
        if (checkValidUser(info)) {
            List<String[]> userData = new ArrayList<>();
            String[] newUser = {"External Borrower", info[0], info[1], info[2], info[3], info[4]};
            System.out.println("Adding guest");
            userData.add(newUser);
            if (adminUsersLayoutController.getStatus() == EnumUtils.UserType.GUEST) {
                adminUsersLayoutController.preloadData(userData, "admin-users-guest-bar.fxml", AdminUsersLayoutController.PreloadType.ADD);
            }
            adminGlobalFormController.getUsersData().add(newUser);
            adminUsersLayoutController.getGuestsData().add(newUser);
            showSuccessNotification();
        }

    }

    boolean checkValidUser(String[] info) {
        String name = info[0];
        String majorOrContact = info[1];
        String email = info[2];
        String id = info[3];
        String password = info[4];
        String cfPassword = info[5];
        boolean check = true;
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || cfPassword.isEmpty()
                || id.isEmpty() || majorOrContact.isEmpty()) {
            check = false;
            notificationLabel.setText("Please fill in all fields");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        } else if (!password.equals(cfPassword)) {
            check = false;
            notificationLabel.setText("Password does not match");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        } else if (!RegExPatterns.emailPattern(email)) {
            check = false;
            notificationLabel.setText("Invalid email");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        } else if (!RegExPatterns.passwordPattern(password)) {
            check = false;
            notificationLabel.setText("Invalid password");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        } else if (!(RegExPatterns.studentIDPattern(id) || RegExPatterns.citizenIDPattern(id))) {
            check = false;
            notificationLabel.setText("Invalid ID");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        } else if (majorOrContact.charAt(0) == '0' && !RegExPatterns.phoneNumberPattern(majorOrContact)) {
            check = false;
            notificationLabel.setText("Invalid phone number");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        }
        return check;
    }

    public boolean checkValidAdmin(String[] info) {
        String name = info[0];
        String email = info[1];
        String password = info[2];
        String cfPassword = info[3];
        boolean check = true;
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || cfPassword.isEmpty()) {
            check = false;
            notificationLabel.setText("Please fill in all fields");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        } else if (!password.equals(cfPassword)) {
            check = false;
            notificationLabel.setText("Password does not match");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        } else if (!RegExPatterns.emailPattern(email)) {
            check = false;
            notificationLabel.setText("Invalid email");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        } else if (!RegExPatterns.passwordPattern(password)) {
            check = false;
            notificationLabel.setText("Invalid password");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "red");
        }
        return check;
    }

    public void showSuccessNotification() {
        notificationLabel.setText("Added successfully");
        AnimationUtils.playNotificationTimeline(notificationLabel, 3, "#08a80d");
        setDefaultContent();
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
