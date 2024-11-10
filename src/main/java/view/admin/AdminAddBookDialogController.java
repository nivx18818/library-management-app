package view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
        if (checkValidInfo()) {
            String[] bookData = new String[]{txtCoverURL.getText(), txtName.getText(),
                    txtType.getText(), txtAuthor.getText(), txtQuantity.getText(),
                    txtPublisher.getText(), txtPublishedDate.getText()};
            addBook(bookData);
        }
    }

    public void addBook(String[] bookData) {
        AdminGlobalFormController.insertBooksData(bookData);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                AdminBooksLayoutController controller = AdminBooksLayoutController.getInstance();
                if (controller.getSearchText().isEmpty()) {
                    List<String[]> data = new ArrayList<>() {{
                        add(AdminGlobalFormController.getLastBookDataFromDatabase());
                    }};
                    controller.preloadData(data);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
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

    public boolean checkValidInfo() throws IOException {
        String url = txtCoverURL.getText();
        String nameBook = txtName.getText();
        String publishedDate = txtPublishedDate.getText();
        String quantity = txtQuantity.getText();

        if (nameBook.isEmpty() || quantity.isEmpty() || url.isEmpty()) {
            notificationLabel.setText("Please fill in mandatory fields.");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "#ff0000");
            return false;
        }
        if (!RegExPatterns.bookCoverUrlPattern(url)) {
            notificationLabel.setText("URL is invalid or not an image link.");
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
        txtCoverURL.setText("");
        txtName.setText("");
        txtType.setText("");
        txtAuthor.setText("");
        txtQuantity.setText("");
        txtPublishedDate.setText("");
        txtPublisher.setText("");
    }

}
