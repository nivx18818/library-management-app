package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import util.ChangeScene;
import util.EnumUtils;

public class AdminBorrowedBooksBarController {

    AdminCatalogBorrowedBooksLayoutController adminCatalogBorrowedBooksLayout = AdminCatalogBorrowedBooksLayoutController.getInstance();
    @FXML
    private Label amountLabel;
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

    private static AdminBorrowedBooksBarController controller;

    public AdminBorrowedBooksBarController() {
        controller = this;
    }

    public static AdminBorrowedBooksBarController getInstance() {
        return controller;
    }

    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        ChangeScene.openAdminPopUp(AdminCatalogBorrowedBooksLayoutController.getInstance().getStackPaneContainer(),
                "/fxml/admin-borrowed-book-view-dialog.fxml", idLabel.getText(),
                EnumUtils.PopupList.BORROWED_BOOK_CATALOG);
    }

    @FXML
    void imgViewOnMouseEntered(MouseEvent event) {
        Image hoverImage = new Image(getClass().getResource("/assets/icon/Property 1=Variant2" +
                ".png").toExternalForm());
        imgView.setImage(hoverImage);
    }

    @FXML
    void imgViewOnMouseExited(MouseEvent event) {
        Image normalImage = new Image(getClass().getResource("/assets/icon/btn view.png").toExternalForm());
        imgView.setImage(normalImage);
    }

    public void setData(String name, String id, int amount, String dueDate, String borrowedDate) {
        idLabel.setText(String.valueOf(id));
        nameLabel.setText(name);
        amountLabel.setText(String.valueOf(amount));
        dueDateLabel.setText(dueDate);
        borrowedDateLabel.setText(borrowedDate);
    }

    public Label getIdLabel() {
        return idLabel;
    }

    public void setIdLabel(Label idLabel) {
        this.idLabel = idLabel;
    }
}
