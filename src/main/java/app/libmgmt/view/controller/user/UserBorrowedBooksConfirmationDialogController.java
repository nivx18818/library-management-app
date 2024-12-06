package app.libmgmt.view.controller.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.model.Book;
import app.libmgmt.model.Loan;
import app.libmgmt.service.LoanService;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.DateUtils;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.view.controller.admin.AdminBooksLayoutController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.util.Date;

public class UserBorrowedBooksConfirmationDialogController {

    private static UserBorrowedBooksConfirmationDialogController controller;

    @FXML
    private Label borrowedDateLabel;

    @FXML
    private JFXButton closeButton;

    @FXML
    private Label closeLabel;

    @FXML
    private Pane closePane;

    @FXML
    private JFXButton confirmButton;

    @FXML
    private Label confirmLabel;

    @FXML
    private Pane confirmPane;

    @FXML
    private Label totalBorrowedBooksLabel;

    @FXML
    private VBox vBoxSelectedBooksList;

    private List<Book> selectedBooksList;

    private List<Loan> newBorrowedBooksList;

    public UserBorrowedBooksConfirmationDialogController() {
        controller = this;
    }

    public static UserBorrowedBooksConfirmationDialogController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        selectedBooksList = UserBooksLayoutController.getInstance().getSelectedBooksList();
        newBorrowedBooksList = new ArrayList<>();

        borrowedDateLabel.setText(DateUtils.parseLocalDateToString(DateUtils.currentLocalTime));
        totalBorrowedBooksLabel.setText(selectedBooksList.size() + (selectedBooksList.size() > 1 ? " Books" : " Book"));

        preloadData();
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        vBoxSelectedBooksList.getChildren().clear();
        ChangeScene.closePopUp();
    }

    @FXML
    void btnConfirmOnAction(ActionEvent event) {
        confirmLabel.setText("Borrowing...");
        int idx = 0;
        for (javafx.scene.Node node : vBoxSelectedBooksList.getChildren()) {
            UserBorrowedBookBarController controller = (UserBorrowedBookBarController) node.getUserData();
            if (controller != null) {
                int amount = controller.getAmount();
                newBorrowedBooksList.get(idx++).setAmount(amount);
                System.out.println("Amount: " + amount);
            }
        }
        UserGlobalController.getInstance().addBorrowedBook(newBorrowedBooksList);
        confirmButton.setDisable(true);
        closeDialogAndNavigateToCatalog();
    }

    private void closeDialogAndNavigateToCatalog() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            confirmLabel.setText("Borrowed!");
        }));
        Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
            ChangeScene.closePopUp();
            UserCatalogController.currentStateUserCatalog = UserCatalogController.USER_CATALOG_STATE.BORROWED;
            try {
                UserNavigationController userNavigationController = UserNavigationController.getInstance();
                userNavigationController.handleNavigation(EnumUtils.NavigationButton.CATALOG, "user-catalog-form.fxml",
                        userNavigationController.getCatalogButton());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }));
        timeline.play();
        timeline2.play();
    }

    @FXML
    void btnOnMouseEntered(MouseEvent event) {
        if (event.getSource() == closeButton) {
            closePane.setStyle(
                    "-fx-background-color: #d7d7d7; -fx-background-radius: 10;");
        } else if (event.getSource() == confirmButton) {
            confirmPane.setStyle(
                    "-fx-background-color: #F2F2F2; -fx-background-radius: 10; -fx-border-color: #000; -fx-border-radius: 10; -fx-border-width: 1.2;");
            confirmLabel.setStyle("-fx-text-fill: #000;");
        }
    }

    @FXML
    void btnOnMouseExited(MouseEvent event) {
        if (event.getSource() == closeButton) {
            closePane.setStyle(
                    "-fx-background-color: #fff; -fx-background-radius: 10;");
        } else if (event.getSource() == confirmButton) {
            confirmPane.setStyle(
                    "-fx-background-color: #000; -fx-background-radius: 10;");
            confirmLabel.setStyle("-fx-text-fill: #fff;");
        }

    }

    // Loads data asynchronously to prevent blocking the main thread.
    private void preloadData() {
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int orderNumber = 1;
                for (Book d : selectedBooksList) {
                    String authorsString = String.join(", ", d.getAuthors());
                    String categoriesString = String.join(", ", d.getCategories());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String publishedDateStr = (d.getPublishedDate() != null) ? outputFormat.format(d.getPublishedDate())
                            : "Not Available";

                    String[] data = new String[] { d.getIsbn(), d.getCoverUrl(), d.getTitle(), categoriesString, authorsString,
                            String.valueOf(d.getAvailableCopies()), d.getPublisher(), publishedDateStr };
                    loadBookData(data, orderNumber++);
                }
                return null;
            }

            @Override
            protected void failed() {
                System.err.println("Error loading data: " + getException().getMessage());
                throw new RuntimeException(getException());
            }
        };
        new Thread(preloadTask).start();
    }

    /**
     * Loads a single book's data into the VBox.
     * 
     * @param bookData Array containing [imageURL, title, author, date].
     */
    private void loadBookData(String[] bookData, int orderNumber) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminBooksLayoutController.class.getResource(
                    "/fxml/user/user-borrowed-book-bar.fxml"));
            Pane scene = fxmlLoader.load();
            UserBorrowedBookBarController controller = fxmlLoader.getController();
            controller.setOrderNumber(orderNumber);
            controller.setData(bookData);
            scene.setUserData(controller);
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

            String borrowedDateText = borrowedDateLabel.getText();
            Date parsedDate = inputFormat.parse(borrowedDateText);
            String formattedDate = outputFormat.format(parsedDate);

            LoanService loanService = new LoanService();
            Loan newBorrowedBookData = new Loan(
                    loanService.getMaxLoanId() + orderNumber,
                    "23020604",
                    // UserGlobalController.getInstance().getUserLoginInfo().getUserId(),
                    bookData[0],
                    1,
                    DateUtils.parseStringToDate(formattedDate),
                    "BORROWED");
            newBorrowedBooksList.add(newBorrowedBookData);
            Platform.runLater(() -> {
                vBoxSelectedBooksList.getChildren().add(scene);
                AnimationUtils.zoomIn(scene, 1.0);
            });
        } catch (Exception e) {
            System.err.println("Error loading book data: " + e.getMessage());
        }
    }
}
