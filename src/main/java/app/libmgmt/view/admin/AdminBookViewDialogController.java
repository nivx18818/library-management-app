package app.libmgmt.view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;

public class AdminBookViewDialogController {

    private static AdminBookViewDialogController controller;

    @FXML
    private Label authorLabel;
    @FXML
    private Label idLabel;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private Pane cancelPane;
    @FXML
    private JFXButton closeDialogButton;
    @FXML
    private Pane closePane;
    @FXML
    private Pane container;
    @FXML
    private ImageView bookCoverImage;
    @FXML
    private ImageView imgClose;
    @FXML
    private Label lblCancel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label publishedDateLabel;
    @FXML
    private Label publisherLabel;
    @FXML
    private ImageView qrCodeImage;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label typeLabel;

    // Singleton pattern
    public AdminBookViewDialogController() {
        controller = this;
    }

    public static AdminBookViewDialogController getInstance() {
        return controller;
    }

    // Initialize method
    public void initialize() {
        System.out.println("AdminViewBookDialogController initialized!");
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    // Set book details data into the UI
    public void setData(String[] data) {
        // Set values to labels and load images
        setBookDetails(data);
    }

    private void setBookDetails(String[] data) {
        for (int i = 0; i < data.length; i++) {
            switch (i) {
                case 0:
                    idLabel.setText("Book ID : " + data[i]);
                    break;
                case 1:
                    loadImage(data[i]);
                    break;
                case 2:
                    nameLabel.setText("Name : " + data[i]);
                    break;
                case 3:
                    typeLabel.setText("Type : " + data[i]);
                    break;
                case 4:
                    authorLabel.setText("Author : " + data[i]);
                    break;
                case 5:
                    setQuantityLabel(data[i]);
                    break;
                case 6:
                    publisherLabel.setText("Publisher : " + data[i]);
                    break;
                case 7:
                    publishedDateLabel.setText("Published Date : " + data[i]);
                    break;
            }
        }
    }

    private void setQuantityLabel(String quantity) {
        quantityLabel.setText(quantity);
        if (Integer.parseInt(quantity) >= 1) {
            quantityLabel.setStyle("-fx-text-fill: green");
        } else {
            quantityLabel.setStyle("-fx-text-fill: red");
        }
    }

    private void loadImage(String path) {
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

    // Set QR Code image path
    public void setQrCodeImage(String path) {
        loadImage(path);
    }

    // Get QR Code image URL
    public String getQrCodeImagePath() {
        return qrCodeImage.getImage().getUrl();
    }

    // Cancel button action
    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    // Close button action
    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }
}
