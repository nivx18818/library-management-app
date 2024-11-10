package view;

import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideInRight;
import animatefx.animation.ZoomOut;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.AnimationUtils;
import util.ChangeScene;
import util.EnumUtils;
import util.RegExPatterns;

import java.io.IOException;
import java.util.logging.Logger;

public class LoginController {

    public static JFXDialog dialog;
    private static LoginController controller;
    @FXML
    private StackPane stackPaneContainer;
    @FXML
    private TextField emailSignUp;
    @FXML
    private TextField fullNameSignUp;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordSignUp;
    @FXML
    private Pane sectionFour;
    @FXML
    private Pane sectionOne;
    @FXML
    private Pane sectionThree;
    @FXML
    private Pane sectionTwo;
    @FXML
    private Button signInButton;
    @FXML
    private Button signInButton2;
    @FXML
    private Button signUpButton;
    @FXML
    private Button signUpButton2;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField studentIDSignUp;
    @FXML
    private Label errorAccountNotify;
    @FXML
    private Label registerNoticeText;
    @FXML
    private Pane loadingPane;
    @FXML
    private TextField citizenIDSignUp;
    @FXML
    private TextField phoneNumberSignUp;
    @FXML
    private ToggleGroup userType;
    @FXML
    private JFXComboBox<String> majorComboBox = new JFXComboBox<>();
    @FXML
    private Label forgotPasswordLabel;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Pane logoPaneSignIn;
    @FXML
    private Pane logoPaneSignUp;

    public LoginController() {
        controller = this;
    }

