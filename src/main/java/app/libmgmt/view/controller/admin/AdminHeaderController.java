package app.libmgmt.view.controller.admin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.DateTimeUtils;
import app.libmgmt.util.EnumUtils;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class AdminHeaderController {

    private static AdminHeaderController controller;

    @FXML
    private Text dateText;
    @FXML
    private Text nameUserText;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView settingImage;
    @FXML
    private Text timeText;
    @FXML
    private Text typeUserText;

    // Constructor to set the controller instance
    public AdminHeaderController() {
        controller = this;
    }

    // Singleton pattern to get the current instance
    public static AdminHeaderController getInstance() {
        return controller;
    }

    // Initialization method called when the view is loaded
    @FXML
    public void initialize() {
        AnimationUtils.fadeInDown(rootPane);
        setUserInformation("Lionel Ronaldo", "Admin");
        initializeDateAndTime();
    }

    // Method to set user information in the header
    private void setUserInformation(String userName, String userType) {
        nameUserText.setText(userName);
        typeUserText.setText(userType);
    }

    // Initializes and continuously updates the current date and time in the header
    private void initializeDateAndTime() {
        updateDateAndTime(); // Initial update of date and time
        startClock(); // Start the clock to continuously update time
    }

    // Updates the current date and time in the header
    private void updateDateAndTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");
        Locale locale = DateTimeUtils.locale;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTime = timeFormatter.format(currentTime.toLocalTime())
                + (currentTime.getHour() < 12 ? " AM" : " PM");
        String formattedDate = dateFormat.format(new Date());

        timeText.setText(formattedTime);
        dateText.setText(formattedDate);
    }

    // Starts a clock to update the time and date every second
    private void startClock() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");
        Locale locale = DateTimeUtils.locale;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime currentTime = LocalDateTime.now();
            String formattedTime = timeFormatter.format(currentTime.toLocalTime())
                    + (currentTime.getHour() < 12 ? " AM" : " PM");
            String formattedDate = dateFormat.format(new Date());

            timeText.setText(formattedTime);
            dateText.setText(formattedDate);
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    // Handles the click event on the settings icon
    @FXML
    private void handleSettingOnMouseClicked(MouseEvent event) {
        openSettingsDialog();
    }

    // Opens the settings dialog
    private void openSettingsDialog() {
        ChangeScene.openAdminPopUp(
                AdminGlobalController.getInstance().getStackPaneContainer(),
                "/fxml/change-credentials-dialog.fxml", null, EnumUtils.PopupList.CHANGE_CREDENTIALS);
    }

}
