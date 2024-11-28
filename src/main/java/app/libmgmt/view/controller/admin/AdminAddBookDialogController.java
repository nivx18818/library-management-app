package app.libmgmt.view.controller.admin;

import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.RegExPatterns;

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
    private DatePicker publishedDatePicker;
    @FXML
    private TextField txtPublisher;
    @FXML
    private Spinner<Integer> quantitySpinner;
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

        setUpSpinner();

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);

        container.setOnMouseClicked(
                event -> {
                    container.requestFocus();
                });
    }

    @FXML
    void addButtonOnAction(ActionEvent event) throws IOException {
        if (checkValidInfo()) {
            String[] bookData = new String[] { txtCoverURL.getText(), txtName.getText(),
                    txtType.getText(), txtAuthor.getText(), quantitySpinner.getValue().toString(),
                    txtPublisher.getText(), publishedDatePicker.getValue().toString() };
            addBook(bookData);
        }
    }

    public void addBook(String[] bookData) {
        // data format: [coverURL, name, type, author, quantity, publisher,
        // publishedDate]
        AdminGlobalController.getInstance().insertBooksData(bookData);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                AdminBooksLayoutController controller = AdminBooksLayoutController.getInstance();
                if (controller.getSearchText().isEmpty()) {
                    List<String[]> data = new ArrayList<>() {
                        {
                            // add id to the first index
                            // data format: [id, coverURL, name, type, author, quantity, publisher,
                            // publishedDate] to match the format of preloadData method in
                            // AdminBooksLayoutController
                            add(AdminGlobalController.getInstance().getObservableBookData().getLast());
                        }
                    };
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

        if (nameBook.isEmpty() || quantitySpinner.getValue() == null || url.isEmpty()
                || publishedDatePicker.getValue() == null) {
            notificationLabel.setText("Please fill in mandatory fields.");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "#ff0000");
            return false;
        }

        if (!RegExPatterns.datePattern(publishedDatePicker.getValue().toString())) {
            notificationLabel.setText("Date is invalid. Please follow the format dd/MM/yyyy.");
            AnimationUtils.playNotificationTimeline(notificationLabel, 3, "#ff0000");
            return false;
        }

        if (!RegExPatterns.bookCoverUrlPattern(url)) {
            notificationLabel.setText("URL is invalid or not an image link.");
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
        quantitySpinner.getValueFactory().setValue(0);
        publishedDatePicker.setValue(null);
        txtPublisher.setText("");
    }

    public void setUpSpinner() {
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                999, 0));
        quantitySpinner.getValueFactory().setValue(0);
        quantitySpinner.setPromptText("Quantity*");
        quantitySpinner.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        }));
    }

}
