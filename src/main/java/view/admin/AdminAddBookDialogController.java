package view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import util.AnimationUtils;
import util.ChangeScene;
import util.RegExPatterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminAddBookDialogController {

    @FXML
    private Pane container;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtCoverURL;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPublishedDate;
    @FXML
    private TextField txtPublisher;
    @FXML
    private TextField txtQuantity;
    @FXML
    private TextField txtType;
    @FXML
    private Label notificationLabel;
    @FXML
    private JFXButton closeDialogButton;
    @FXML
    private ImageView imgClose;

    public void initialize() {
        System.out.println("Admin Add Book Dialog initialized");

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);

        container.setOnMouseClicked(
            event -> {
                container.requestFocus();
            }
        );
    }

    @FXML
    void addButtonOnAction(ActionEvent event) throws IOException {
        if (checkValid()) {
            String[] bookData = new String[]{txtID.getText(), txtCoverURL.getText(), txtName.getText(),
                    txtType.getText(), txtAuthor.getText(), txtQuantity.getText(),
                    txtPublisher.getText(), txtPublishedDate.getText()};
            addBook(bookData);
        }
    }

    public void addBook(String[] bookData) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                AdminBooksLayoutController adminBooksLayoutController = AdminBooksLayoutController.getInstance();
                System.out.println(adminBooksLayoutController.getSearchText());
                if (adminBooksLayoutController.getSearchText().isEmpty()) {
                    List<String[]> booksData = new ArrayList<>() {{
                        add(bookData);
                    }};
                    adminBooksLayoutController.preloadData(booksData);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                AdminGlobalFormController.getInstance().getBooksData().add(bookData);
                notificationLabel.setText("Book added successfully.");
                AnimationUtils.playNotificationTimeline(notificationLabel, 3, "#08a80d");
            }

            @Override
            protected void failed() {
                super.failed();
            }
        };

        new Thread(task).start();
        setDefault();
    }

    public boolean checkValid() throws IOException {
//        String urlOriginalPromptText = txtCoverURL.getPromptText();
        String idBook = txtID.getText();
        String url = txtCoverURL.getText();
        String nameBook = txtName.getText();
//        String typeBook = txtType.getText();
//        String author = txtAuthor.getText();
        String publishedDate = txtPublishedDate.getText();
        String quantity = txtQuantity.getText();

        if (idBook.isEmpty() || nameBook.isEmpty() || quantity.isEmpty() || url.isEmpty()) {
            notificationLabel.setText("Please fill in mandatory fields.");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "#ff0000");
            return false;
        }
        if (!RegExPatterns.bookCoverUrlPattern(url)) {
            notificationLabel.setText("URL is invalid or not an image link.");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "#ff0000");
            return false;
        } else if (!RegExPatterns.bookIDPattern(idBook)) {
            notificationLabel.setText("Invalid ID.");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "#ff0000");
            return false;
        } else if (Integer.parseInt(quantity) < 0) {
            notificationLabel.setText("Invalid quantity.");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "#ff0000");
            return false;
        } else if (!publishedDate.isEmpty() && !RegExPatterns.datePattern(publishedDate)) {
            notificationLabel.setText("Invalid date.");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "#ff0000");
            return false;
        }
        return true;
    }


    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        setDefault();
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    public void setDefault() {
        txtID.setText("");
        txtCoverURL.setText("");
        txtName.setText("");
        txtType.setText("");
        txtAuthor.setText("");
        txtQuantity.setText("");
        txtPublishedDate.setText("");
        txtPublisher.setText("");
    }

}
