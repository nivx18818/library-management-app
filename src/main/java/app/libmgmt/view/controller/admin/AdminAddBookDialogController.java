package app.libmgmt.view.controller.admin;

import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import app.libmgmt.model.Book;
import app.libmgmt.service.external.GoogleBooksApiService;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.RegExPatterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import org.json.JSONObject;

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

    private long lastKeyPressTime = 0;
    private static final int DELAY = 3000;
    
    public void initialize() {
        System.out.println("Admin Add Book Dialog initialized");

        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                1000, 1));
        quantitySpinner.getValueFactory().setValue(0);
        quantitySpinner.setPromptText("Quantity*");

        container.setOnMouseClicked(
                event -> {
                    container.requestFocus();
                });

        txtName.textProperty().addListener((observable, oldValue, newValue) -> {
            lastKeyPressTime = System.currentTimeMillis();
            new Thread(() -> {
                try {
                    Thread.sleep(DELAY);
                    if (System.currentTimeMillis() - lastKeyPressTime >= DELAY) {
                        fetchBookSuggestions(newValue);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
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
        // data format: [coverURL, name, type, author, quantity, publisher, publishedDate]
        String[] newBook = new String[] { "0", bookData[0], bookData[1], bookData[2], bookData[3], bookData[4],
                bookData[5], bookData[6] };
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
                            // publishedDate] to match the format of preloadData method in
                            // AdminBooksLayoutController
                            Book lastBook = AdminGlobalController.getInstance().getObservableBookData().getLast();

                            String authorsString = String.join(", ", lastBook.getAuthors());
                            String categoriesString = String.join(", ", lastBook.getCategories());

                            String[] book_Data = new String[] { lastBook.getIsbn(), lastBook.getCoverUrl(),
                                    lastBook.getTitle(), categoriesString,
                                    authorsString, String.valueOf(lastBook.getAvailableCopies()),
                                    lastBook.getPublisher(), lastBook.getPublishedDate().toString() };
                            lastBook = new Book(book_Data);
                            add(lastBook);
                            System.out.println("Book added: Fac diu vai lc" + lastBook.toString());
                        }
                    };
                    controller.preloadData(data);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                notificationLabel.setText("Book added successfully." + bookData[7]);
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

    private void fetchBookSuggestions(String title) {
        if (title == null || title.trim().isEmpty()) {
            return;
        }
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                JSONObject response = GoogleBooksApiService.searchBook(title, 5);
                if (response != null && response.has("items")) {
                    var items = response.getJSONArray("items");

                    if (items.length() > 0) {
                        var book = items.getJSONObject(0).getJSONObject("volumeInfo");

                        String id = response.getJSONArray("items").getJSONObject(0).optString("id", "No ID");
                        String isbn = "No ISBN";
                        if (book.has("industryIdentifiers")) {
                            var identifiers = book.getJSONArray("industryIdentifiers");
                            for (int j = 0; j < identifiers.length(); j++) {
                                var identifier = identifiers.getJSONObject(j);
                                if (identifier.optString("type").equals("ISBN_13")) {
                                    isbn = identifier.optString("identifier", "No ISBN");
                                    break;
                                }
                            }
                        }

                        String coverURL = book.optJSONObject("imageLinks") != null
                                ? book.getJSONObject("imageLinks").optString("thumbnail", "No Cover URL") : "No Cover URL";
                        String name = book.optString("title", "No Title");
                        String authors = book.optJSONArray("authors") != null
                                ? String.join(", ", book.getJSONArray("authors").toList().toArray(new String[0]))
                                : "Unknown Author";
                        String type = book.optJSONArray("categories") != null
                                ? String.join(", ", book.getJSONArray("categories").toList().toArray(new String[0]))
                                : "Unknown Type";
                        String publisher = book.optString("publisher", "Unknown Publisher");
                        String publishedDate = book.optString("publishedDate", "Unknown Date");

                        String quantity = "1";

                        String[] data = new String[]{isbn.equals("No ISBN") ? id : isbn, coverURL, name, type, authors, quantity, publisher, publishedDate};
                        setData(data);
                    }
                }

                return null;
            }
            @Override
            protected void succeeded() {
                super.succeeded();
                Platform.runLater(() -> {
                    super.succeeded();
                    notificationLabel.setText("Book suggestion successfully.");
                    AnimationUtils.playNotificationTimeline(notificationLabel, 3, "#08a80d");
                });
            }
            @Override
            protected void failed() {
                super.failed();
                Platform.runLater(() -> notificationLabel.setText("Failed to fetch book suggestions."));
            }
        };

        new Thread(task).start();
    }

    public boolean checkValidInfo() throws IOException {
        String url = txtCoverURL.getText();
        String nameBook = txtName.getText();
        String quantity = quantitySpinner.getValue().toString();

        if (nameBook.isEmpty() || quantity.isEmpty() || url.isEmpty() || publishedDatePicker.getValue() == null) {
            notificationLabel.setText("Please fill in mandatory fields.");
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
        quantitySpinner.getValueFactory().setValue(0);
        publishedDatePicker.setValue(LocalDate.parse(data[7]));
        txtPublisher.setText(data[6]);
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

}
