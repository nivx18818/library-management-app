package app.libmgmt.view.controller.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
    private JFXButton updateButton, closeDialogButton;
    @FXML
    private Pane container;
    @FXML
    private TextField emailTextField, idTextField, phoneNumberTextField, nameTextField;
    @FXML
    private ImageView imgClose;
    @FXML
    private Label majorLabel, phoneNumberLabel, userTypeLabel, notificationLabel;
    @FXML
    private JFXComboBox<String> majorComboBox;

    private String[] originalData;
    private EnumUtils.UserType userType;

    // Constructor to set the controller instance
    public AdminUsersEditDialogController() {
        controller = this;
    }

    // Singleton pattern to get the current instance
    public static AdminUsersEditDialogController getInstance() {
        return controller;
    }

    // Initialization method called when the view is loaded
    public void initialize() {
        System.out.println("AdminUsersEditDialogController initialized");
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    // Action triggered when the update button is clicked
    @FXML
    void updateButtonOnAction(ActionEvent event) {
        if (!areFieldsValid())
            return;

        String[] updatedData = extractUpdatedData();
        if (isDataChanged(updatedData)) {
            processUserDataUpdate(updatedData);
        } else {
            showNoChangesDetected();
        }
    }

    // Action triggered when the close button is clicked
    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    // Displays original user data in the fields
    public void showOriginalUserData(String[] data, EnumUtils.UserType userType) {
        this.userType = userType;
        originalData = data;
        populateFieldsWithData();
    }

    // Checks if all fields are valid
    private boolean areFieldsValid() {
        if (isStudentDataInvalid() || isGuestDataInvalid() || isEmailInvalid()) {
            return false;
        }

        return true;
    }

    // Validates student-specific data
    private boolean isStudentDataInvalid() {
        if (majorLabel.isVisible() && !RegExPatterns.studentIDPattern(idTextField.getText())) {
            showValidationError("Student ID must be 8 digits!");
            return true;
        }

        return false;
    }

    // Validates guest-specific data
    private boolean isGuestDataInvalid() {
        if (!majorLabel.isVisible()) {
            if (!RegExPatterns.citizenIDPattern(idTextField.getText())) {
                showValidationError("Citizen ID must be 12 digits!");
                return true;
            }

            if (!RegExPatterns.phoneNumberPattern(phoneNumberTextField.getText())) {
                showValidationError("Phone number must be 10 digits!");
                return true;
            }
        }

        return false;
    }

    // Validates email format
    private boolean isEmailInvalid() {
        if (!RegExPatterns.emailPattern(emailTextField.getText())) {
            showValidationError("Invalid email format!");
            return true;
        }

        return false;
    }

    // Populates fields with data based on user type
    private void populateFieldsWithData() {
        idTextField.setText(originalData[0]);
        nameTextField.setText(originalData[1]);

        if (userType == EnumUtils.UserType.GUEST) {
            showGuestFields();
        } else {
            showStudentFields();
        }

        emailTextField.setText(originalData[3]);
        userTypeLabel.setText(userType == EnumUtils.UserType.GUEST ? "External Borrower" : "Student");
    }

    // Shows fields specific to a guest user
    private void showGuestFields() {
        phoneNumberLabel.setVisible(true);
        phoneNumberTextField.setVisible(true);
        phoneNumberTextField.setText(originalData[2]);
    }

    // Shows fields specific to a student user
    private void showStudentFields() {
        majorLabel.setVisible(true);
        majorComboBox.getItems().addAll(EnumUtils.UETMajor);
        majorComboBox.setVisible(true);
        majorComboBox.setValue(originalData[2]);
    }

    // Extracts updated data from fields
    private String[] extractUpdatedData() {
        //data format: [name, major/phone, email, id, password]
        return new String[] {
                nameTextField.getText(),
                majorLabel.isVisible() ? majorComboBox.getValue() : phoneNumberTextField.getText(),
                emailTextField.getText(),
                idTextField.getText(),
                "password"
        };
    }

    // Checks if any data has changed
    private boolean isDataChanged(String[] updatedData) {
        for (int i = 0; i < updatedData.length; i++) {
            if (!updatedData[i].equals(originalData[i]))
                return true;
        }

        return false;
    }

    // Processes the user data update and updates the UI
    private void processUserDataUpdate(String[] updatedData) {
        AdminGlobalController.getInstance().updateUserData(updatedData, userType);
        showSuccessMessage("User updated successfully!");
        originalData = updatedData;
    }

    // Shows an error message for validation failures
    private void showValidationError(String message) {
        notificationLabel.setText(message);
        notificationLabel.setStyle("-fx-text-fill: #ff0000;");
        AnimationUtils.playNotificationTimeline(notificationLabel, 2, "#ff0000");
    }

    // Shows a success message for successful updates
    private void showSuccessMessage(String message) {
        notificationLabel.setText(message);
        notificationLabel.setStyle("-fx-text-fill: #08a80d;");
        AnimationUtils.playNotificationTimeline(notificationLabel, 2, "#08a80d");
    }

    // Displays a message when no changes are detected
    private void showNoChangesDetected() {
        notificationLabel.setText("No changes detected.");
        notificationLabel.setStyle("-fx-text-fill: #ff0000;");
        AnimationUtils.playNotificationTimeline(notificationLabel, 2, "#ff0000");
    }
}
