package app.libmgmt.view.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.List;

import app.libmgmt.model.Loan;
import app.libmgmt.model.Book;
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
        id.setText("ID: " + d.getIsbn());
        this.loan = d;
    }

    public void setId(String idText) {
        id.setText("ID: " + idText);
    }

    @FXML
    void handleViewImageOnMouseClicked(MouseEvent event) {
        ChangeScene.openAdminPopUp(AdminDashboardController.getInstance().stackPaneContainer,
                "/fxml/admin/admin-borrowed-book-view-dialog.fxml", id.getText(), EnumUtils.PopupList.OVERDUE_BOOK_DASHBOARD);
        List<Book> book = loanService.getBookFromLoan(loan.getIsbn());
        Book book_data = book.get(0);
        if (book_data == null) {
            System.out.println("book is null");
        }
        AdminBorrowedBookViewDialogController.getInstance().loadBookData(book_data, loan);
    }
}
