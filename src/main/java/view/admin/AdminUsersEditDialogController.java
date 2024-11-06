package view.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import util.AnimationUtils;
import util.ChangeScene;
import util.EnumUtils;

public class AdminUsersEditDialogController {

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private Pane container;

    @FXML
    private JFXToggleButton editMode;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField idTextField;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label majorOrPhoneLabel;

    @FXML
    private TextField majorOrPhoneTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label userTypeLabel;

    @FXML
    private Label notificationLabel;

    private String[] originalData;

    private static AdminUsersEditDialogController controller;

    public AdminUsersEditDialogController() {
        controller = this;
    }

    public static AdminUsersEditDialogController getInstance() {
        return controller;
    }

    public void initialize() {
        System.out.println("AdminUsersEditDialogController initialized");

        editMode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setEditableFields(newValue);
        });
        setEditableFields(false);

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    @FXML
    void updateButtonOnAction(ActionEvent event) {
        String updatedData[] = new String[] {
                idTextField.getText(),
                nameTextField.getText(),
                majorOrPhoneTextField.getText(),
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
            String[] userData =
                    AdminGlobalFormController.getInstance().getUserDataById(originalData[0]);
            if (userData != null) {
                updateData(updatedData, userData);
                AdminUsersLayoutController.getInstance().refreshUsersList();
                notificationLabel.setText("User updated successfully!");
                originalData = updatedData;
                notificationLabel.setStyle("-fx-text-fill: #08a80d;");
            } else {
                notificationLabel.setText("User not found!");
                notificationLabel.setStyle("-fx-text-fill: ff0000;");
            }
        } else {
            notificationLabel.setText("No changes detected.");
            notificationLabel.setStyle("-fx-text-fill: #ff0000;");
        }

        AnimationUtils.playNotificationTimeline(notificationLabel, 2,
                hasChanges ? "#08a80d" : "#ff0000");

    }

    private void updateData(String[] src, String[] dest) {
        String[] formattedData = new String[] {
                src[1],
                src[2],
                src[3],
                src[0]
        };
        System.arraycopy(formattedData, 0, dest, 1, src.length);
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    private void setEditableFields(boolean isEditable) {
        idTextField.setEditable(isEditable);
        nameTextField.setEditable(isEditable);
        emailTextField.setEditable(isEditable);
        majorOrPhoneTextField.setEditable(isEditable);
    }

    public void showOriginalUserData(String[] data, EnumUtils.UserType userType) {
        originalData = data;
        idTextField.setText(originalData[0]);
        nameTextField.setText(originalData[1]);
        majorOrPhoneLabel.setText(userType == EnumUtils.UserType.GUEST ? "Phone number :" :
                "Major :");
        majorOrPhoneTextField.setText(originalData[2]);
        emailTextField.setText(originalData[3]);
        userTypeLabel.setText(userType == EnumUtils.UserType.GUEST ? "External Borrower" :
                "Student");
    }

}
