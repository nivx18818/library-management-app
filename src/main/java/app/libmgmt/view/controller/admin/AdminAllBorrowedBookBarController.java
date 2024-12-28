package app.libmgmt.view.controller.admin;

import java.io.IOException;
import java.text.SimpleDateFormat;

import app.libmgmt.model.Loan;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.view.controller.admin.AdminBorrowedBooksLayoutController.STATE;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class AdminAllBorrowedBookBarController {

    @FXML
    private Label lblBorrowedDate;

    @FXML
    private Label lblDueDate;

    @FXML
    private Label lblLoanId;

    @FXML
    private Label lblIsbn;

    @FXML
    private Label lblStatus;

    @FXML
    private ImageView viewFunction;

    Loan loan;

    public void setData(Loan loan) {
        this.loan = loan;
        lblLoanId.setText(String.valueOf(loan.getLoanId()));
        lblIsbn.setText(loan.getIsbn());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        lblDueDate.setText(sdf.format(loan.getDueDate()));
        lblStatus.setText(loan.getStatus().toString());
        lblBorrowedDate.setText(sdf.format(loan.getBorrowedDate()));
    }

    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        if (lblStatus.getText().equals("BORROWED") || lblStatus.getText().equals("OVERDUE")) {
            ChangeScene.closePopUp();
            AdminBorrowedBooksLayoutController.status = lblStatus.getText().equals("BORROWED") ? STATE.BORROWED
                    : STATE.OVERDUE;
            try {
                AdminNavigationController userNavigationController = AdminNavigationController.getInstance();
                userNavigationController.handleNavigation(EnumUtils.NavigationButton.CATALOG,
                        "admin-borrowed-books-form.fxml",
                        userNavigationController.getCatalogButton());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), e -> {
            ChangeScene.openAdminPopUp(
                    (lblStatus.getText().equals("BORROWED") || lblStatus.getText().equals("OVERDUE"))
                            ? AdminBorrowedBooksLayoutController.getInstance().getStackPaneContainer()
                            : AdminUsersLayoutController.getInstance().stackPaneContainer,
                    "/fxml/admin/admin-borrowed-book-view-dialog.fxml",
                    EnumUtils.PopupList.BORROWED_BOOK_CATALOG);
            AdminBorrowedBookViewDialogController.getInstance().loadDataAsync(loan);
            if (lblStatus.getText().equals("RETURNED")) {
                AdminBorrowedBookViewDialogController.getInstance().setReturnButtonVisibility(false);
            }
        }));

        timeline.play();
    }

    @FXML
    private void imgViewOnMouseEntered(MouseEvent event) {
        updateImage(viewFunction, "/assets/icon/Property 1=Variant2.png");
    }

    @FXML
    private void imgViewOnMouseExited(MouseEvent event) {
        updateImage(viewFunction, "/assets/icon/btn view.png");
    }

    private void updateImage(ImageView imageView, String imagePath) {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        imageView.setImage(image);
    }

}
