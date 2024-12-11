package app.libmgmt.view.controller.admin;

import com.jfoenix.controls.JFXButton;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import app.libmgmt.model.Book;
import app.libmgmt.model.Loan;
import app.libmgmt.service.LoanService;
import app.libmgmt.util.AnimationUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;

public class AdminBorrowedBooksLayoutController {

    public enum STATE {
        BORROWED, OVERDUE
    }

    // Singleton instance of the controller
    private static AdminBorrowedBooksLayoutController controller;

    @FXML
    private StackPane stackPaneContainer;
    @FXML
    private VBox vBoxBorrowedBooks;
    @FXML
    private TextField textSearch;
    @FXML
    private JFXButton borrowedBooksButton;
    @FXML
    private JFXButton overdueBorrowersButton;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private Label borrowedBooksLabel;
    @FXML
    private Label overdueBorrowersLabel;
    @FXML
    private Pane borrowedBooksPane;
    @FXML
    private Pane overdueBorrowersPane;
    @FXML
    private Pane refreshPaneButton;
    @FXML
    private Pane searchPane;

    private Timeline debounceTimeline;

    // External controllers and data
    private final AdminGlobalController adminGlobalController = AdminGlobalController.getInstance();
    private final List<Loan> borrowedBooksData = adminGlobalController.getBorrowedBooksData();
    private final List<Loan> overdueData = adminGlobalController.getOverDueLoans();
    public static STATE status = STATE.BORROWED;

    // Constructor and Singleton Access
    public AdminBorrowedBooksLayoutController() {
        controller = this;
    }

    public static AdminBorrowedBooksLayoutController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        Logger.getLogger("javafx").setLevel(java.util.logging.Level.SEVERE);

        debounceDataSearch();

        showBorrowedBooksList();
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

    // Data Preloading
    public void preloadData(List<Loan> data) {
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (Loan d : data) {
                    loadBorrowedBookBar(d);
                    Thread.sleep(10);
                }
                return null;
            }

