package app.libmgmt.view.controller.user;

import java.time.LocalDate;

import app.libmgmt.util.DateTimeUtils;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class UserBorrowedBookBarController {

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private ImageView deleteImage;

    @FXML
    private Label dueDateLabel;

    @FXML
    private Label orderLabel;

    public void setData(String[] data) {
        // Form of global data: [id, imgPath, name, type, author, quantity,
        // publisher,publishedDate]
        // Form borrowed book bar data: [orderNumber, book Image, name, due date]

        try {
            uploadImageAsync(data[1], bookImage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        bookNameLabel.setText(data[2]);
        dueDateLabel.setText(getDueDate());
    }

    private void uploadImageAsync(String newImagePath, ImageView bookImage) {
        // Load the image asynchronously
        Task<Image> imageLoadTask = new Task<>() {
            @Override
            protected Image call() {
                Image image = new Image(newImagePath);
                return image;
            }
        };

        imageLoadTask.setOnSucceeded(event -> bookImage.setImage(imageLoadTask.getValue()));
        imageLoadTask.setOnFailed(event -> System.out.println("Failed to load image: " + newImagePath));

        new Thread(imageLoadTask).start();
    }

    public String getDueDate() {
        LocalDate today = DateTimeUtils.currentLocalTime;
    
        // Add 14 days to the current date = due date
        LocalDate dueDate = today.plusDays(14);
    
        // Format the due date to dd/MM/yyyy
        return DateTimeUtils.convertLocalDateToString(dueDate);
    }

    @FXML
    void imgOnMouseClicked(MouseEvent event) {

    }

    @FXML
    void imgOnMouseEntered(MouseEvent event) {

    }

    @FXML
    void imgOnMouseExited(MouseEvent event) {

    }

    public void setOrderNumber(int orderNumber) {
        orderLabel.setText(String.valueOf(orderNumber));
    }

}
