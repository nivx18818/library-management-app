package app.libmgmt.view.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.text.SimpleDateFormat;

import app.libmgmt.model.Loan;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;

public class AdminBorrowedBooksBarController {

    private static AdminBorrowedBooksBarController controller;

    @FXML
    private Label loanIdLabel;
    @FXML
    private Label borrowedDateLabel;
    @FXML
    private Label dueDateLabel;
    @FXML
    private Label idLabel;
    @FXML
    private ImageView imgView;
    @FXML
    private Label nameLabel;

    private Loan loan;

    // Singleton pattern
    public AdminBorrowedBooksBarController() {
        controller = this;
    }

    public static AdminBorrowedBooksBarController getInstance() {
        return controller;
    }

    // Set book data to the labels
    public void setData(Loan loan) {
        idLabel.setText(loan.getUserId());
        nameLabel.setText(loan.getUserName());
        loanIdLabel.setText(String.valueOf(loan.getLoanId()));
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dueDateString = outputFormat.format(loan.getDueDate());
        String borrowedDateString = outputFormat.format(loan.getBorrowedDate());
        dueDateLabel.setText(dueDateString);
        borrowedDateLabel.setText(borrowedDateString);
        this.loan = loan;
    }

    // Handle image click event to open borrowed book dialog
    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        ChangeScene.openAdminPopUp(
                AdminBorrowedBooksLayoutController.getInstance().getStackPaneContainer(),
                "/fxml/admin/admin-borrowed-book-view-dialog.fxml",
                EnumUtils.PopupList.BORROWED_BOOK_CATALOG);
        AdminBorrowedBookViewDialogController.getInstance().loadDataAsync(loan);
    }

    // Handle image mouse hover
    @FXML
    void imgViewOnMouseEntered(MouseEvent event) {
        setImage("/assets/icon/Property 1=Variant2.png");
    }

    // Reset image when mouse exits
    @FXML
    void imgViewOnMouseExited(MouseEvent event) {
        setImage("/assets/icon/btn view.png");
    }

    // Utility method to set the image for imgView
    private void setImage(String imagePath) {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        imgView.setImage(image);
    }

    // Getter and Setter for idLabel (if needed for other parts of the code)
    public Label getIdLabel() {
        return idLabel;
    }

    public void setIdLabel(Label idLabel) {
        this.idLabel = idLabel;
    }

    public String getId() {
        return this.idLabel.getText();
    }
}