    public static LoginController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        AnimationUtils.zoomIn(rootPane, 1.2);

        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);

        setDefault();

        majorComboBox.getItems().addAll(EnumUtils.UETMajor);

        stackPaneContainer.setOnMouseClicked(event -> {
            stackPaneContainer.requestFocus();
        });
    }

    @FXML
    void handleSignUpOption(MouseEvent event) {
        Platform.runLater(() -> sectionFour.requestFocus());
        setDefault();

        if (event.getSource().equals(signUpButton)) {
            handleSignUpButtonClicked();
        }
    }

    // Show effect when click on sign up button in the login form
    public void handleSignUpButtonClicked() {
        sectionOne.setVisible(false);
        sectionTwo.setVisible(false);
        sectionThree.setVisible(true);
        sectionFour.setVisible(true);
        new SlideInRight(sectionThree).setSpeed(1.2).play();
        new SlideInLeft(sectionFour).setSpeed(1.2).play();
        AnimationUtils.zoomIn(logoPaneSignIn, 0.7);
        errorAccountNotify.setOpacity(0.0);
    }

    @FXML
    void handleStudentComboBoxClicked(MouseEvent event) {
        setDefault();

        citizenIDSignUp.setDisable(true);
        phoneNumberSignUp.setDisable(true);
        citizenIDSignUp.setOpacity(0);
        phoneNumberSignUp.setOpacity(0);

        majorComboBox.setDisable(false);
        majorComboBox.setOpacity(1);
        studentIDSignUp.setDisable(false);
        studentIDSignUp.setOpacity(1);

    }

    @FXML
    void handleGuestComboBoxClicked(MouseEvent event) {
        setDefault();

        citizenIDSignUp.setDisable(false);
        phoneNumberSignUp.setDisable(false);
        citizenIDSignUp.setOpacity(1);
        phoneNumberSignUp.setOpacity(1);

        majorComboBox.setDisable(true);
        majorComboBox.setOpacity(0);
        studentIDSignUp.setDisable(true);
        studentIDSignUp.setOpacity(0);
    }

    @FXML
    void handleSignInOption(MouseEvent event) {
        setDefault();

        if (event.getSource().equals(signInButton2)) {
            handleSignInButtonClicked();
        }
    }

    // Show effect when click on sign in button in the register form
    public void handleSignInButtonClicked() {
        sectionThree.setVisible(false);
        sectionFour.setVisible(false);
        sectionOne.setVisible(true);
        sectionTwo.setVisible(true);
        new SlideInRight(sectionOne).setSpeed(1.2).play();
        new SlideInLeft(sectionTwo).setSpeed(1.2).play();
        AnimationUtils.zoomIn(logoPaneSignUp, 0.6);
        errorAccountNotify.setOpacity(0.0);
    }

    /**
     * Handles the event when the user clicks the login button in the login section.
     * <p>
     * This method checks if the account is valid. If the account is valid,
     * the user will be redirected to the dashboard.
     *
     * @param event the mouse event triggered when clicking the login button
     * @throws IOException if an input/output error occurs during the sign-in process
     */
    @FXML
    void handleSignInStatus(MouseEvent event) throws IOException {
        if (event.getSource().equals(signInButton)) {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Check if the account is valid and redirect to the dashboard or show an error message
            if (checkAccount(username, password)) {
                goDashboard();
            } else {
                handleFailedLogin();
            }
        }
    }

    public void goDashboard() throws IOException {
        ZoomOut zoomOut = new ZoomOut(rootPane);

        AnimationUtils.zoomIn(loadingPane, 1);
        loadingPane.setVisible(true);

        zoomOut.setOnFinished(event -> {
            try {
                ChangeScene.changeInterfaceWindow((Stage) loadingPane.getScene().getWindow(),
                        "/fxml/admin-global-layout.fxml", "Library Management System");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            rootPane.setVisible(false);
        });
        zoomOut.play();
    }

    public void handleFailedLogin() {
        errorAccountNotify.setOpacity(1.0);
        forgotPasswordLabel.setVisible(false);
        AnimationUtils.playNotificationTimeline(errorAccountNotify, 3.0, "red");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3.5), event -> {
            forgotPasswordLabel.setVisible(true);
        }));
        timeline.play();
    }

    /**
     * Check if the account is valid by comparing the username and password with the database.
     *
     * @param username the username of the account
     * @param password the password of the account
     * @return true if the account is valid, false otherwise
     */
    public boolean checkAccount(String username, String password) {
        //TODO: Implement the account checking process
        return true;
    }

    /**
     * Handles the event when the user clicks the sign-up button in the register section.
     * <p>
     * This method checks if the sign-up information is valid. If the information is valid,
     * the user will be redirected to the login section.
     *
     * @param event the mouse event triggered when clicking the sign-up button
     */
    @FXML
    public void handleSignUpStatus(MouseEvent event) {
        if (event.getSource().equals(signUpButton2)) {
            String fullName = fullNameSignUp.getText();
            String email = emailSignUp.getText();
            String password = passwordSignUp.getText();
            RadioButton selectedUserType = (RadioButton) userType.getSelectedToggle();
            String majorOrPhoneNumber = selectedUserType.getText().equals("Student") ?
                    ((majorComboBox.getValue() != null) ? majorComboBox.getValue() : "") :
                    phoneNumberSignUp.getText();
            String username = selectedUserType.getText().equals("Student") ?
                    studentIDSignUp.getText() : citizenIDSignUp.getText();
            // Check if the sign-up information is valid
            if (checkSignUp(fullName, majorOrPhoneNumber, email, username, password, registerNoticeText)) {
                logInAfterRegister();
            }
        }
    }

    // Display a notification and automatically redirect to the login section after a successful sign-up.
    public void logInAfterRegister() {

        int[] countdownSeconds = {5};

        AnimationUtils.playNotificationTimeline(registerNoticeText, 5.0, "08a80d");

        registerNoticeText.setText("Sign up successfully. Please log in! Automatically after " +
                countdownSeconds[0] + " seconds...");

        Timeline countdown = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    countdownSeconds[0]--;

                    registerNoticeText.setText("Sign up successfully. Please log in! Automatically" +
                            " after " + countdownSeconds[0] + " seconds...");

                    if (countdownSeconds[0] == 0) {
                        registerNoticeText.setOpacity(0);
                        registerNoticeText.setText("");
                        handleSignInButtonClicked();
                        setDefault();
                    }
                })
        );

        countdown.setCycleCount(countdownSeconds[0]);
        countdown.play();

        errorAccountNotify.setOpacity(0.0);
    }

    public boolean checkSignUp(String fullName, String majorOrPhoneNumber, String email,
                               String username, String password, Label registerNoticeText) {

        if (fullName.isEmpty() || majorOrPhoneNumber.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            registerNoticeText.setText("Please fill in all fields.");
            AnimationUtils.playNotificationTimeline(registerNoticeText, 3.0, "red");
            return false;
        } else if (!RegExPatterns.emailPattern(email)) {
            registerNoticeText.setText("Invalid email.");
            AnimationUtils.playNotificationTimeline(registerNoticeText, 3.0, "red");
            return false;
        } else if (!RegExPatterns.passwordPattern(password)) {
            registerNoticeText.setText("Invalid password.");
            AnimationUtils.playNotificationTimeline(registerNoticeText, 3.0, "red");
            return false;
        }

        RadioButton selectedUserType = (RadioButton) userType.getSelectedToggle();

        if (selectedUserType.getText().equals("Student")) {
            if (!RegExPatterns.studentIDPattern(username)) {
                registerNoticeText.setText("Invalid student ID.");
                AnimationUtils.playNotificationTimeline(registerNoticeText, 3.0, "red");
                return false;
            }
        } else {
            if (!RegExPatterns.citizenIDPattern(username)) {
                registerNoticeText.setText("Invalid citizen ID.");
                AnimationUtils.playNotificationTimeline(registerNoticeText, 3.0, "red");
                return false;
            }
            if (!RegExPatterns.phoneNumberPattern(majorOrPhoneNumber)) {
                registerNoticeText.setText("Invalid phone number.");
                AnimationUtils.playNotificationTimeline(registerNoticeText, 3.0, "red");
                return false;
            }
        }
        return true;
    }

    @FXML
    public void handleForgotPassword(MouseEvent event) {
        ChangeScene.openAdminPopUp(stackPaneContainer, "/fxml/forgot" +
                "-password-dialog.fxml");
    }

    public void setDefault() {
        fullNameSignUp.setText("");
        emailSignUp.setText("");
        studentIDSignUp.setText("");
        passwordSignUp.setText("");
        majorComboBox.setValue(null);
        citizenIDSignUp.setText("");
        phoneNumberSignUp.setText("");

        usernameField.setText("");
        passwordField.setText("");

    }
}
