package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import util.Animation;
import util.DateTimeUtils;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class HeaderController {

    private static int initializeTimes;
    @FXML
    private ImageView avatarImage;
    @FXML
    private HBox containerSettingBox;
    @FXML
    private HBox containerUserBox;
    @FXML
    private Text dateText;
    @FXML
    private VBox infoUserBox;
    @FXML
    private Text nameUserText;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView settingImage;
    @FXML
    private VBox timeBox;
    @FXML
    private Text timeText;
    @FXML
    private Text typeUserText;
    @FXML
    private Line vLIne;

    @FXML
    public void initialize() {
        ++initializeTimes;

        showAnimation();

        nameUserText.setText("Lionel Ronaldo");
        typeUserText.setText("Admin");

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

    public void showAnimation() {
        if (initializeTimes == 1) {
            Animation.fadeInDown(rootPane);
        }
    }


}
