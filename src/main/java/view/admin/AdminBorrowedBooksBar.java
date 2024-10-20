package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class AdminBorrowedBooksBar {

    AdminCatalogBorrowedBooksLayout adminCatalogBorrowedBooksLayout = AdminCatalogBorrowedBooksLayout.getInstance();
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

    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        AdminNavigationController.openPopUp(adminCatalogBorrowedBooksLayout.getStackPaneContainer(),
                "/fxml/admin-borrowed-book-view-dialog.fxml");
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

}
