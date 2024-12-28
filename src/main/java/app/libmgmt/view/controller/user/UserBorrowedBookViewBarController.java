package app.libmgmt.view.controller.user;

import java.text.SimpleDateFormat;

import app.libmgmt.model.Loan;
import app.libmgmt.model.Book;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserBorrowedBookViewBarController {

    @FXML
    private ImageView bookImage;

    @FXML
    private Label lblAmount;

    @FXML
    private Label lblDueDate;

    @FXML
    private Label lblIsbn;

    @FXML
    private Label lblName;

    public void setData(Loan loan, Book book, String amount) {
        lblIsbn.setText(book.getIsbn());

        Task<Image> imageLoadTask = new Task<>() {
            @Override
            protected Image call() {
                Image image = new Image(book.getCoverUrl());
                return image;
            }
        };

        imageLoadTask.setOnSucceeded(event -> bookImage.setImage(imageLoadTask.getValue()));
        imageLoadTask.setOnFailed(event -> System.out.println("Failed to load image: " + imageLoadTask.getException().getMessage()));

        new Thread(imageLoadTask).start();

        lblName.setText(book.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dueDateString = sdf.format(loan.getDueDate());
        lblDueDate.setText(dueDateString);
        lblAmount.setText(amount);
    }

}
