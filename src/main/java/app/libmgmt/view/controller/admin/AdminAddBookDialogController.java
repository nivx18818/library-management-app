package app.libmgmt.view.controller.admin;

import com.jfoenix.controls.JFXButton;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import app.libmgmt.model.Book;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.RegExPatterns;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.time.LocalDate;
import java.util.Date;

public class AdminAddBookDialogController {

    private static AdminAddBookDialogController controller;

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
    private String webReaderUrl;

    public AdminAddBookDialogController() {
        controller = this;
    }

    public static AdminAddBookDialogController getInstance() {
        return controller;
    }

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
                    txtPublisher.getText(), publishedDatePicker.getValue().toString(), webReaderUrl };
            addBook(bookData);
        }
    }

    public void addBook(String[] bookData) {
        // data format: [coverURL, name, type, author, quantity, publisher,
        // publishedDate]
        String[] newBook = new String[] { "0", bookData[0], bookData[1], bookData[2], bookData[3], bookData[4],
                bookData[5], bookData[6], bookData[7] };
        AdminGlobalController adminGlobalController = AdminGlobalController.getInstance();
        adminGlobalController.insertBooksData(newBook);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                AdminBooksLayoutController controller = AdminBooksLayoutController.getInstance();
                if (controller.getSearchText().isEmpty()) {
                    List<Book> data = new ArrayList<>() {
                        {
                            // add id to the first index
                            // data format: [id, coverURL, name, type, author, quantity, publisher,
                            // publishedDate, webReaderUrl] to match the format of preloadData method in
                            // AdminBooksLayoutController
                            Book lastBook = AdminGlobalController.getInstance().getObservableBookData().getLast();

                            String authorsString = String.join(", ", lastBook.getAuthors());
                            String categoriesString = String.join(", ", lastBook.getCategories());

                            String formattedDate = "Not Available";
                            if (lastBook.getPublishedDate() != null) {
                                try {
                                    String inputPattern = "EEE MMM dd HH:mm:ss z yyyy";
                                    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
                                    String outputPattern = "yyyy-MM-dd";
                                    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                                    Date parsedDate = inputFormat.parse(lastBook.getPublishedDate().toString());
                                    formattedDate = outputFormat.format(parsedDate);
                                } catch (Exception e) {
                                    formattedDate = "Not Available";
                                }
                            }

                            String[] book_Data = new String[] { lastBook.getIsbn(), lastBook.getCoverUrl(),
                                    lastBook.getTitle(), categoriesString,
                                    authorsString, String.valueOf(lastBook.getAvailableCopies()),
                                    lastBook.getPublisher(), formattedDate, lastBook.getWebReaderUrl() };
                            lastBook = new Book(book_Data);
                            add(lastBook);
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

        if (RegExPatterns.datePattern(publishedDatePicker.getValue().toString())) {
            System.out.println(publishedDatePicker.getValue().toString());
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

    // [id/isbn, coverURL, name, type, author, quantity, publisher, publishedDate]
    public void setData(String[] data) {
        txtCoverURL.setText(data[1]);
        txtName.setText(data[2]);
        txtType.setText(data[3]);
        txtAuthor.setText(data[4]);
        quantitySpinner.getValueFactory().setValue(1);
        publishedDatePicker.setValue(LocalDate.parse(data[7]));
        txtPublisher.setText(data[6]);
        webReaderUrl = data[8];
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
