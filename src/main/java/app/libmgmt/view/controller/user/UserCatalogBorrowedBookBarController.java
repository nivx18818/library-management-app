package app.libmgmt.view.controller.user;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.google.zxing.WriterException;

import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.view.controller.admin.AdminBookViewDialogController;
import app.libmgmt.view.controller.user.UserCatalogController.USER_CATALOG_STATE;
import app.libmgmt.model.Book;
import app.libmgmt.model.Loan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class UserCatalogBorrowedBookBarController {

    @FXML
    private Label borrowedDateLabel;

    @FXML
    private Label dueDateLabel;

    @FXML
    private Label loanIdLabel;

    @FXML
    private Pane viewPane;

    @FXML
    private ImageView imageView;

    @FXML
    private Label amountLabel;

    @FXML
    private Text isbnText;

    private final String hoverViewLogo = "/assets/icon/Property 1=Variant2.png";
    private final String viewLogo = "/assets/icon/btn view.png";

    @FXML
    void btnBookReturnedOnAction(ActionEvent event) {
        openPopUp("/fxml/user/user-return-book-confirmation-dialog.fxml", EnumUtils.PopupList.RETURN_BOOK);
    }

    @FXML
    void imageViewOnMouseClicked(MouseEvent event) throws WriterException, IOException {
        openPopUp("/fxml/admin/admin-book-view-dialog.fxml", EnumUtils.PopupList.BOOK_VIEW);
        Book bookData = UserGlobalController.getInstance().getBookDataById(isbnText.getText());

        String authorsString = String.join(", ", bookData.getAuthors());
        String categoriesString = String.join(", ", bookData.getCategories());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String publishedDateStr = (bookData.getPublishedDate() != null)
                ? outputFormat.format(bookData.getPublishedDate())
                : "Not Available";

        String[] data = new String[] { bookData.getIsbn(), bookData.getCoverUrl(), bookData.getTitle(),
                categoriesString, authorsString,
                String.valueOf(bookData.getAvailableCopies()), bookData.getPublisher(), publishedDateStr };

        AdminBookViewDialogController.getInstance().setData(data);
    }

    @FXML
    void imageViewOnMouseEntered(MouseEvent event) {
        imageView.setImage(new Image(getClass().getResource(hoverViewLogo).toExternalForm()));
    }

    @FXML
    void imageViewOnMouseExited(MouseEvent event) {
        imageView.setImage(new Image(getClass().getResource(viewLogo).toExternalForm()));
    }

    public void setData(Loan loan) {
        loanIdLabel.setText(String.valueOf(loan.getLoanId()));
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (UserCatalogController.currentStateUserCatalog == USER_CATALOG_STATE.BORROWED) {
            String dueDateString = outputFormat.format(loan.getDueDate());
            dueDateLabel.setText(dueDateString);
        } else {
            String returnedDateString = "Not Available";
            if (loan.getReturnedDate() != null) {
                returnedDateString = outputFormat.format(loan.getReturnedDate());
            }
            dueDateLabel.setText(returnedDateString);
        }
        isbnText.setText(loan.getIsbn());
        String borrowedDateString = outputFormat.format(loan.getBorrowedDate());
        borrowedDateLabel.setText(borrowedDateString);
        String[] amounts = loan.getAmount().split(",\\s*");
        int totalAmount = 0;
        for (String amount : amounts) {
            totalAmount += Integer.parseInt(amount);
        }
        amountLabel.setText(String.valueOf(totalAmount));
    }

    private void openPopUp(String fxmlPath, EnumUtils.PopupList popupType) {
        ChangeScene.openAdminPopUp(UserGlobalController.getInstance().getStackPaneContainer(),
                fxmlPath, loanIdLabel.getText(), popupType);
    }

    public void setLoanId(String loanId) {
        loanIdLabel.setText(loanId);
    }

    public String getLoanId() {
        return loanIdLabel.getText();
    }

}
