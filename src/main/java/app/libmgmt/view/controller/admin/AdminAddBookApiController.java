package app.libmgmt.view.controller.admin;

import java.io.IOException;

import org.json.JSONObject;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.service.external.GoogleBooksApiService;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AdminAddBookApiController {

    @FXML
    private TextField apiSearchText;

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private Pane closePane;

    @FXML
    private ImageView imgClose;

    @FXML
    private Pane searchPane;

    @FXML
    private VBox vBoxBooksList;

    private Timeline debounceTimeline;

    @FXML
    public void initialize() {
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);

        debounceTimeline = new Timeline(new KeyFrame(Duration.millis(450), e -> performSearch()));
        debounceTimeline.setCycleCount(1);

        apiSearchText.setOnKeyTyped(event -> {
            debounceTimeline.stop();
            debounceTimeline.playFromStart();
        });
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    private void performSearch() {
        vBoxBooksList.getChildren().clear();
        String title = apiSearchText.getText();

        if (title == null || title.trim().isEmpty()) {
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                JSONObject response = GoogleBooksApiService.searchBook(title, 30);
                if (response != null && response.has("items")) {
                    var items = response.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        var book = items.getJSONObject(i).getJSONObject("volumeInfo");

                        String id = response.getJSONArray("items").getJSONObject(i).optString("id", "No ID");
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
                                ? book.getJSONObject("imageLinks").optString("thumbnail", "No Cover URL")
                                : "No Cover URL";
                        String name = book.optString("title", "No Title");
                        String authors = book.optJSONArray("authors") != null
                                ? String.join(", ", book.getJSONArray("authors").toList().toArray(new String[0]))
                                : "Unknown Author";
                        String type = book.optJSONArray("categories") != null
                                ? String.join(", ", book.getJSONArray("categories").toList().toArray(new String[0]))
                                : "Unknown Type";
                        String publisher = book.optString("publisher", "Unknown Publisher");

                        String publishedDate = book.optString("publishedDate", "Unknown Date");
                        if (publishedDate.length() == 4) {
                            publishedDate = publishedDate.concat("-01-01");
                        } else if (publishedDate.length() == 7) {
                            publishedDate = publishedDate.concat("-01");
                        } else if (publishedDate.equals("Unknown Date")) {
                            publishedDate = "Not Available";
                        }

                        String webReader = items.getJSONObject(i).getJSONObject("accessInfo") != null
                                ? items.getJSONObject(0).getJSONObject("accessInfo").optString("webReaderLink",
                                        "No Web Reader URL")
                                : "No Web Reader URL";

                        String[] data = new String[] { isbn.equals("No ISBN") ? id : isbn, coverURL, name, authors,
                                type, publisher, publishedDate, webReader };

                        loadBookBar(data);
                    }
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    private void loadBookBar(String[] data) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    AdminBooksLayoutController.class.getResource("/fxml/admin/admin-book-bar-api.fxml"));
            Pane scene = fxmlLoader.load();
            scene.setId(data[0]);
            AdminBookBarApiController controller = fxmlLoader.getController();
            controller.setData(data);

            // Set the controller as UserData for easy access later
            scene.setUserData(controller);

            Platform.runLater(() -> {
                vBoxBooksList.getChildren().add(scene);
                AnimationUtils.zoomIn(scene, 1.0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
