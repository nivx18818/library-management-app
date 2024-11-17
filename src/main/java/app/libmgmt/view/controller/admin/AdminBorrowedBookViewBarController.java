package app.libmgmt.view.controller.admin;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AdminBorrowedBookViewBarController {

    @FXML
    private Label dueDateLabel;
    @FXML
    private ImageView bookImage;
    @FXML
    private Label nameBookLabel;
    @FXML
    private Label authorBookLabel;

    public void setData(String path, String name, String author, String dueDate) {
        try {
            uploadImageAsync(path, bookImage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        nameBookLabel.setText(name);
        authorBookLabel.setText(author);
        dueDateLabel.setText(dueDate);
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

}
