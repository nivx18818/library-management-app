package view.admin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import util.AnimationUtils;
import util.ChangeScene;
import util.DateTimeUtils;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class AdminHeaderController {

    private static int initializeTimes;
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

    public AdminHeaderController() {
        controller = this;
    }

    public static AdminHeaderController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        ++initializeTimes;

        showAnimation();

        setInformation();

        setDateAndTimeHeader();
    }

    public static void setInformation() {
        getInstance().nameUserText.setText("Lionel Ronaldo");
        getInstance().typeUserText.setText("Admin");
    }

    // Set current date and time in the header
    public void setDateAndTimeHeader() {
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm");
        timeText.setText(time.format(java.time.LocalTime.now()) + " " + (LocalDateTime.now().getHour() < 12 ? "AM" : "PM"));

        Locale locale = DateTimeUtils.locale;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());
        dateText.setText(date);

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime currentTime = LocalDateTime.now();
            timeText.setText(time.format(java.time.LocalTime.now()) + " " + (currentTime.getHour() < 12 ? "AM" : "PM"));

            String newDate = dateFormat.format(new Date());
            dateText.setText(newDate);
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    @FXML
    void handleSettingOnMouseClicked(MouseEvent event) {
        ChangeScene.openAdminPopUp(AdminGlobalFormController.getInstance().
                getStackPaneContainer(), "/fxml/change-credentials-dialog.fxml");
    }

    public void showAnimation() {
        if (initializeTimes == 1) {
            AnimationUtils.fadeInDown(rootPane);
        }
    }
}
