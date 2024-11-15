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

    // UI Components
    @FXML private TextField authorTextField, imgUrlTextField, nameTextField, publisherTextField, typeTextField, qrCodeTextField;
    @FXML private HBox basicInfoContainer;
    @FXML private Pane bookCoverContainer, container;
    @FXML private ImageView bookCoverImage, imgClose, qrCodeImage;
    @FXML private Label bookCoverLabel, notificationLabel;
    @FXML private JFXButton closeDialogButton, updateButton, basicInfoButton, bookCoverButton, qrCodeButton;
    @FXML private DatePicker publishedDatePicker;
    @FXML private Spinner<Integer> quantitySpinner = new Spinner<>();

    private String[] originalData;
    private String lastImageURL;
    private String lastQrCodeURL;

    public AdminBookEditDialogController() {
        controller = this;
    }

    public static AdminBookEditDialogController getInstance() {
        return controller;
    }

    // Initialization
    public void initialize() {
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
        quantitySpinner.setStyle("-fx-font-size: 16px;");
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    // Action Methods
    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void updateButtonOnAction(ActionEvent event) {
        String[] updatedData = getUpdatedData();
        if (isDataChanged(updatedData)) {
            updateBookData(updatedData);
        } else {
            showNoChangesNotification();
        }
    }

    @FXML
    void uploadButtonOnAction(ActionEvent event) {
        String imgUrl = currentUpdateType.equals(UpdateType.BOOK_COVER) ? imgUrlTextField.getText() : qrCodeTextField.getText();
        String lastUrl = currentUpdateType.equals(UpdateType.BOOK_COVER) ? lastImageURL : lastQrCodeURL;

        if (!imgUrl.equals(lastUrl)) {
            if (RegExPatterns.bookCoverUrlPattern(imgUrl)) {
                updateImage(currentUpdateType, imgUrl);
                bookCoverLabel.setText("Valid URL");
                AnimationUtils.playNotificationTimeline(bookCoverLabel, 2, "#08a80d");
            } else {
                showInvalidUrlNotification();
            }
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

    // Data Setup and Update
    public void showOriginalBookData(String[] data) {
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

    private String[] getUpdatedData() {
        return new String[]{
                originalData[0],
                imgUrlTextField.getText().isEmpty() ? originalData[1] : imgUrlTextField.getText(),
                nameTextField.getText(),
                typeTextField.getText(),
                authorTextField.getText(),
                quantitySpinner.getValue().toString(),
                publisherTextField.getText(),
                publishedDatePicker.getValue() == null ? "" : DateTimeUtils.convertDateToString(publishedDatePicker.getValue())
        };
    }

    private boolean isDataChanged(String[] updatedData) {
        for (int i = 0; i < updatedData.length; i++) {
            if (!updatedData[i].equals(originalData[i])) {
                return true;
            }
        }
        return false;
    }

    private void updateBookData(String[] updatedData) {
        AdminGlobalController.getInstance().updateBookData(updatedData, originalData[0]);
        originalData = updatedData;
        showUpdateSuccessNotification();
    }

    private void showUpdateSuccessNotification() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.7), e -> updateButton.setDisable(true)));
        timeline.setOnFinished(e -> {
            notificationLabel.setText("Book updated successfully!");
            updateButton.setDisable(false);
            AnimationUtils.playNotificationTimeline(notificationLabel, 1, "#08a80d");
        });
        timeline.play();
    }

    private void showNoChangesNotification() {
        notificationLabel.setText("No changes detected.");
        notificationLabel.setStyle("-fx-text-fill: #ff0000;");
        AnimationUtils.playNotificationTimeline(notificationLabel, 1, "#ff0000");
    }

    private void showInvalidUrlNotification() {
        bookCoverLabel.setText("Invalid URL");
        AnimationUtils.playNotificationTimeline(bookCoverLabel, 2, "#ff0000");
    }

    // Image Update Methods
    private void updateImage(UpdateType updateType, String url) {
        if (updateType.equals(UpdateType.BOOK_COVER)) {
            setBookCoverImage(url);
            lastImageURL = url;
        } else if (updateType.equals(UpdateType.QR_CODE)) {
            setQrCodeImage(url);
            lastQrCodeURL = url;
        }
    }

    private void setBookCoverImage(String path) {
        loadImageAsync(path, bookCoverImage);
    }

    private void setQrCodeImage(String path) {
        if (path != null) {
            loadImageAsync(path, qrCodeImage);
        }
    }

    private void loadImageAsync(String path, ImageView imageView) {
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                return new Image(path);
            }
        };

        loadImageTask.setOnSucceeded(event -> imageView.setImage(loadImageTask.getValue()));
        loadImageTask.setOnFailed(event -> System.err.println("Failed to load image: " + loadImageTask.getException().getMessage()));
        new Thread(loadImageTask).start();
    }

    // UI Logic Methods
    public void showPane() {
        container.setOnMouseClicked(event -> container.requestFocus());
        if (lastUpdateType.equals(currentUpdateType)) {
            return;
        }

        lastUpdateType = currentUpdateType;
        switch (currentUpdateType) {
            case BASIC_INFO:
                basicInfoContainer.setVisible(true);
                bookCoverContainer.setVisible(false);
                AnimationUtils.zoomIn(basicInfoContainer, 1.0);
                break;
            case BOOK_COVER:
            case QR_CODE:
                handleImagePaneVisibility();
                break;
        }
    }

    private void handleImagePaneVisibility() {
        boolean isBookCover = currentUpdateType.equals(UpdateType.BOOK_COVER);
        qrCodeTextField.setVisible(!isBookCover);
        imgUrlTextField.setVisible(isBookCover);
        qrCodeImage.setVisible(!isBookCover);
        bookCoverImage.setVisible(isBookCover);
        basicInfoContainer.setVisible(false);
        bookCoverContainer.setVisible(true);
        AnimationUtils.zoomIn(bookCoverContainer, 1.0);
    }

    public void handleEffectButtonClicked() {
        resetButtonStyles();
        changeSelectedButtonStyle();
    }

    private void resetButtonStyles() {
        basicInfoButton.setStyle("-fx-background-color: #000; -fx-text-fill: #fff;");
        bookCoverButton.setStyle("-fx-background-color: #000; -fx-text-fill: #fff;");
        qrCodeButton.setStyle("-fx-background-color: #000; -fx-text-fill: #fff;");
    }

    private void changeSelectedButtonStyle() {
        JFXButton selectedButton = getSelectedButton();
        selectedButton.setStyle("-fx-background-color: #fff; -fx-text-fill: #000; -fx-border-color: #000;");
    }

    private JFXButton getSelectedButton() {
        switch (currentUpdateType) {
            case BASIC_INFO:
                return basicInfoButton;
            case BOOK_COVER:
                return bookCoverButton;
            case QR_CODE:
                return qrCodeButton;
            default:
                return basicInfoButton;
        }
    }

    public enum UpdateType {
        BASIC_INFO, BOOK_COVER, QR_CODE
    }
}
