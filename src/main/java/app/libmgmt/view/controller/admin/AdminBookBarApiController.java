package app.libmgmt.view.controller.admin;

import app.libmgmt.service.BookService;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils.PopupList;
import app.libmgmt.view.controller.EmptyDataNotificationDialogController;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AdminBookBarApiController {

    @FXML
    private Label authorLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label typeLabel;

    private String publisher;
    private String publishedDate;
    private String webReaderUrl;

    public void setData(String[] data) {
        isbnLabel.setText(data[0]);

        Task<Image> imageLoadTask = new Task<>() {
            @Override
            protected Image call() {
                Image image = new Image(data[1]);
                return image;
            }
        };

        imageLoadTask.setOnSucceeded(event -> bookImage.setImage(imageLoadTask.getValue()));
        imageLoadTask.setOnFailed(event -> System.out.println("Failed to load image: " + data[1]));

        new Thread(imageLoadTask).start();

        nameLabel.setText(data[2]);
        authorLabel.setText(data[3]);
        typeLabel.setText(data[4]);
        publisher = data[5];
        publishedDate = data[6];
        webReaderUrl = data[7];
    }

    @FXML
    void addBookButtonOnAction() {
        ChangeScene.openAdminPopUp(AdminBooksLayoutController.getInstance().getStackPaneContainer(),
                "/fxml/admin/admin-add-book-dialog.fxml", PopupList.ADD_BOOK);
        System.out.println(webReaderUrl);
        String[] data = { isbnLabel.getText(), bookImage.getImage().getUrl(), nameLabel.getText(), typeLabel.getText(),
                authorLabel.getText(), "1", publisher, publishedDate, webReaderUrl };
        BookService bookService = new BookService();
        if (bookService.getBookByIsbn(isbnLabel.getText()) == null) {
            AdminAddBookDialogController.getInstance().setData(data);
            System.out.println("ISBN: " + isbnLabel.getText());
        } else {
            ChangeScene.openAdminPopUp(AdminAddBookApiController.getInstance().getStackPaneContainer(),
                    "/fxml/empty-data-notification-dialog.fxml",
                    PopupList.EMPTY_DATA_NOTIFICATION);
            EmptyDataNotificationDialogController.getInstance().setNotificationLabel("This book already exists!");
        }
    }
}
