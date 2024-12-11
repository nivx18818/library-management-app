package app.libmgmt.view.controller.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.model.Loan;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.service.LoanService;
import app.libmgmt.model.Book;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.util.Duration;

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

    private Timeline debounceTimeline;

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

        debounceDataSearch();

        if (currentStateUserCatalog == USER_CATALOG_STATE.BORROWED) {
            updateStatusUI(USER_CATALOG_STATE.BORROWED);
            showBorrowedBooksList();
        } else if (currentStateUserCatalog == USER_CATALOG_STATE.RETURNED) {
            updateStatusUI(USER_CATALOG_STATE.RETURNED);
            showReturnedBooksList();
        }
    }

    // Debounce Search
    private void debounceDataSearch() {
        debounceTimeline = new Timeline(new KeyFrame(Duration.millis(280), event -> {
            performSearch();
        }));
        debounceTimeline.setCycleCount(1);

        textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (debounceTimeline.getStatus() == Timeline.Status.RUNNING) {
                debounceTimeline.stop();
            }
            debounceTimeline.play();
        });
    }

    @FXML
    void btnBorrowedBooksOnAction(ActionEvent event) {
        handleChangeBorrowedBooksButtonOnAction();
    }

    public void handleChangeBorrowedBooksButtonOnAction() {
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
    }

    @FXML
    void btnReturnedBooksOnAction(ActionEvent event) {
        handleChangeReturnedBooksButtonOnAction();
    }

    public void handleChangeReturnedBooksButtonOnAction() {
        if (currentStateUserCatalog == USER_CATALOG_STATE.RETURNED) {
            return;
        }

        updateStatusUI(USER_CATALOG_STATE.RETURNED);
        showReturnedBooksList();
    }

    public void refreshLoansList() {
        vBoxBooksList.getChildren().clear();
        userGlobalController.getObservableBookData().clear();
        if (currentStateUserCatalog == USER_CATALOG_STATE.BORROWED) {
            userGlobalController.getBorrowedBooksData().clear();
        } else {
            userGlobalController.getReturnedBooksData().clear();
        }
        Task<List<Loan>> reloadTask = new Task<>() {
            @Override
            protected List<Loan> call() {
                if (currentStateUserCatalog == USER_CATALOG_STATE.BORROWED) {
                    return userGlobalController.preLoadLoansData();
                } else {
                    return userGlobalController.setOriginalReturnedBooksData();
                }
            }

            @Override
            protected void succeeded() {
                if (currentStateUserCatalog == USER_CATALOG_STATE.BORROWED) {
                    userGlobalController.getBorrowedBooksData().addAll(getValue());
                    preloadData(borrowedBooksData, USER_CATALOG_STATE.BORROWED);
                    textSearch.clear();
                    textSearch.setEditable(true);
                } else {
                    userGlobalController.getReturnedBooksData().addAll(getValue());
                    preloadData(borrowedBooksData, USER_CATALOG_STATE.RETURNED);
                    textSearch.clear();
                    textSearch.setEditable(true);
                }
            }

            @Override
            protected void failed() {
                System.out.println("Failed to reload data from database: " + getException().getMessage());
            }
        };

        new Thread(reloadTask).start();
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
            refreshLoansList();
            System.out.println("Load data from db");
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

    public void performSearch() {
        vBoxBooksList.getChildren().clear();
        String searchText = textSearch.getText();
        if (searchText.isEmpty()) {
            if (currentStateUserCatalog == USER_CATALOG_STATE.BORROWED) {
                showBorrowedBooksList();
            } else if (currentStateUserCatalog == USER_CATALOG_STATE.RETURNED) {
                showReturnedBooksList();
            }
        } else {
            showFilteredData(searchText);
        }
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

    public void showFilteredData(String searchText) {
        String searchLower = searchText.toLowerCase().trim();

        String[] searchTerms = searchLower.split("\\s+");
        
        if (currentStateUserCatalog == USER_CATALOG_STATE.BORROWED) {
            userGlobalController.getBorrowedBooksData().stream()
                    .filter(loan -> matchesAllSearchTerms(loan, searchTerms))
                    .forEach(loan -> loadBorrowedBookBar(loan, currentStateUserCatalog));
        } else if (currentStateUserCatalog == USER_CATALOG_STATE.RETURNED) {
            userGlobalController.getReturnedBooksData().stream()
                    .filter(loan -> matchesAllSearchTerms(loan, searchTerms))
                    .forEach(loan -> loadBorrowedBookBar(loan, currentStateUserCatalog));
        }
    }

    private boolean matchesAllSearchTerms(Loan loan, String[] searchTerms) {
        return java.util.Arrays.stream(searchTerms)
                .allMatch(term -> matchesSingleTerm(loan, term));
    }
    
    private boolean matchesSingleTerm(Loan loan, String searchText) {
        try {
            // Get book details for the loan
            List<Book> books = loanService.getBookFromLoan(loan.getIsbn());
            Book book = books.isEmpty() ? null : books.get(0);
            
            // Format dates for searching
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String borrowedDate = dateFormat.format(loan.getBorrowedDate());
            String dueDate = dateFormat.format(loan.getDueDate());
            String returnedDate = loan.getReturnedDate() != null ? 
                                dateFormat.format(loan.getReturnedDate()) : "";
    
            // Check all searchable fields
            return (loan.getIsbn() != null && loan.getIsbn().toLowerCase().contains(searchText)) ||
                   (book != null && book.getTitle().toLowerCase().contains(searchText)) ||
                   borrowedDate.toLowerCase().contains(searchText) ||
                   dueDate.toLowerCase().contains(searchText) ||
                   (returnedDate.toLowerCase().contains(searchText)) ||
                   String.valueOf(loan.getLoanId()).contains(searchText) ||
                   String.valueOf(loan.getAmount()).contains(searchText);
        } catch (Exception e) {
            System.err.println("Error while matching search criteria: " + e.getMessage());
            return false;
        }
    }

    // Data Preloading
    public void preloadData(List<Loan> data, USER_CATALOG_STATE currentStatus) {
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    for (Loan d : data) {
                        loadBorrowedBookBar(d, currentStatus);
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
    public void loadBorrowedBookBar(Loan d, USER_CATALOG_STATE currentStatus) {
        try {
            // data format: [isbn, loanId, coverUrl, title, borrowedDate, dueDate,
            // returnedDate, amount]
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/fxml/user/user-catalog-borowed-books-bar.fxml"));
            Pane scene = fxmlLoader.load();
            UserCatalogBorrowedBookBarController controller = fxmlLoader.getController();
            scene.setUserData(controller);
            controller.setData(d);

            Platform.runLater(() -> {
                vBoxBooksList.getChildren().add(scene);
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
            borrowedBooksLabel.setStyle("-fx-text-fill: white;");
            borrowedBooksPane.setStyle("-fx-background-color: black; -fx-background-radius: 12px;");
            dueDateHeaderLabel.setText("Due Date");
        } else {
            returnedBooksLabel.setStyle("-fx-text-fill: white;");
            returnedBooksPane.setStyle("-fx-background-color: black; -fx-background-radius: 12px;");
            dueDateHeaderLabel.setText("Returned Date");
        }
        currentStateUserCatalog = newStatus;
    }

    public StackPane getStackPaneContainer() {
        return stackPaneContainer;
    }
}
