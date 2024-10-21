package view;

import animatefx.animation.FadeInLeft;
import animatefx.animation.FadeInRight;
import animatefx.animation.ZoomIn;
import animatefx.animation.ZoomOut;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.AdminInitializer;
import util.Animation;
import util.ChangeScene;
import util.RegExPatterns;
import view.admin.AdminNavigationController;

import java.io.IOException;
import java.util.logging.Logger;

public class LoginController {

    private static LoginController controller;
    private String[] major = {"CN1 - Information Technology", "CN2 - Computer Engineering",
            "CN3 - Engineering Physics", "CN4 - Mechanical Engineering", "CN5 - Construction " +
            "Engineering Technology", "CN6 - Mechatronic Engineering Technology", "CN7 - " +
            "Aerospace Technology", "CN8 - Computer Science", "CN9 - Electronic Engineering Technology - " +
            "Telecommunications", "CN10 - Agricultural Technology", "CN11 - Control and " +
            "Automation Engineering", "CN12 - Artificial intelligence", "CN13 - Energy " +
            "Engineering Technology", "CN14 - Information Systems", "CN15 - Computer Networks and" +
            " Data Communications", "CN17 - Computers and Robots"};
    @FXML
    private StackPane stackPaneContainer;
    @FXML
    private BorderPane container;
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
    public static JFXDialog dialog;

    public LoginController() {
        controller = this;
    }

    public static LoginController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);

        setDefault();

        majorComboBox.getItems().addAll(major);

        container.setOnMouseClicked(event -> {
            container.requestFocus();
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

    public void handleSignUpButtonClicked() {
        new FadeInRight(sectionThree).play();
        new ZoomIn(sectionFour).play();

        sectionOne.setVisible(false);
        sectionOne.setDisable(true);
        sectionOne.setOpacity(0.0);

        sectionTwo.setVisible(false);
        sectionTwo.setDisable(true);
        sectionTwo.setOpacity(0.0);

        sectionThree.setVisible(true);
        sectionThree.setDisable(false);
        sectionThree.setOpacity(1.0);

        sectionFour.setVisible(true);
        sectionFour.setDisable(false);
        sectionFour.setOpacity(1.0);

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

    public void handleSignInButtonClicked() {
        new FadeInLeft(sectionTwo).play();
        new ZoomIn(sectionOne).play();

        sectionOne.setVisible(true);
        sectionOne.setDisable(false);
        sectionOne.setOpacity(1.0);

        sectionTwo.setVisible(true);
        sectionTwo.setDisable(false);
        sectionTwo.setOpacity(1.0);

        sectionThree.setVisible(false);
        sectionThree.setDisable(true);
        sectionThree.setOpacity(0.0);

        sectionFour.setVisible(false);
        sectionFour.setDisable(true);
        sectionFour.setOpacity(0.0);

        errorAccountNotify.setOpacity(0.0);
    }

    @FXML
    void handleSignInStatus(MouseEvent event) throws IOException {
        if (event.getSource().equals(signInButton)) {
            signIn();
        }
    }

    public void signIn() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (checkAccount(username, password)) {
            goDashboard();
        } else {
            handleFailedLogin();
        }
    }

    /**
     * Check if the account is valid
     *
     * @param username the username of the account
     * @param password the password of the account
     * @return true if the account is valid, false otherwise
     */
    public boolean checkAccount(String username, String password) {
        //TODO: Implement this method
        return true;
    }

    @FXML
    public void handleSignUpStatus(MouseEvent event) {
        if (event.getSource().equals(signUpButton2)) {
            signUp();
        }
    }

    public void signUp() {
        String fullName = fullNameSignUp.getText();
        String email = emailSignUp.getText();
        String password = passwordSignUp.getText();
        RadioButton selectedUserType = (RadioButton) userType.getSelectedToggle();
        String majorOrPhoneNumber = selectedUserType.getText().equals("Student") ?
                ((majorComboBox.getValue() != null) ? majorComboBox.getValue() : "") :
                phoneNumberSignUp.getText();
        String username = selectedUserType.getText().equals("Student") ?
                studentIDSignUp.getText() : citizenIDSignUp.getText();

        if (checkSignUp(fullName, majorOrPhoneNumber, email, username, password)) {
            logInAfterRegister();
        }
    }

    public void logInAfterRegister() {

        int[] countdownSeconds = {5};

        Animation.playNotificationTimeline(registerNoticeText, 5.0, "08a80d");

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
                               String username, String password) {

        if (fullName.isEmpty() || majorOrPhoneNumber.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            registerNoticeText.setText("Please fill in all fields.");
            Animation.playNotificationTimeline(registerNoticeText, 3.0, "red");
            return false;
        } else if (!RegExPatterns.emailPattern(email)) {
            registerNoticeText.setText("Invalid email.");
            Animation.playNotificationTimeline(registerNoticeText, 3.0, "red");
            return false;
        } else if (!RegExPatterns.passwordPattern(password)) {
            registerNoticeText.setText("Invalid password.");
            Animation.playNotificationTimeline(registerNoticeText, 3.0, "red");
            return false;
        }

        RadioButton selectedUserType = (RadioButton) userType.getSelectedToggle();

        if (selectedUserType.getText().equals("Student")) {
            if (!RegExPatterns.studentIDPattern(username)) {
                registerNoticeText.setText("Invalid student ID.");
                Animation.playNotificationTimeline(registerNoticeText, 3.0, "red");
                return false;
            }
        } else {
            if (!RegExPatterns.citizenIDPattern(username)) {
                registerNoticeText.setText("Invalid citizen ID.");
                Animation.playNotificationTimeline(registerNoticeText, 3.0, "red");
                return false;
            }
            if (!RegExPatterns.phoneNumberPattern(majorOrPhoneNumber)) {
                registerNoticeText.setText("Invalid phone number.");
                Animation.playNotificationTimeline(registerNoticeText, 3.0, "red");
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

    public void goDashboard() throws IOException {
        ZoomOut zoomOut = new ZoomOut(container);

        new ZoomIn(loadingPane).play();
        loadingPane.setVisible(true);

        zoomOut.setOnFinished(event -> {
            try {
                Stage stage = (Stage) signInButton.getScene().getWindow();

                FXMLLoader fxmlLoader = new FXMLLoader(AdminInitializer.class.getResource(
                        "/fxml/admin-global-layout.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                stage.setScene(scene);
                stage.setResizable(false);
                stage.centerOnScreen();
                stage.setTitle("BookWarm Library Management System");

                loadingPane.setVisible(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        zoomOut.play();
    }

    public void handleFailedLogin() {
        errorAccountNotify.setOpacity(1.0);
        forgotPasswordLabel.setVisible(false);
        Animation.playNotificationTimeline(errorAccountNotify, 3.0, "red");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3.5), event -> {
            forgotPasswordLabel.setVisible(true);
        }));
        timeline.play();
    }

    public String[] getMajor() {
        return major;
    }

    public void setMajor(String[] major) {
        this.major = major;
    }
}