            @Override
            protected void failed() {
                System.out.println("Error during data table loading: " + getException().getMessage());
            }
        };
        new Thread(preloadTask).start();
    }

    // Load a single borrowed book bar
    private void loadBorrowedBookBar(Loan d) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/admin/admin-borrowed-book-bar.fxml"));
            Pane scene = fxmlLoader.load();
            AdminBorrowedBooksBarController controller = fxmlLoader.getController();

            // String name, String id, int amount, String dueDate, String borrowedDate
            controller.setData(d);

            // Add to VBox and animate
            Platform.runLater(() -> {
                vBoxBorrowedBooks.getChildren().add(scene);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UI Update Methods
    private void setDefaultStyle() {
        overdueBorrowersLabel.setStyle("-fx-text-fill: black;");
        borrowedBooksLabel.setStyle("-fx-text-fill: black;");
        overdueBorrowersPane.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 12px;");
        borrowedBooksPane.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 12px;");
    }

    public void updateStatusUI(STATE newStatus) {
        setDefaultStyle();
        if (newStatus == STATE.OVERDUE) {
            overdueBorrowersLabel.setStyle("-fx-text-fill: white;");
            overdueBorrowersPane.setStyle("-fx-background-color: #000; -fx-background-radius: 12px;");
        } else {
            borrowedBooksLabel.setStyle("-fx-text-fill: white;");
            borrowedBooksPane.setStyle("-fx-background-color: #000; -fx-background-radius: 12px;");
        }
        status = newStatus;
    }

    // Data Display
    public void showBorrowedBooksList() {
        vBoxBorrowedBooks.getChildren().clear();
        preloadData(borrowedBooksData);
    }

    public void showOverdueBorrowersList() {
        vBoxBorrowedBooks.getChildren().clear();
        preloadData(overdueData);
    }

    // Event Handlers
    @FXML
    void btnOverdueBorrowersOnAction(ActionEvent event) {
        if (event.getSource() == overdueBorrowersButton) {
            updateStatusUI(STATE.OVERDUE);
            showOverdueBorrowersList();
        } else if (event.getSource() == borrowedBooksButton) {
            updateStatusUI(STATE.BORROWED);
            showBorrowedBooksList();
        }
    }

    @FXML
    void btnRefreshTableOnAction(ActionEvent event) {
        refreshBorrowedBooksList();
        // textSearch.clear();
    }

    public void refreshBorrowedBooksList() {
        vBoxBorrowedBooks.getChildren().clear();
        if (status == STATE.BORROWED) {
            adminGlobalController.getBorrowedBooksData().clear();
        } else {
            adminGlobalController.getOverDueLoans().clear();
        }
        Task<List<Loan>> reloadTask = new Task<>() {
            @Override
            protected List<Loan> call() {
                if (status == STATE.BORROWED) {
                    return adminGlobalController.preLoadBorrowedBooksData();
                } else {
                    return adminGlobalController.preLoadOverDueLoans();
                }
            }

            @Override
            protected void succeeded() {
                if (status == STATE.BORROWED) {
                    adminGlobalController.getBorrowedBooksData().addAll(getValue());
                    preloadData(borrowedBooksData);
                    textSearch.clear();
                    textSearch.setEditable(true);
                } else {
                    adminGlobalController.getOverDueLoans().addAll(getValue());
                    preloadData(overdueData);
                    textSearch.clear();
                    textSearch.setEditable(true);
                }
            }

            @Override
            protected void failed() {
                System.out.println("Failed to reload" + getException().getMessage());
            }
        };

        new Thread(reloadTask).start();
    }

    private void performSearch() {
        vBoxBorrowedBooks.getChildren().clear();
        String searchText = textSearch.getText();
        if (searchText.isEmpty()) {
            if (status == STATE.BORROWED) {
                showBorrowedBooksList();
            } else if (status == STATE.OVERDUE) {
                showOverdueBorrowersList();
            }
        } else {
            showFilteredData(searchText);
        }
    }

    // Filtering Logic
    public void showFilteredData(String searchText) {
        String searchLower = searchText.toLowerCase().trim();

        // Split search terms by spaces for multi-criteria search
        String[] searchTerms = searchLower.split("\\s+");

        if (status == STATE.BORROWED) {
            adminGlobalController.getBorrowedBooksData().stream()
                    .filter(loan -> matchesAllSearchTerms(loan, searchTerms))
                    .forEach(loan -> loadBorrowedBookBar(loan));
        } else if (status == STATE.OVERDUE) {
            adminGlobalController.getOverDueLoans().stream()
                    .filter(loan -> matchesAllSearchTerms(loan, searchTerms))
                    .forEach(loan -> loadBorrowedBookBar(loan));
        }
    }

    private boolean matchesAllSearchTerms(Loan loan, String[] searchTerms) {
        return java.util.Arrays.stream(searchTerms)
                .allMatch(term -> matchesSingleTerm(loan, term));
    }

    private boolean matchesSingleTerm(Loan loan, String searchText) {
        try {
            // Get book details for the loan
            LoanService loanService = new LoanService();
            List<Book> books = loanService.getBookFromLoan(loan.getIsbn());
            Book book = books.isEmpty() ? null : books.get(0);

            // Format dates for searching
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String borrowedDate = dateFormat.format(loan.getBorrowedDate());
            String dueDate = dateFormat.format(loan.getDueDate());

            // Check all searchable fields
            return (loan.getIsbn() != null && loan.getIsbn().toLowerCase().contains(searchText)) ||
                    (loan.getUserId() != null && loan.getUserId().toLowerCase().contains(searchText)) ||
                    (loan.getUserName() != null && loan.getUserName().toLowerCase().contains(searchText)) ||
                    (book != null && book.getTitle().toLowerCase().contains(searchText)) ||
                    borrowedDate.toLowerCase().contains(searchText) ||
                    dueDate.toLowerCase().contains(searchText) ||
                    String.valueOf(loan.getLoanId()).contains(searchText);
        } catch (Exception e) {
            System.err.println("Error while matching search criteria: " + e.getMessage());
            return false;
        }
    }

    @FXML
    void btnOnMouseEntered(MouseEvent event) {
        Object source = event.getSource();
        if (source == borrowedBooksButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.HOVER_SCALE, borrowedBooksPane).play();
        } else if (source == overdueBorrowersButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.HOVER_SCALE, overdueBorrowersPane).play();
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
        } else if (source == overdueBorrowersButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, overdueBorrowersPane).play();
        } else if (source == refreshButton) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, refreshPaneButton).play();
        } else if (source == searchPane) {
            AnimationUtils.createScaleTransition(AnimationUtils.DEFAULT_SCALE, searchPane).play();
        }
    }

    public StackPane getStackPaneContainer() {
        return stackPaneContainer;
    }
}
