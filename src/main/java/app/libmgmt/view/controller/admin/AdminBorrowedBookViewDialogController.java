package app.libmgmt.view.controller.admin;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import app.libmgmt.model.Book;
import app.libmgmt.model.Loan;

import app.libmgmt.service.LoanService;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;

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
        listenLoanDataChanges();
    } 

    private void listenLoanDataChanges() {
        AdminGlobalController.getInstance().getBorrowedBooksData()
            .addListener((ListChangeListener.Change<? extends Loan> change) -> {
                while (change.next()) {
                    if (change.wasUpdated() && change.getTo() != -1) {
                        List<? extends Loan> subList = change.getList().subList(change.getFrom(), change.getTo());
                        subList.forEach(loan -> {
                            String bookId = loan.getIsbn();
                            vBox.getChildren().stream()
                                .filter(child -> child.getId() != null && child.getId().equals(bookId))
                                .findFirst()
                                .ifPresent(child -> Platform.runLater(() -> {
                                    vBox.getChildren().remove(child);
                                }));
                        });
                    }
                }
            });   
    }

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
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < data.size(); i++) {
                    loadBookData(data.get(i), loan);
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
    public void loadBookData(Book bookData, Loan loanData) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminBooksLayoutController.class.getResource(
                    "/fxml/admin/admin-borrowed-book-view-bar.fxml"));
            Pane scene = fxmlLoader.load();
            scene.setId(bookData.getIsbn());
            AdminBorrowedBookViewBarController controller = fxmlLoader.getController();

            controller.setData(bookData, loanData);
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
        ChangeScene.closePopUp();
    }

    @FXML
    void btnReturnOnAction(ActionEvent event) {
        ChangeScene.openAdminPopUp(AdminBorrowedBooksLayoutController.getInstance().getStackPaneContainer(),
        "/fxml/user/user-return-book-confirmation-dialog.fxml", EnumUtils.PopupList.RETURN_BOOK);
    }

    @FXML
    void btnOnMouseEntered(MouseEvent event) {
        if (event.getSource() == closeButton) {
            closePane.setStyle(
                    "-fx-background-color: #d7d7d7; -fx-background-radius: 10;");
        } else if (event.getSource() == returnButton) {
            returnPane.setStyle(
                    "-fx-background-color: #F2F2F2; -fx-background-radius: 10; -fx-border-color: #000; -fx-border-radius: 10; -fx-border-width: 1.2;");
            returnLabel.setStyle("-fx-text-fill: #000;");
        }
    }

    @FXML
    void btnOnMouseExited(MouseEvent event) {
        if (event.getSource() == closeButton) {
            closePane.setStyle(
                    "-fx-background-color: #fff; -fx-background-radius: 10;");
        } else if (event.getSource() == returnButton) {
            returnPane.setStyle(
                    "-fx-background-color: #000; -fx-background-radius: 10;");
            returnLabel.setStyle("-fx-text-fill: #fff;");
        }
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
}
