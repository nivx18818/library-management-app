package view.admin;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import util.Animation;
import util.RegExPatterns;

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

    public void initialize() {
        System.out.println("Admin Add Book Dialog initialized");

        container.setOnMouseClicked(
                event -> {
                    container.requestFocus();
                }
        );
    }

    @FXML
    void addButtonOnAction(ActionEvent event) {
        if (checkValid()) {
            String[] bookData = new String[]{txtID.getText(), txtCoverURL.getText(), txtName.getText(),
                    txtType.getText(), txtAuthor.getText(), "Available"};
            addBook(bookData);
        }
    }

    public void addBook(String[] bookData) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                List<String[]> booksData = new ArrayList<>();
                booksData.add(bookData);
                AdminBooksLayoutController.getInstance().preloadData(booksData);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                AdminGlobalFormController.getInstance().getBooksData().add(bookData);
                notificationLabel.setText("Book added successfully.");
                Animation.playNotificationTimeline(notificationLabel, 3, "#08a80d");
            }

            @Override
            protected void failed() {
                super.failed();
            }
        };

        new Thread(task).start();
        setDefault();
    }

    public boolean checkValid() {
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
            Animation.playNotificationTimeline(notificationLabel, 3, "#ff0000");
            return false;
        }
        RegExPatterns.checkUrlAsync(url, notificationLabel);
        if (notificationLabel.getText().equals("Invalid URL.")) {
            return false;
        } else if (!RegExPatterns.bookIDPattern(idBook)) {
            notificationLabel.setText("Invalid ID.");
            Animation.playNotificationTimeline(notificationLabel, 3, "#ff0000");
            return false;
        } else if (Integer.parseInt(quantity) < 0) {
            notificationLabel.setText("Invalid quantity.");
            Animation.playNotificationTimeline(notificationLabel, 3, "#ff0000");
            return false;
        } else if (!publishedDate.isEmpty() && !RegExPatterns.datePattern(publishedDate)) {
            notificationLabel.setText("Invalid date.");
            Animation.playNotificationTimeline(notificationLabel, 3, "#ff0000");
            return false;
        }
        return true;
    }

    @FXML
    void authorOnAction(ActionEvent event) {

    }


    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        setDefault();
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        AdminNavigationController.closePopUp();
    }

    @FXML
    void coverURLOnAction(ActionEvent event) {

    }

    @FXML
    void idOnAction(ActionEvent event) {

    }

    @FXML
    void publishedDateOnAction(ActionEvent event) {

    }

    @FXML
    void publisherOnAction(ActionEvent event) {

    }

    @FXML
    void quantityOnAction(ActionEvent event) {

    }

    @FXML
    void txtNameOnAction(ActionEvent event) {

    }

    @FXML
    void txtNameOnKeyPressed(KeyEvent event) {

    }

    @FXML
    void typeOnAction(ActionEvent event) {

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
