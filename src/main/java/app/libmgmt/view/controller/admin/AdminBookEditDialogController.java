package app.libmgmt.view.controller.admin;

import java.io.IOException;

import com.google.zxing.WriterException;
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
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.DateUtils;
import app.libmgmt.util.QRCodeGenerator;
import app.libmgmt.util.RegExPatterns;

public class AdminBookEditDialogController {

    private static AdminBookEditDialogController controller;
    private UpdateType currentUpdateType = UpdateType.BASIC_INFO;
    private UpdateType lastUpdateType = UpdateType.BASIC_INFO;

    // UI Components
    @FXML
    private TextField authorTextField, imgUrlTextField, nameTextField, publisherTextField, typeTextField,
            qrCodeTextField;
    @FXML
    private HBox basicInfoContainer;
    @FXML
    private Pane bookCoverContainer, container;
    @FXML
    private ImageView bookCoverImage, imgClose, qrCodeImage;
    @FXML
    private Label bookCoverLabel, notificationLabel;
    @FXML
    private JFXButton closeDialogButton, updateButton, basicInfoButton, bookCoverButton, qrCodeButton;
    @FXML
    private DatePicker publishedDatePicker;
    @FXML
    private Spinner<Integer> quantitySpinner = new Spinner<>();
    @FXML
    private HBox hBoxImageNotification;
    @FXML
    private Label imageNotificationLabel;

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
        setUpSpinner();

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    // Action Methods
    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void updateButtonOnAction(ActionEvent event) throws IOException {
        if (checkValidFields()) {
            String[] updatedData = getUpdatedData();
            if (isDataChanged(updatedData)) {
                updateBookData(updatedData);
            } else {
                showNoChangesNotification();
            }
        } else {
            return;
        }

    }

    @FXML
    void uploadButtonOnAction(ActionEvent event) {
        String imgUrl = currentUpdateType.equals(UpdateType.BOOK_COVER) ? imgUrlTextField.getText()
                : qrCodeTextField.getText();
        String lastUrl = currentUpdateType.equals(UpdateType.BOOK_COVER) ? lastImageURL : lastQrCodeURL;

        if (!imgUrl.equals(lastUrl)) {
            updateImage(currentUpdateType, imgUrl);
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
        lastImageURL = originalData[1];
        imgUrlTextField.setText(originalData[1]);
        try {
            qrCodeImage.setImage(QRCodeGenerator.generateQRCode(originalData[8], 140, 140));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastQrCodeURL = originalData[8];
        qrCodeTextField.setText(originalData[8]);
    }

    private void setOriginalBasicInfo() {
        nameTextField.setText(originalData[2]);
        typeTextField.setText(originalData[3]);
        authorTextField.setText(originalData[4]);
        quantitySpinner.getValueFactory().setValue(Integer.parseInt(originalData[5]));
        publisherTextField.setText(originalData[6]);
        publishedDatePicker.setValue(DateUtils.parseStringToLocalDate(originalData[7]));
    }

    private String[] getUpdatedData() {
        return new String[] {
                originalData[0],
                imgUrlTextField.getText().isEmpty() ? originalData[1] : imgUrlTextField.getText(),
                nameTextField.getText(),
                typeTextField.getText(),
                authorTextField.getText(),
                quantitySpinner.getValue().toString(),
                publisherTextField.getText(),
                // TODO: Handle null exception
                publishedDatePicker.getValue().toString(),
                qrCodeTextField.getText().isEmpty() ? originalData[8] : qrCodeTextField.getText()
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
        AdminGlobalController.getInstance().updateBookData(updatedData);
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
            if (!RegExPatterns.bookCoverUrlPattern(url)) {
                showInvalidUrlNotification();
                return;
            }
            setBookCoverImage(url);
            bookCoverLabel.setText("Valid URL");
            AnimationUtils.playNotificationTimeline(bookCoverLabel, 2, "#08a80d");
            lastImageURL = url;
        } else if (updateType.equals(UpdateType.QR_CODE)) {
            try {
                qrCodeImage.setImage(QRCodeGenerator.generateQRCode(url, 140, 140));
            } catch (WriterException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setBookCoverImage(String path) {
        loadImageAsync(path, bookCoverImage);
    }

    private void loadImageAsync(String path, ImageView imageView) {
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                return new Image(path);
            }
        };

        loadImageTask.setOnSucceeded(event -> imageView.setImage(loadImageTask.getValue()));
        loadImageTask.setOnFailed(
                event -> System.err.println("Failed to load image: " + loadImageTask.getException().getMessage()));
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
        if (isBookCover) {
            imageNotificationLabel.setText("The image file must have one of the following extensions: .jpg, .jpeg, .png, .gif, .bmp, .svg.");
        } else {
            imageNotificationLabel.setText("The URL should be Web Reader link.");
        }
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

    public boolean checkValidFields() throws IOException {
        String quantityText = quantitySpinner.getEditor().getText();
        if (nameTextField.getText().isEmpty() ||
                publishedDatePicker.getValue() == null ||
                publishedDatePicker.getValue().toString().isEmpty() ||
                quantityText.isEmpty()) {

            notificationLabel.setText("Please fill in all required fields.");
            notificationLabel.setStyle("-fx-text-fill: #ff0000;");
            AnimationUtils.playNotificationTimeline(notificationLabel, 2, "#ff0000");
            return false;
        }

        if (!RegExPatterns.datePattern(publishedDatePicker.getValue().toString())) {
            notificationLabel.setText("Date is invalid. Please follow the format dd/MM/yyyy.");
            notificationLabel.setStyle("-fx-text-fill: #ff0000;");
            AnimationUtils.playNotificationTimeline(notificationLabel, 2, "#ff0000");
            return false;
        }

        return true;
    }

    public void setUpSpinner() {
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 0));
        quantitySpinner.setStyle("-fx-font-size: 16px;");
        quantitySpinner.setPromptText("Quantity*");
        quantitySpinner.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        }));
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
