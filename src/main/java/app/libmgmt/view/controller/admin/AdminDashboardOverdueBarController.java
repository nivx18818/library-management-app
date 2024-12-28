package app.libmgmt.view.controller.admin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;

import app.libmgmt.model.Loan;
import app.libmgmt.service.LoanService;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;

public class AdminDashboardOverdueBarController {

    @FXML
    private Label name;
    @FXML
    private Label id;

    LoanService loanService = new LoanService();
    Loan loan = null;

    public void setData(Loan d) {
        name.setText(d.getUserName());
        id.setText("ID: " + d.getUserId());
        this.loan = d;
    }

    public void setId(String idText) {
        id.setText("ID: " + idText);
    }

    @FXML
    void handleViewImageOnMouseClicked(MouseEvent event) {
            try {
                AdminNavigationController userNavigationController = AdminNavigationController.getInstance();
                userNavigationController.handleNavigation(EnumUtils.NavigationButton.CATALOG,
                        "admin-borrowed-books-form.fxml",
                        userNavigationController.getCatalogButton());
                AdminBorrowedBooksLayoutController.getInstance().showOverdueBorrowersList();
                AdminBorrowedBooksLayoutController.getInstance().updateStatusUI(null);
            } catch (IOException e1) {
                e1.printStackTrace();
            }


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), e -> {
            ChangeScene.openAdminPopUp(
                AdminBorrowedBooksLayoutController.getInstance().getStackPaneContainer(),
                "/fxml/admin/admin-borrowed-book-view-dialog.fxml",
                EnumUtils.PopupList.BORROWED_BOOK_CATALOG);
        AdminBorrowedBookViewDialogController.getInstance().loadDataAsync(loan);

        }));

        timeline.play();
    }
}
