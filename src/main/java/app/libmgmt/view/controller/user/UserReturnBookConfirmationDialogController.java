package app.libmgmt.view.controller.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import app.libmgmt.model.Loan;
import app.libmgmt.service.LoanService;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.view.controller.admin.AdminBorrowedBookViewDialogController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class UserReturnBookConfirmationDialogController {
    
    private static UserReturnBookConfirmationDialogController controller;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private Label cancelLabel;

    @FXML
    private Pane cancelPane;

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private JFXButton confirmButton;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblConfirm;

    @FXML
    private Label notificationLabel;

    @FXML
    private Pane confirmPane;

    private int loanId;

    private Loan loan;

    List<String> isbnListReturned = new ArrayList<>();
    List<String> amountList = new ArrayList<>();

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    private final LoanService loanService = new LoanService();

    public UserReturnBookConfirmationDialogController() {
        controller = this;
    }

    public static UserReturnBookConfirmationDialogController getInstance() {
        if (controller == null) {
            controller = new UserReturnBookConfirmationDialogController();
        }
        return controller;
    }

    @FXML
    public void initialize() {
        isbnListReturned = AdminBorrowedBookViewDialogController.getInstance().getSelectedIsbnList();
        amountList = AdminBorrowedBookViewDialogController.getInstance().getSelectedAmountList();
        loanId = AdminBorrowedBookViewDialogController.getInstance().getLoanId();
        loan = loanService.getLoanById(loanId);
        System.out.println("Loan ID: " + loanId);
        for (String isbn : isbnListReturned) {
            System.out.println(isbn);
        }
        setupHoverEffects();
    }

    @FXML
    void btnConfirmOnAction(ActionEvent event) {
        startReturnBookProcess();
    }

    private void startReturnBookProcess() {
        lblConfirm.setText("Returning...");
        disableButtons(true);

        List<String> isbnOfLoan = Arrays.asList(loan.getIsbn().split(",\\s*"));
        List<String> amountOfLoan = Arrays.asList(loan.getAmount().split(",\\s*"));
        List<String> resultIsbnList = new ArrayList<>();
        List<String> resultAmountList = new ArrayList<>();

        for (int i = 0; i < isbnOfLoan.size(); i++) {
            String isbn = isbnOfLoan.get(i);
            if (!isbnListReturned.contains(isbn)) {
                resultIsbnList.add(isbn);
                resultAmountList.add(amountOfLoan.get(i));
            }
        }

        String resultIsbnString = String.join(", ", resultIsbnList);
        String resultAmountString = String.join(", ", resultAmountList);

        if (resultIsbnString.isEmpty()) {
            loanService.updateLoanReturnedDate(loanId);
        } else {
            loan.setIsbn(resultIsbnString);
            loan.setAmount(resultAmountString);
            loanService.updateLoan(loan);
            Loan return_loan = loan;
            return_loan.setIsbn(String.join(", ", isbnListReturned));
            return_loan.setAmount(String.join(", ", amountList));
            loanService.addLoan(return_loan);
            loanService.updateLoanReturnedDate(loanService.getMaxLoanId());
        }
        
        closeDialogAndNavigateToCatalog();
    }

    /**
     * Disables or enables delete and cancel buttons.
     */
    private void disableButtons(boolean disable) {
        confirmButton.setDisable(disable);
        cancelButton.setDisable(disable);
    }

    /**
     * Closes the dialog after a short delay.
     */
    private void closeDialogAndNavigateToCatalog() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            notificationLabel.setText("Return book successfully! Returning to the main page...");
            lblConfirm.setText("Returned!");
        }));
        Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
            ChangeScene.closePopUp();
        }));
        timeline.play();
        timeline2.play();
    }

    @FXML
    void btnConfirmOnMouseEntered(MouseEvent event) {
        confirmPane.setStyle(
                "-fx-background-color: #f2f2f2; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: black");
        lblConfirm.setStyle("-fx-text-fill: #000");
    }

    @FXML
    void btnConfirmOnMouseExited(MouseEvent event) {
        confirmPane.setStyle("-fx-background-color: black; -fx-background-radius: 10");
        lblConfirm.setStyle("-fx-text-fill: #fff");
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void cancelButtonOnMouseEntered(MouseEvent event) {
        changeCancelPaneStyle("#d7d7d7");
    }

    @FXML
    void cancelButtonOnMouseExited(MouseEvent event) {
        changeCancelPaneStyle("#fff");
    }

    /**
     * Changes the background color of the cancel pane based on hover state.
     */
    private void changeCancelPaneStyle(String color) {
        cancelPane.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10");
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    /**
     * Sets up hover effects for interactive buttons.
     */
    private void setupHoverEffects() {
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    public void setIsbnReturned(List<String> isbnListReturned) {
        this.isbnListReturned = isbnListReturned;
    }
}
