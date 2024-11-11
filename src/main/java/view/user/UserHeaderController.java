package view.user;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import util.AnimationUtils;
import util.DateTimeUtils;
import view.admin.AdminHeaderController;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class UserHeaderController {

    private static int initializeTimes;

    @FXML
    private Label dateLabel;

    @FXML
    private HBox containerSettingBox;

    @FXML
    private HBox containerUserBox;

    @FXML
    private Label nameUserLabel;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView settingImage;

    @FXML
    private Label timeLabel;

    private static UserHeaderController controller;

    public UserHeaderController() {
        controller = this;
    }

    public static UserHeaderController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        ++initializeTimes;

        showAnimation();

        setDateAndTimeHeader();
    }

    public static void setInformation() {
        getInstance().nameUserLabel.setText("Hoang Duy Thinh");
    }

    public void setDateAndTimeHeader() {
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm");
        timeLabel.setText(time.format(java.time.LocalTime.now()) + " " + (LocalDateTime.now().getHour() < 12 ? "AM" : "PM"));

        Locale locale = DateTimeUtils.locale;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());
        dateLabel.setText(date);

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime currentTime = LocalDateTime.now();
            timeLabel.setText(time.format(java.time.LocalTime.now()) + " " + (currentTime.getHour() < 12
                    ? "AM" : "PM"));

            String newDate = dateFormat.format(new Date());
            dateLabel.setText(newDate);
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    @FXML
    void handleSettingOnMouseClicked(MouseEvent event) {
        //TODO: Implement setting on mouse clicked
    }

    @FXML
    void settingImageOnMouseEntered(MouseEvent event) {
        settingImage.setImage(new Image(getClass().getResource("/assets/icon/setting2.png").toExternalForm()));
    }

    @FXML
    void settingImageOnMouseExited(MouseEvent event) {
        settingImage.setImage(new Image(getClass().getResource("/assets/icon/setting1.png").toExternalForm()));
    }

    public void showAnimation() {
        if (initializeTimes == 1) {
            AnimationUtils.fadeInDown(rootPane);
        }
    }

}
