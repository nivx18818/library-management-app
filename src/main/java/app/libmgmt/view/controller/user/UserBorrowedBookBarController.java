package app.libmgmt.view.controller.user;

import java.time.LocalDate;

import app.libmgmt.service.BookService;
import app.libmgmt.util.DateUtils;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserBorrowedBookBarController {

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label dueDateLabel;

    @FXML
    private Label orderLabel;

    @FXML
    private Spinner<Integer> amountSpinner;
    private String bookId;

    public void setData(String[] data) {
        // Form of global data: [id, imgPath, name, type, author, quantity,
        // publisher,publishedDate]
        // Form borrowed book bar data: [orderNumber, book Image, name, due date]
        bookId = data[0];
        try {
            uploadImageAsync(data[1], bookImage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        bookNameLabel.setText(data[2]);
        dueDateLabel.setText(getDueDate());
        setUpSpinner();
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
        LocalDate today = DateUtils.currentLocalTime;
    
        // Add 14 days to the current date = due date
        LocalDate dueDate = today.plusDays(14);
    
        // Format the due date to dd/MM/yyyy
        return DateUtils.parseLocalDateToString(dueDate);
    }

    public void setOrderNumber(int orderNumber) {
        orderLabel.setText(String.valueOf(orderNumber));
    }

    private void setUpSpinner() {
        BookService bookService = new BookService();
        int maxAmount = bookService.getBookByIsbn(bookId).getAvailableCopies();
        amountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
        maxAmount, 1));
        amountSpinner.setPromptText("Quantity*");
        amountSpinner.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        }));
    }

    public void setMaxAmount(int maxAmount) {
        amountSpinner.getValueFactory().setValue(maxAmount);
    }

    public int getAmount() {
        return amountSpinner.getValue();
    }

}
