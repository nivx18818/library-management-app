package app.libmgmt.view.controller.user;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.google.zxing.WriterException;
import com.jfoenix.controls.JFXCheckBox;

import app.libmgmt.model.Book;
import app.libmgmt.model.Loan;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.view.controller.admin.AdminBookViewDialogController;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class UserReturnedBookViewBarController {

    @FXML
    private Label amountLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private JFXCheckBox checkBoxButton;

    @FXML
    private Label nameBookLabel;

    @FXML
    private ImageView viewImage;

    private Book book;

    public void setData(Loan loan, Book book, String amount) {
        this.book = book;
        Task<Image> imageLoadTask = new Task<>() {
            @Override
            protected Image call() {
                Image image = new Image(book.getCoverUrl());
                return image;
            }
        };

        imageLoadTask.setOnSucceeded(event -> bookImage.setImage(imageLoadTask.getValue()));
        imageLoadTask.setOnFailed(
                event -> System.out.println("Failed to load image: " + imageLoadTask.getException().getMessage()));

        new Thread(imageLoadTask).start();

        nameBookLabel.setText(book.getTitle());
        amountLabel.setText(amount);
        if (book.getAvailableCopies() == 0) {
            checkBoxButton.setDisable(true);
        }
    }

    @FXML
    void btnViewOnMouseClicked(MouseEvent event) {
        ChangeScene.openAdminPopUp(UserGlobalController.getInstance().getStackPaneContainer(), "/fxml/admin/admin-book-view-dialog.fxml", book.getIsbn(), EnumUtils.PopupList.BOOK_VIEW);

        String[] bookData = {
            book.getIsbn(),
            book.getCoverUrl(),
            book.getTitle(),
            String.join(", ", book.getCategories()),
            String.join(", ", book.getAuthors()),
            String.valueOf(book.getAvailableCopies()),
            book.getPublisher(),
            new SimpleDateFormat("yyyy-MM-dd").format(book.getPublishedDate()),
            book.getWebReaderUrl()
        };

        try {
            AdminBookViewDialogController.getInstance().setData(bookData);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnViewOnMouseEntered(MouseEvent event) {
        viewImage.setImage(new Image(getClass().getResource("/assets/icon/Property 1=Variant2.png").toExternalForm()));
    }

    @FXML
    void btnViewOnMouseExited(MouseEvent event) {
        viewImage.setImage(new Image(getClass().getResource("/assets/icon/btn view.png").toExternalForm()));
    }

}
