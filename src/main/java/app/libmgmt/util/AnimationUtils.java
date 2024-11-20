package app.libmgmt.util;

import animatefx.animation.*;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class AnimationUtils {

    public final static double HOVER_SCALE = 1.1;
    public final static double DEFAULT_SCALE = 1.0;
    public final static Duration SCALE_DURATION = Duration.millis(200);

    public static void playNotificationTimeline(Label label, double seconds, String color) {
        label.setStyle("-fx-text-fill: " + color + ";");
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), label);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(false);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), label);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(seconds));

        SequentialTransition sequentialTransition = new SequentialTransition(fadeIn, fadeOut);
        sequentialTransition.play();
    }

    public static ScaleTransition createScaleTransition(double scaleTo, Node node) {
        ScaleTransition scaleTransition = new ScaleTransition(SCALE_DURATION, node);

        scaleTransition.setToX(scaleTo);
        scaleTransition.setToY(scaleTo);

        return scaleTransition;
    }

    public static void fadeInRight(Pane pane, double speed) {
        FadeInRight fir = new FadeInRight(pane);
        fir.setSpeed(speed);
        fir.play();
    }

    public static void fadeInLeft(Pane pane, double speed) {
        FadeInLeft fir = new FadeInLeft(pane);
        fir.setSpeed(speed);
        fir.play();
    }

    public static void fadeInLeft(Pane pane) {
        FadeInLeft fil = new FadeInLeft(pane);
        fil.play();
    }

    public static void fadeInUp(Pane pane) {
        FadeInUp fiu = new FadeInUp(pane);
        fiu.play();
    }

    public static void fadeInDown(Pane pane) {
        FadeInDown fid = new FadeInDown(pane);
        fid.play();
    }

    public static void zoomIn(Pane pane, double speed) {
        ZoomIn zi = new ZoomIn(pane);
        zi.setSpeed(speed);
        zi.play();
    }

    public static void zoomOut(Pane pane, double speed) {
        ZoomOut zi = new ZoomOut(pane);
        zi.setSpeed(speed);
        zi.play();
    }

    public static void hoverCloseIcons(JFXButton closeDialogButton, ImageView imgClose) {
        Image hoverImg = new Image(AnimationUtils.class.getResource("/assets/icon/close-square2.png").toExternalForm());
        Image defaultImg = new Image(
                AnimationUtils.class.getResource("/assets/icon/close-square 1.png").toExternalForm());
        closeDialogButton.setOnMouseEntered(event -> imgClose.setImage(hoverImg));
        closeDialogButton.setOnMouseExited(event -> imgClose.setImage(defaultImg));
    }

}
