package app.libmgmt.view.admin;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.DateTimeUtils;
import app.libmgmt.util.RegExPatterns;
import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class AdminBookEditDialogController {

    private static AdminBookEditDialogController controller;
    private UpdateType currentUpdateType = UpdateType.BASIC_INFO;
    private UpdateType lastUpdateType = UpdateType.BASIC_INFO;
    @FXML
    private TextField authorTextField;
    @FXML
    private HBox basicInfoContainer;
    @FXML
    private Pane bookCoverContainer;
    @FXML
    private ImageView bookCoverImage;
    @FXML
    private Label bookCoverLabel;
    @FXML
    private JFXButton closeDialogButton;
    @FXML
    private Pane container;
    @FXML
    private ImageView imgClose;
    @FXML
    private TextField imgUrlTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private DatePicker publishedDatePicker;
    @FXML
    private TextField publisherTextField;
    @FXML
    private Spinner<Integer> quantitySpinner = new Spinner<>();
    @FXML
    private TextField typeTextField;
    @FXML
    private JFXButton updateButton;
    @FXML
    private Pane updatePane;
    @FXML
    private ImageView qrCodeImage;
    @FXML
    private Label notificationLabel;
    @FXML
    private TextField qrCodeTextField;
    @FXML
    private JFXButton basicInfoButton;
    @FXML
    private JFXButton bookCoverButton;
    @FXML
    private JFXButton qrCodeButton;
    private String[] originalData;
    private String lastImageURL;
    private String lastQrCodeURL;

    public AdminBookEditDialogController() {
        controller = this;
    }

    public static AdminBookEditDialogController getInstance() {
        return controller;
    }

    public void initialize() {
        System.out.println("AdminBookEditDialogController initialized");

        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
        quantitySpinner.setStyle("-fx-font-size: 16px;");

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void updateButtonOnAction(ActionEvent event) {
        String[] updatedData = new String[]{
                originalData[0],
                imgUrlTextField.getText().equals("") ? originalData[1] : imgUrlTextField.getText(),
                nameTextField.getText(),
                typeTextField.getText(),
                authorTextField.getText(),
                quantitySpinner.getValue().toString(),
                publisherTextField.getText(),
                publishedDatePicker.getValue() == null ? "" :
                        DateTimeUtils.convertDateToString(publishedDatePicker.getValue())
        };

        boolean hasChanges = false;

        for (int i = 0; i < updatedData.length; i++) {
            if (!updatedData[i].equals(originalData[i])) {
                hasChanges = true;
                break;
            }
        }

        if (hasChanges) {
            updateBookData(updatedData);
        } else {
            notificationLabel.setText("No changes detected.");
            notificationLabel.setStyle("-fx-text-fill: #ff0000;");
            AnimationUtils.playNotificationTimeline(notificationLabel, 1, "#ff0000");
        }

    }

    private void updateBookData(String[] updatedData) {
        // Update book in global data and database
        AdminGlobalController.getInstance().updateBookData(updatedData, originalData[0]);

        originalData = updatedData;

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.7), e -> {
            updateButton.setDisable(true);
        }));
        timeline.setOnFinished(e -> {
            notificationLabel.setText("Book updated successfully!");
            updateButton.setDisable(false);
            AnimationUtils.playNotificationTimeline(notificationLabel, 1, "#08a80d");
        });
        timeline.play();
    }

    @FXML
    void uploadButtonOnAction(ActionEvent event) {
        String imgUrl = UpdateType.BOOK_COVER.equals(currentUpdateType) ?
                imgUrlTextField.getText() : qrCodeTextField.getText();

        String lastUrl = UpdateType.BOOK_COVER.equals(currentUpdateType) ? lastImageURL : lastQrCodeURL;
        if (imgUrl.equals(lastUrl)) {
            return;
        }

        if (RegExPatterns.bookCoverUrlPattern(imgUrl)) {
            if (UpdateType.BOOK_COVER.equals(currentUpdateType)) {
                setBookCoverImage(imgUrl);
                lastImageURL = imgUrl;
            } else {
                setQrCodeImage(imgUrl);
                lastQrCodeURL = imgUrl;
            }

            bookCoverLabel.setText("Valid URL");
            AnimationUtils.playNotificationTimeline(bookCoverLabel, 2, "#08a80d"); // Màu xanh
        } else {
            System.out.println("Invalid URL");
            bookCoverLabel.setText("Invalid URL");
            AnimationUtils.playNotificationTimeline(bookCoverLabel, 2, "#ff0000"); // Màu đỏ
        }
    }

    @FXML
    void basicInfoButtonOnAction(ActionEvent event) {
        currentUpdateType = UpdateType.BASIC_INFO;
        showPane();
        handleEffectButtonClicked();
    }

    @FXML
    void bookCoverButtonOnAction(ActionEvent event) {
        currentUpdateType = UpdateType.BOOK_COVER;
        showPane();
        handleEffectButtonClicked();
    }

    @FXML
    void qrCodeButtonOnAction(ActionEvent event) {
        currentUpdateType = UpdateType.QR_CODE;
        showPane();
        handleEffectButtonClicked();
    }

    public void showOriginalBookData(String[] data) {
        System.out.println("Showing original book data");
        originalData = data;
        setOriginalBasicInfo();
        setBookCoverImage(originalData[1]);
        setQrCodeImage(null);
    }

    private void setOriginalBasicInfo() {
        nameTextField.setText(originalData[2]);
        typeTextField.setText(originalData[3]);
        authorTextField.setText(originalData[4]);
        quantitySpinner.getValueFactory().setValue(Integer.parseInt(originalData[5]));
        publisherTextField.setText(originalData[6]);
        publishedDatePicker.setValue(DateTimeUtils.convertStringToDate(originalData[7]));
    }

    public void setBookCoverImage(String path) {
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                return new Image(path);
            }
        };

        loadImageTask.setOnSucceeded(event -> bookCoverImage.setImage(loadImageTask.getValue()));

        loadImageTask.setOnFailed(event -> {
            Throwable exception = loadImageTask.getException();
            System.err.println("Failed to load image: " + exception.getMessage());
            exception.printStackTrace();
        });

        new Thread(loadImageTask).start();
    }

    public void setQrCodeImage(String path) {
        if (path == null) {
            return;
        }
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                return new Image(path);
            }
        };

        loadImageTask.setOnSucceeded(event -> qrCodeImage.setImage(loadImageTask.getValue()));

        loadImageTask.setOnFailed(event -> {
            Throwable exception = loadImageTask.getException();
            System.err.println("Failed to load image: " + exception.getMessage());
            exception.printStackTrace();
        });

        new Thread(loadImageTask).start();
    }

    public void showPane() {
        container.setOnMouseClicked(event -> {
            container.requestFocus();
        });

        if (lastUpdateType != null && lastUpdateType.equals(currentUpdateType)) {
            return;
        }

        lastUpdateType = currentUpdateType;

        switch (currentUpdateType) {
            case UpdateType.BASIC_INFO:
                basicInfoContainer.setVisible(true);
                bookCoverContainer.setVisible(false);
                AnimationUtils.zoomIn(basicInfoContainer, 1.0);
                break;
            case UpdateType.BOOK_COVER:
            case UpdateType.QR_CODE:
                if (currentUpdateType.equals(UpdateType.BOOK_COVER)) {
                    qrCodeTextField.setVisible(false);
                    imgUrlTextField.setVisible(true);
                    qrCodeImage.setVisible(false);
                    bookCoverImage.setVisible(true);
                } else {
                    qrCodeImage.setVisible(true);
                    bookCoverImage.setVisible(false);
                    qrCodeTextField.setVisible(true);
                    imgUrlTextField.setVisible(false);
                }
                basicInfoContainer.setVisible(false);
                bookCoverContainer.setVisible(true);
                AnimationUtils.zoomIn(bookCoverContainer, 1.0);
                break;
        }
    }

    public void handleEffectButtonClicked() {
        setDefaultButtonStyle();
        changeButtonStyle();
    }

    public void setDefaultButtonStyle() {
        basicInfoButton.setStyle("-fx-background-color: #000; -fx-text-fill: #fff;");
        bookCoverButton.setStyle("-fx-background-color: #000; -fx-text-fill: #fff;");
        qrCodeButton.setStyle("-fx-background-color: #000; -fx-text-fill: #fff;");
    }

    public void changeButtonStyle() {
        setDefaultButtonStyle();
        switch (lastUpdateType) {
            case UpdateType.BASIC_INFO:
                basicInfoButton.setStyle("-fx-background-color: #fff; -fx-text-fill: #000; " +
                        "-fx-background-radius: 0");
                break;
            case UpdateType.BOOK_COVER:
                bookCoverButton.setStyle("-fx-background-color: #fff; -fx-text-fill: #000; " +
                        "-fx-background-radius: 0");
                break;
            case UpdateType.QR_CODE:
                qrCodeButton.setStyle("-fx-background-color: #fff; -fx-text-fill: #000; " +
                        "-fx-background-radius: 0");
                break;
        }
    }

    enum UpdateType {
        BASIC_INFO,
        BOOK_COVER,
        QR_CODE
    }

}