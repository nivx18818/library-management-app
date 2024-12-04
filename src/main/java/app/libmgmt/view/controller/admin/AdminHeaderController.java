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
import app.libmgmt.util.DateUtils;
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
        setInformation();
        initializeDateAndTime();
    }

    // Method to set user information in the header
    public static void setInformation() {
        getInstance().nameUserText.setText("Hello, BookWorm Admin");
        getInstance().typeUserText.setText("Admin");
    }

    // Initializes and continuously updates the current date and time in the header
    private void initializeDateAndTime() {
        updateDateAndTime(); // Initial update of date and time
    }

    // Updates the current date and time in the header
    private void updateDateAndTime() {
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm");
        timeText.setText(
                time.format(java.time.LocalTime.now()) + " " + (LocalDateTime.now().getHour() < 12 ? "AM" : "PM"));

        Locale locale = DateUtils.locale;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());
        dateText.setText(date);

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime currentTime = LocalDateTime.now();
            timeText.setText(time.format(java.time.LocalTime.now()) + " " + (currentTime.getHour() < 12
                    ? "AM"
                    : "PM"));

            String newDate = dateFormat.format(new Date());
            dateText.setText(newDate);
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
                "/fxml/change-credentials-dialog.fxml", EnumUtils.PopupList.CHANGE_CREDENTIALS);
    }

}
