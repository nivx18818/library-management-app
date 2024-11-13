package app.libmgmt.view.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.util.RegExPatterns;

public class AdminUsersEditDialogController {

    private static AdminUsersEditDialogController controller;
    @FXML
    private JFXButton updateButton;
    @FXML
    private JFXButton closeDialogButton;
    @FXML
    private Pane container;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField idTextField;
    @FXML
    private ImageView imgClose;
    @FXML
    private Label majorLabel;
    @FXML
    private JFXComboBox majorComboBox;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private Label userTypeLabel;
    @FXML
    private Label notificationLabel;
    private String[] originalData;
    private EnumUtils.UserType userType;

    public AdminUsersEditDialogController() {
        controller = this;
    }

    public static AdminUsersEditDialogController getInstance() {
        return controller;
    }

    public void initialize() {
        System.out.println("AdminUsersEditDialogController initialized");

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    @FXML
    void updateButtonOnAction(ActionEvent event) {
        if (!checkValidFields()) {
            return;
        }

        String[] updatedData = new String[]{
                idTextField.getText(),
                nameTextField.getText(),
                (majorLabel.isVisible() ? (String) majorComboBox.getValue() :
                        phoneNumberTextField.getText()),
                emailTextField.getText()
        };

        boolean hasChanges = false;

        for (int i = 0; i < updatedData.length; i++) {
            if (!updatedData[i].equals(originalData[i])) {
                hasChanges = true;
                break;
            }
        }

        if (hasChanges) {
            String[] formattedData = new String[]{
                    EnumUtils.UserType.STUDENT == userType ? "Student" : "External Borrower",
                    updatedData[1],
                    updatedData[2],
                    updatedData[3],
                    updatedData[0]
            };

            AdminGlobalController.getInstance().updateUserData(formattedData, userType);
            notificationLabel.setText("User updated successfully!");
            originalData = updatedData;
            notificationLabel.setStyle("-fx-text-fill: #08a80d;");
        } else {
            notificationLabel.setText("No changes detected.");
            notificationLabel.setStyle("-fx-text-fill: #ff0000;");
        }

        AnimationUtils.playNotificationTimeline(notificationLabel, 2,
                hasChanges ? "#08a80d" : "#ff0000");

    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    public void showOriginalUserData(String[] data, EnumUtils.UserType userType) {
        this.userType = userType;
        originalData = data;
        idTextField.setText(originalData[0]);
        nameTextField.setText(originalData[1]);
        if (userType == EnumUtils.UserType.GUEST) {
            phoneNumberLabel.setVisible(true);
            phoneNumberTextField.setVisible(true);
            phoneNumberTextField.setText(originalData[2]);
        } else {
            majorLabel.setVisible(true);
            majorComboBox.getItems().addAll(EnumUtils.UETMajor);
            majorComboBox.setVisible(true);
            majorComboBox.setValue(originalData[2]);
        }

        emailTextField.setText(originalData[3]);
        userTypeLabel.setText(userType == EnumUtils.UserType.GUEST ? "External Borrower" :
                "Student");
    }

    public boolean checkValidFields() {
        if (majorLabel.isVisible()) {
            if (!RegExPatterns.studentIDPattern(idTextField.getText())) {
                notificationLabel.setText("Student ID must be 8 digits!");
                notificationLabel.setStyle("-fx-text-fill: #ff0000;");
                AnimationUtils.playNotificationTimeline(notificationLabel, 2, "#ff0000");
                return false;
            }
        } else {
            if (!RegExPatterns.citizenIDPattern(idTextField.getText())) {
                notificationLabel.setText("Citizen ID must be 12 digits!");
                notificationLabel.setStyle("-fx-text-fill: #ff0000;");
                AnimationUtils.playNotificationTimeline(notificationLabel, 2, "#ff0000");
                return false;
            }
            if (!RegExPatterns.phoneNumberPattern(phoneNumberTextField.getText())) {
                notificationLabel.setText("Phone number must be 10 digits!");
                notificationLabel.setStyle("-fx-text-fill: #ff0000;");
                AnimationUtils.playNotificationTimeline(notificationLabel, 2, "#ff0000");
                return false;
            }
        }

        if (!RegExPatterns.emailPattern(emailTextField.getText())) {
            notificationLabel.setText("Invalid email format!");
            notificationLabel.setStyle("-fx-text-fill: #ff0000;");
            AnimationUtils.playNotificationTimeline(notificationLabel, 2, "#ff0000");
            return false;
        }

        return true;
    }

}
