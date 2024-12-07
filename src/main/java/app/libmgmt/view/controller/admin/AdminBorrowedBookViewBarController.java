package app.libmgmt.view.controller.admin;

import java.text.SimpleDateFormat;

import com.jfoenix.controls.JFXCheckBox;

import app.libmgmt.model.Book;
import app.libmgmt.model.Loan;
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
    private Label amountLabel;
    @FXML
    private JFXCheckBox checkBoxButton;

    public void setData(Book book, Loan loan, String amount) {
        try {
            uploadImageAsync(book.getCoverUrl(), bookImage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        nameBookLabel.setText(book.getTitle());
        amountLabel.setText(amount + "");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dueDateString = outputFormat.format(loan.getDueDate());
        dueDateLabel.setText(dueDateString);
        if (loan.getStatus().equals("RETURNED")) {
            checkBoxButton.setDisable(true);
        } else {
            checkBoxButton.setDisable(false);
        }
    }

    public JFXCheckBox getCheckBoxButton() {
        return checkBoxButton;
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
