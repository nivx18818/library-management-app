package app.libmgmt.view.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;

public class AdminBorrowedBooksBarController {

    private static AdminBorrowedBooksBarController controller;

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

    // Singleton pattern
    public AdminBorrowedBooksBarController() {
        controller = this;
    }

    public static AdminBorrowedBooksBarController getInstance() {
        return controller;
    }

    // Set book data to the labels
    public void setData(String name, String id, int amount, String dueDate, String borrowedDate) {
        idLabel.setText(id);
        nameLabel.setText(name);
        amountLabel.setText(String.valueOf(amount));
        dueDateLabel.setText(dueDate);
        borrowedDateLabel.setText(borrowedDate);
    }

    // Handle image click event to open borrowed book dialog
    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        ChangeScene.openAdminPopUp(
                AdminBorrowedBooksLayoutController.getInstance().getStackPaneContainer(),
                "/fxml/admin/admin-borrowed-book-view-dialog.fxml",
                idLabel.getText(),
                EnumUtils.PopupList.BORROWED_BOOK_CATALOG
        );
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
}
