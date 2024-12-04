package app.libmgmt.view.controller.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.model.Loan;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.service.LoanService;
import app.libmgmt.model.Book;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UserCatalogController {

    // Enum defined within the class
    public enum USER_CATALOG_STATE {
        BORROWED, RETURNED
    }

    private static UserCatalogController controller;
    public static USER_CATALOG_STATE currentStateUserCatalog = USER_CATALOG_STATE.BORROWED; // Default state

    @FXML
    private JFXButton borrowedBooksButton;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private Label borrowedBooksLabel;
    @FXML
    private Label dueDateHeaderLabel;
    @FXML
    private Pane borrowedBooksPane;
    @FXML
    private Pane refreshPaneButton;
    @FXML
    private JFXButton returnedBooksButton;
    @FXML
    private Label returnedBooksLabel;
    @FXML
    private Pane returnedBooksPane;
    @FXML
    private Pane searchPane;
    @FXML
    private StackPane stackPaneContainer;
    @FXML
    private TextField textSearch;
    @FXML
    private VBox vBoxBooksList;
    @FXML
    private Label columnHeader1Label;

    private String deletedOrderNumber;

    // External controllers and data
    private final UserGlobalController userGlobalController = UserGlobalController.getInstance();
    private List<Loan> borrowedBooksData = userGlobalController.getBorrowedBooksData();
    private List<Loan> returnedBooksData = userGlobalController.getReturnedBooksData();
    private LoanService loanService = new LoanService(); // Add this line to initialize loanService

    // Constructor and Singleton Pattern
    public UserCatalogController() {
        controller = this;
    }

    public static UserCatalogController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        if (currentStateUserCatalog == USER_CATALOG_STATE.BORROWED) {
            updateStatusUI(USER_CATALOG_STATE.BORROWED);
            showBorrowedBooksList();
        } else if (currentStateUserCatalog == USER_CATALOG_STATE.RETURNED) {
            updateStatusUI(USER_CATALOG_STATE.RETURNED);
            showReturnedBooksList();
        }

        listenReturnBookEvent();
    }

    // TODO:: This method should be reviewed and refactored
    private void listenReturnBookEvent() {
        userGlobalController.getReturnedBooksData().addListener((ListChangeListener.Change<? extends Loan> change) -> {
            while (change.next()) {
                if (change.wasAdded() && currentStateUserCatalog == USER_CATALOG_STATE.BORROWED) {
                    try {
                        int index = Integer.parseInt(deletedOrderNumber) - 1;
                        if (index >= 0 && index < vBoxBooksList.getChildren().size()) {
                            Pane bookBar = (Pane) vBoxBooksList.getChildren().get(index);

                            // Retrieve controller from bookBar
                            UserCatalogBorrowedBookBarController controller = (UserCatalogBorrowedBookBarController) bookBar
                                    .getUserData();
                            if (controller != null) {
                                controller.setDisableReturnButton(true);
                            } else {
                                System.err.println("Controller not found for book bar");
                            }
                        } else {
                            System.err.println("Index out of bounds: " + index);
                        }

                    } catch (IndexOutOfBoundsException e) {
                        System.err.println("Invalid order number: " + deletedOrderNumber);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid format for order number: " + deletedOrderNumber);
                    } catch (Exception e) {
                        System.err.println("Error updating return button: " + e.getMessage());
                    }
                }
            }
        });
    }

    @FXML
    void btnBorrowedBooksOnAction(ActionEvent event) {
        if (currentStateUserCatalog == USER_CATALOG_STATE.BORROWED) {
            return;
        }

        updateStatusUI(USER_CATALOG_STATE.BORROWED);
        showBorrowedBooksList();
    }

    @FXML
    void btnRefreshTableOnAction(ActionEvent event) {
        if (currentStateUserCatalog == USER_CATALOG_STATE.BORROWED) {
            showBorrowedBooksList();
        } else if (currentStateUserCatalog == USER_CATALOG_STATE.RETURNED) {
            showReturnedBooksList();
        }
        textSearch.clear();
        textSearch.setEditable(true);
    }

    @FXML
    void btnReturnedBooksOnAction(ActionEvent event) {
        if (currentStateUserCatalog == USER_CATALOG_STATE.RETURNED) {
            return;
        }

        updateStatusUI(USER_CATALOG_STATE.RETURNED);
        showReturnedBooksList();
    }

    @FXML
    void btnOnMouseEntered(MouseEvent event) {
        Object source = event.getSource();
        if (source == borrowedBooksButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.HOVER_SCALE, borrowedBooksPane).play();
        } else if (source == returnedBooksButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.HOVER_SCALE, returnedBooksPane).play();
        } else if (source == refreshButton) {
            AnimationUtils.createScaleTransition(1.15, refreshPaneButton).play();
        } else if (source == searchPane) {
            AnimationUtils.createScaleTransition(1.05, searchPane).play();
        }
    }

    @FXML
    void btnOnMouseExited(MouseEvent event) {
        Object source = event.getSource();
        if (source == borrowedBooksButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, borrowedBooksPane).play();
        } else if (source == returnedBooksButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, returnedBooksPane).play();
        } else if (source == refreshButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, refreshPaneButton).play();
        } else if (source == searchPane) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, searchPane).play();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        // TODO: Implement search functionality by book's name
        textSearch.setEditable(false);
    }

    // Data Display
    public void showBorrowedBooksList() {
        vBoxBooksList.getChildren().clear();
        preloadData(borrowedBooksData, USER_CATALOG_STATE.BORROWED);
    }

    public void showReturnedBooksList() {
        vBoxBooksList.getChildren().clear();
        preloadData(returnedBooksData, USER_CATALOG_STATE.RETURNED);
    }

    // Data Preloading
    public void preloadData(List<Loan> data, USER_CATALOG_STATE currentStatus) {
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    for (Loan d : data) {
                        List<Book> books = loanService.getBookFromLoan(d.getIsbn());
                        Book book = books.isEmpty() ? null : books.get(0);

                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String dueDateString = outputFormat.format(d.getDueDate());
                        String borrowedDateString = outputFormat.format(d.getBorrowedDate());

                        String returnedDateString = "Not Available";
                        if (d.getReturnedDate() != null) {
                            returnedDateString = outputFormat.format(d.getReturnedDate());
                        }

                        String[] loanData = new String[] {
                                d.getIsbn(),
                                d.getLoanId() + "",
                                book.getCoverUrl(),
                                book.getTitle(),
                                borrowedDateString,
                                dueDateString,
                                returnedDateString,
                                d.getAmount() + ""
                        };
                        //data format: [isbn, loanId, coverUrl, title, borrowedDate, dueDate, returnedDate, amount]
                        loadBorrowedBookBar(loanData, currentStatus);
                    }
                } catch (Exception e) {
                    System.out.println("Error loading data table: " + e.getMessage());
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            protected void failed() {
                System.out.println("Task failed: " + getException().getMessage());
            }
        };

        new Thread(preloadTask).start();
    }

    // Load a single borrowed book bar
    public void loadBorrowedBookBar(String[] d, USER_CATALOG_STATE currentStatus) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/fxml/user/user-catalog-borowed-books-bar.fxml"));
            Pane scene = fxmlLoader.load();
            UserCatalogBorrowedBookBarController controller = fxmlLoader.getController();
            scene.setUserData(controller);
            controller.setData(d);

            if (currentStatus == USER_CATALOG_STATE.BORROWED) {
                controller.setVisibleAction(true);
                String isbn = d[0];
                int loanId = Integer.parseInt(d[1]);
                boolean isReturned = userGlobalController.isBookReturned(loanId, isbn);
                controller.setDisableReturnButton(isReturned);
            } else {
                controller.setVisibleAction(false);
            }

            Platform.runLater(() -> {
                vBoxBooksList.getChildren().add(scene);
                AnimationUtils.zoomIn(scene, 1.0);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // UI Update Methods
    private void setDefaultStyle() {
        borrowedBooksLabel.setStyle("-fx-text-fill: black;");
        returnedBooksLabel.setStyle("-fx-text-fill: black;");
        borrowedBooksPane.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 12px;");
        returnedBooksPane.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 12px;");
    }

    private void updateStatusUI(USER_CATALOG_STATE newStatus) {
        setDefaultStyle();
        if (newStatus == USER_CATALOG_STATE.BORROWED) {
            columnHeader1Label.setText("No.");
            borrowedBooksLabel.setStyle("-fx-text-fill: white;");
            borrowedBooksPane.setStyle("-fx-background-color: black; -fx-background-radius: 12px;");
            dueDateHeaderLabel.setText("Due Date");
        } else {
            columnHeader1Label.setText("ISBN");
            returnedBooksLabel.setStyle("-fx-text-fill: white;");
            returnedBooksPane.setStyle("-fx-background-color: black; -fx-background-radius: 12px;");
            dueDateHeaderLabel.setText("Returned Date");
        }
        currentStateUserCatalog = newStatus;
    }

    public void setDeletedOrderNumber(String deletedOrderNumber) {
        this.deletedOrderNumber = deletedOrderNumber;
    }
}
