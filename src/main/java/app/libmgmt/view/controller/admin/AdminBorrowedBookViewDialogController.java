package app.libmgmt.view.controller.admin;

import com.jfoenix.controls.JFXButton;

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
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import app.libmgmt.model.Book;
import app.libmgmt.model.Loan;

import app.libmgmt.service.LoanService;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.util.EnumUtils.PopupList;

import java.util.ArrayList;
import java.util.List;

public class AdminBorrowedBookViewDialogController {

    private static AdminBorrowedBookViewDialogController controller;
    private final LoanService loanService = new LoanService();

    @FXML
    private Pane closePane;
    @FXML
    private Pane returnPane;
    @FXML
    private Label closeLabel;
    @FXML
    private Label returnLabel;
    @FXML
    private Label lblId;
    @FXML
    private Label lblUserId;
    @FXML
    private VBox vBox;
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton returnButton;
    @FXML
    private HBox hBoxReturn;
    @FXML
    private ImageView returnImage;

    public AdminBorrowedBookViewDialogController() {
        controller = this;
    }

    public static AdminBorrowedBookViewDialogController getInstance() {
        if (controller == null) {
            controller = new AdminBorrowedBookViewDialogController();
        }
        return controller;
    }

    @FXML
    public void initialize() {
        // listenLoanDataChanges();
    }

    // private void listenLoanDataChanges() {
    // AdminGlobalController.getInstance().getBorrowedBooksData()
    // .addListener((ListChangeListener.Change<? extends Loan> change) -> {
    // while (change.next()) {
    // if (change.wasUpdated() && change.getTo() != -1) {
    // List<? extends Loan> subList = change.getList().subList(change.getFrom(),
    // change.getTo());
    // subList.forEach(loan -> {
    // String bookId = loan.getIsbn();
    // vBox.getChildren().stream()
    // .filter(child -> child.getId() != null && child.getId().equals(bookId))
    // .findFirst()
    // .ifPresent(child -> Platform.runLater(() -> {
    // vBox.getChildren().remove(child);
    // }));
    // });
    // }
    // }
    // });
    // }

    public List<Book> getBooksData(String id) {
        return loanService.getBookFromLoan(id);
    }

    /**
     * Loads data asynchronously to prevent blocking the main thread.
     */
    public void loadDataAsync(Loan loan) {
        setUserId(loan.getUserId());
        setId(String.valueOf(loan.getLoanId()));
        List<Book> data = getBooksData(loan.getIsbn());
        String[] amountString = loan.getAmount().split(",\\s*");
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < data.size(); i++) {
                    loadBookData(data.get(i), loan, amountString[i]);
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
    public void loadBookData(Book bookData, Loan loanData, String amount) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminBooksLayoutController.class.getResource(
                    "/fxml/admin/admin-borrowed-book-view-bar.fxml"));
            Pane scene = fxmlLoader.load();
            AdminBorrowedBookViewBarController controller = fxmlLoader.getController();
            scene.setUserData(controller);

            controller.setData(bookData, loanData, amount);
            Platform.runLater(() -> {
                vBox.getChildren().add(scene);
                AnimationUtils.zoomIn(scene, 1.0);
            });
        } catch (Exception e) {
            System.err.println("Error loading book data: " + e.getMessage());
        }
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
    void btnReturnOnAction(ActionEvent event) {
        if (getSelectedBooksList().isEmpty()) {
            ChangeScene.openAdminPopUp(AdminBorrowedBooksLayoutController.getInstance().getStackPaneContainer(), "/fxml/empty-data-notification-dialog.fxml",
                    PopupList.EMPTY_DATA_NOTIFICATION);
        } else {
            ChangeScene.openAdminPopUp(AdminBorrowedBooksLayoutController.getInstance().getStackPaneContainer(),
            "/fxml/user/user-return-book-confirmation-dialog.fxml", EnumUtils.PopupList.RETURN_BOOK);
        }
    }

    public List<String> getSelectedIsbnList() {
        List<String> isbnList = new ArrayList<>();

        for (int i = 0; i < vBox.getChildren().size(); i++) {
            Pane pane = (Pane) vBox.getChildren().get(i);
            AdminBorrowedBookViewBarController controller = (AdminBorrowedBookViewBarController) pane.getUserData();
            if (controller.getCheckBoxButton().isSelected()) {
                isbnList.add(controller.getIsbn());
            }
        }

        return isbnList;
    }

    public List<String> getSelectedAmountList() {
        List<String> amountList = new ArrayList<>();

        for (int i = 0; i < vBox.getChildren().size(); i++) {
            Pane pane = (Pane) vBox.getChildren().get(i);
            AdminBorrowedBookViewBarController controller = (AdminBorrowedBookViewBarController) pane.getUserData();
            if (controller.getCheckBoxButton().isSelected()) {
                amountList.add(controller.getAmount());
            }
        }

        return amountList;
    }

    @FXML
    void btnOnMouseEntered(MouseEvent event) {
        if (event.getSource() == closeButton) {
            closePane.setStyle(
                    "-fx-background-color: #d7d7d7; -fx-background-radius: 10;");
        } else if (event.getSource() == returnButton) {
            hBoxReturn.setStyle(
                    "-fx-background-color: #f2f2f2; -fx-background-radius: 10px; -fx-border-color: #000; -fx-border-radius: 10px; -fx-border-width: 1.2px;");
            returnImage.setImage(new Image(getClass().getResource("/assets/icon/redo 1 (1).png").toExternalForm()));
            returnLabel.setStyle("-fx-text-fill: #000;");
        }
    }

    @FXML
    void btnOnMouseExited(MouseEvent event) {
        if (event.getSource() == closeButton) {
            closePane.setStyle(
                    "-fx-background-color: #fff; -fx-background-radius: 10;");
        } else if (event.getSource() == returnButton) {
            hBoxReturn.setStyle("-fx-background-color: #000; -fx-background-radius: 10px;");
            returnImage.setImage(new Image(getClass().getResource("/assets/icon/redo 1.png").toExternalForm()));
            returnLabel.setStyle("-fx-text-fill: #f2f2f2;");
        }
    }

    public int getLoanId() {
        return Integer.parseInt(lblId.getText());
    }

    /**
     * Sets the ID label with the given ID.
     * 
     * @param id The ID to set.
     */
    public void setId(String id) {
        lblId.setText(id);
    }

    public void setUserId(String userId) {
        lblUserId.setText(userId);
    }

    public List<Book> getSelectedBooksList() {
        List<Book> selectedBooks = new ArrayList<>();

        for (Node node : vBox.getChildren()) {
            if (node instanceof Pane) {
                Pane bookBar = (Pane) node;
                AdminBorrowedBookViewBarController controller = (AdminBorrowedBookViewBarController) bookBar
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
