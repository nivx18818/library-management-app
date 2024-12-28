package app.libmgmt.view.controller.user;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.model.Book;
import app.libmgmt.model.Loan;
import app.libmgmt.service.LoanService;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.DateUtils;
import app.libmgmt.util.EnumUtils.PopupList;
import app.libmgmt.view.controller.EmptyDataNotificationDialogController;
import app.libmgmt.view.controller.admin.AdminBooksLayoutController;
import app.libmgmt.view.controller.user.UserBorrowedBooksConfirmationDialogController.BORROW_TYPE;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class UserReturnedBookViewDialogController {

    private static UserReturnedBookViewDialogController controller;

    @FXML
    private HBox hBoxReborrow;

    @FXML
    private JFXButton closeButton;

    @FXML
    private Label closeLabel;

    @FXML
    private Pane closePane;

    @FXML
    private Label lblBorrowedDate;

    @FXML
    private Label lblReturnId;

    @FXML
    private Label lblReturnedDate;

    @FXML
    private JFXButton reborrowButton;

    @FXML
    private ImageView reborrowImage;

    @FXML
    private Label reborrowLabel;

    @FXML
    private VBox vBox;

    private final String hoverReborrowLogo = "/assets/icon/acquire-logo-1.png";
    private final String defaultReborrowLogo = "/assets/icon/add-circle 1.png";

    public UserReturnedBookViewDialogController() {
        controller = this;
    }

    public static UserReturnedBookViewDialogController getInstance() {
        return controller;
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        vBox.getChildren().clear();
        closeButton.setDisable(true);
        closeLabel.setText("Closing...");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), e -> {
            closeLabel.setText("Closed!");
            ChangeScene.closePopUp();
        }));
        timeline.play();
    }

    @FXML
    void btnOnMouseEntered(MouseEvent event) {
        if (event.getSource() == closeButton) {
            closePane.setStyle(
                    "-fx-background-color: #d7d7d7; -fx-background-radius: 10;");
        } else if (event.getSource() == reborrowButton) {
            hBoxReborrow.setStyle(
                    "-fx-background-color: #F2F2F2; -fx-background-radius: 10; -fx-border-color: #000; -fx-border-radius: 10; -fx-border-width: 1.2;");
            reborrowLabel.setStyle("-fx-text-fill: #000;");
            reborrowImage.setImage(new Image(getClass().getResource(hoverReborrowLogo).toExternalForm()));
        }
    }

    @FXML
    void btnOnMouseExited(MouseEvent event) {
        if (event.getSource() == closeButton) {
            closePane.setStyle(
                    "-fx-background-color: #fff; -fx-background-radius: 10;");
        } else if (event.getSource() == reborrowButton) {
            hBoxReborrow.setStyle(
                    "-fx-background-color: #000; -fx-background-radius: 10;");
            reborrowLabel.setStyle("-fx-text-fill: #fff;");
            reborrowImage.setImage(new Image(getClass().getResource(defaultReborrowLogo).toExternalForm()));
        }
    }

    @FXML
    void btnReborrowOnAction(ActionEvent event) {
        StackPane stackPane = UserCatalogController.getInstance().getStackPaneContainer();
        if (getSelectedBooksList().isEmpty()) {
            ChangeScene.openAdminPopUp(stackPane, "/fxml/empty-data-notification-dialog.fxml", PopupList.EMPTY_DATA_NOTIFICATION);
        } else {
            long daysBetween = ChronoUnit.DAYS.between(DateUtils.parseStringToLocalDate(lblReturnedDate.getText()),
                    DateUtils.currentLocalTime);
            if (daysBetween >= 7) {
                UserBorrowedBooksConfirmationDialogController.borrowType = BORROW_TYPE.REBORROW;
                ChangeScene.openAdminPopUp(stackPane, "/fxml/user/user-borrowed-books-confirmation-dialog.fxml",
                        PopupList.ACQUIRE_BOOK);
            } else {
                ChangeScene.openAdminPopUp(stackPane, "/fxml/empty-data-notification-dialog.fxml", PopupList.EMPTY_DATA_NOTIFICATION);
                EmptyDataNotificationDialogController.getInstance().setNotificationLabel("You can only reborrow books after 7 days of returning.");
            }
        }
    }

    // Loads data asynchronously to prevent blocking the main thread.
    public void loadDataAsync(Loan loan) {
        lblReturnId.setText(String.valueOf(loan.getLoanId()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String borrowedDateString = sdf.format(loan.getBorrowedDate());
        String returnedDateString = sdf.format(loan.getReturnedDate());
        lblBorrowedDate.setText(borrowedDateString);
        lblReturnedDate.setText(returnedDateString);

        LoanService loanService = new LoanService();
        List<Book> books = loanService.getBookFromLoan(loan.getIsbn());
        String[] amountString = loan.getAmount().split(",\\s*");
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Format data: [cover photo, name book, amount]
                for (int i = 0; i < books.size(); i++) {
                    loadBookData(loan, books.get(i), amountString[i]);
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

    private void loadBookData(Loan loan, Book book, String amount) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminBooksLayoutController.class.getResource(
                    "/fxml/user/user-returned-book-view-bar.fxml"));
            Pane scene = fxmlLoader.load();
            UserReturnedBookViewBarController controller = fxmlLoader.getController();
            controller.setData(loan, book, amount);
            scene.setUserData(controller);

            Platform.runLater(() -> {
                vBox.getChildren().add(scene);
                AnimationUtils.zoomIn(scene, 1.0);
            });
        } catch (Exception e) {
            System.err.println("Error loading book data: " + e.getMessage());
        }
    }

    public List<Book> getSelectedBooksList() {
        List<Book> selectedBooks = new ArrayList<>();

        for (Node node : vBox.getChildren()) {
            if (node instanceof Pane) {
                Pane bookBar = (Pane) node;
                UserReturnedBookViewBarController controller = (UserReturnedBookViewBarController) bookBar
                        .getUserData();

                try {
                    if (controller.getCheckBoxButton().isSelected()) {
                        Book book = new Book(controller.getBookData());
                        selectedBooks.add(book);
                    }
                } catch (Exception e) {
                    System.out.println("Error getting selected books: " + e.getMessage());
                }
            }
        }

        return selectedBooks;
    }

}
