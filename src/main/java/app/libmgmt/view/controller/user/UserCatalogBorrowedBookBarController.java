package app.libmgmt.view.controller.user;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.google.zxing.WriterException;

import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.view.controller.user.UserCatalogController.USER_CATALOG_STATE;
import app.libmgmt.model.Loan;
import app.libmgmt.service.LoanService;
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
    void imageViewOnMouseClicked(MouseEvent event) throws WriterException, IOException {
        if (UserCatalogController.currentStateUserCatalog == USER_CATALOG_STATE.BORROWED) {
            openPopUp("/fxml/user/user-borrowed-view-dialog.fxml", EnumUtils.PopupList.BOOK_VIEW);
            LoanService loanService = new LoanService();
            UserBorrowedBookViewDialogController.getInstance().loadDataAsync(loanService.getLoanById(Integer.parseInt(loanIdLabel.getText())));
        } else {
            openPopUp("/fxml/user/user-returned-book-view-dialog.fxml", EnumUtils.PopupList.BOOK_VIEW);
            LoanService loanService = new LoanService();
            UserReturnedBookViewDialogController.getInstance().loadDataAsync(loanService.getLoanById(Integer.parseInt(loanIdLabel.getText())));
        }
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
        ChangeScene.openAdminPopUp(UserCatalogController.getInstance().getStackPaneContainer(),
                fxmlPath, popupType);
    }

    public void setLoanId(String loanId) {
        loanIdLabel.setText(loanId);
    }

    public String getLoanId() {
        return loanIdLabel.getText();
    }

}
