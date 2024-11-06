package view.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import util.AnimationUtils;
import util.ChangeScene;
import util.RegExPatterns;

public class AdminBookEditDialogController {

    private static AdminBookEditDialogController controller;
    String currentUpdateType = UpdateType.BASIC_INFO.name();
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
    private Pane closePane;
    @FXML
    private Pane container;
    @FXML
    private JFXToggleButton editMode;
    @FXML
    private TextField idTextField;
    @FXML
    private ImageView imgClose;
    @FXML
    private TextField imgUrlTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField publishedDateITextField;
    @FXML
    private TextField publisherTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private ToggleGroup update;
    @FXML
    private JFXButton updateButton;
    @FXML
    private Pane updatePane;
    @FXML
    private JFXButton uploadButton;
    @FXML
    private ImageView qrCodeImage;
    @FXML
    private Label notificationLabel;
    @FXML
    private Pane refreshPane;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private TextField qrCodeTextField;
    private String[] originalData;
    private String lastImageURL;
    private String lastQrCodeURL;
    private String lastUpdateType;

    public AdminBookEditDialogController() {
        controller = this;
    }

    public static AdminBookEditDialogController getInstance() {
        return controller;
    }

    public void initialize() {
        System.out.println("AdminBookEditDialogController initialized");

        update.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                handleToggleGroupAction();
            }
        });

        editMode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setEditableFields(newValue);
        });
        setEditableFields(false);

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void updateButtonOnAction(ActionEvent event) {
        String[] updatedData = new String[]{
                idTextField.getText(),
                imgUrlTextField.getText().equals("") ? originalData[1] : imgUrlTextField.getText(),
                nameTextField.getText(),
                typeTextField.getText(),
                authorTextField.getText(),
                quantityTextField.getText(),
                publisherTextField.getText(),
                publishedDateITextField.getText()
        };

        boolean hasChanges = false;

//        if (!qrCodeTextField.getText().equals(AdminBookViewDialogController.getInstance().getQrCodeImagePath())) {
//            hasChanges = true;
//            AdminBookViewDialogController.getInstance().setQrCodeImage(qrCodeTextField.getText());
//        }

        for (int i = 0; i < updatedData.length; i++) {
            if (!updatedData[i].equals(originalData[i])) {
                hasChanges = true;
                break;
            }
        }

        if (hasChanges) {
            String[] bookData = AdminGlobalFormController.getInstance().getBookDataById(originalData[0]);
            if (bookData != null) {
                System.arraycopy(updatedData, 0, bookData, 0, updatedData.length);
                notificationLabel.setText("Update successful. Please refresh the page!");
                originalData = updatedData;
                notificationLabel.setStyle("-fx-text-fill: #08a80d;");
                updatePane.setVisible(false);
                updateButton.setDisable(true);
                refreshButton.setDisable(false);
                refreshPane.setVisible(true);
                AnimationUtils.zoomIn(refreshPane, 1.0);
            } else {
                notificationLabel.setText("Book not found!");
                notificationLabel.setStyle("-fx-text-fill: ff0000;");
            }
        } else {
            notificationLabel.setText("No changes detected.");
            notificationLabel.setStyle("-fx-text-fill: #ff0000;");
        }

        AnimationUtils.playNotificationTimeline(notificationLabel, 2,
                hasChanges ? "#08a80d" : "#ff0000");
    }

    @FXML
    void refreshButtonOnAction(ActionEvent event) {
        AdminBooksLayoutController.getInstance().refreshBooksList();
        notificationLabel.setText("Refreshing...");
        refreshButton.setDisable(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4), e -> {
            refreshPane.setVisible(false);
            updatePane.setVisible(true);
            updateButton.setDisable(false);
            AnimationUtils.zoomIn(updatePane, 1.0);
            notificationLabel.setText("Refreshed successfully.");
        }));
        timeline.play();
        AnimationUtils.playNotificationTimeline(notificationLabel, 6, "#08a80d");
    }

    @FXML
    void uploadButtonOnAction(ActionEvent event) {
        String imgUrl = UpdateType.BOOK_COVER.name().equals(currentUpdateType) ?
                imgUrlTextField.getText() : qrCodeTextField.getText();
        if (imgUrl.equals(UpdateType.BOOK_COVER.name().equals(currentUpdateType) ? lastImageURL : lastQrCodeURL)) {
            return;
        }
        if (RegExPatterns.bookCoverUrlPattern(imgUrl)) {
            if (UpdateType.BOOK_COVER.name().equals(currentUpdateType)) {
                setBookCoverImage(imgUrl);
            } else {
                setQrCodeImage(imgUrl);
            }
            bookCoverLabel.setText("Valid URL");
            if (UpdateType.BOOK_COVER.name().equals(currentUpdateType)) {
                lastImageURL = imgUrl;
            } else {
                lastQrCodeURL = imgUrl;
            }
        } else {
            System.out.println("Invalid URL");
            bookCoverLabel.setText("Invalid URL");
        }
        AnimationUtils.playNotificationTimeline(bookCoverLabel, 2, bookCoverLabel.getText().equals("Valid URL") ?
                "#08a80d" : "#ff0000");
    }

    public void handleToggleGroupAction() {
        JFXToggleButton selectedUpdateType = (JFXToggleButton) update.getSelectedToggle();
        if (selectedUpdateType.getText().equals("Basic Info")) {
            currentUpdateType = UpdateType.BASIC_INFO.name();
        } else if (selectedUpdateType.getText().equals("Book Cover")) {
            currentUpdateType = UpdateType.BOOK_COVER.name();
        } else if (selectedUpdateType.getText().equals("QR Code")) {
            currentUpdateType = UpdateType.QR_CODE.name();
        } else if (selectedUpdateType.getText().equals("QR Code")) {
            currentUpdateType = UpdateType.QR_CODE.name();
        }

        showPane();
    }

    public void showOriginalBookData(String[] data) {
        System.out.println("Showing original book data");
        originalData = data;
        setOriginalBasicInfo();
        setBookCoverImage(originalData[1]);
        setQrCodeImage(null);
    }

    private void setOriginalBasicInfo() {
        idTextField.setText(originalData[0]);
        nameTextField.setText(originalData[2]);
        typeTextField.setText(originalData[3]);
        authorTextField.setText(originalData[4]);
        quantityTextField.setText(originalData[5]);
        publisherTextField.setText(originalData[6]);
        publishedDateITextField.setText(originalData[7]);
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

    private void setEditableFields(boolean isEditable) {
        idTextField.setEditable(isEditable);
        nameTextField.setEditable(isEditable);
        typeTextField.setEditable(isEditable);
        authorTextField.setEditable(isEditable);
        quantityTextField.setEditable(isEditable);
        publisherTextField.setEditable(isEditable);
        publishedDateITextField.setEditable(isEditable);
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
            case "BASIC_INFO":
                basicInfoContainer.setVisible(true);
                bookCoverContainer.setVisible(false);
                AnimationUtils.zoomIn(basicInfoContainer, 1.0);
                break;
            case "BOOK_COVER":
            case "QR_CODE":
                if (currentUpdateType.equals("BOOK_COVER")) {
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

    enum UpdateType {
        BASIC_INFO,
        BOOK_COVER,
        QR_CODE
    }

}
