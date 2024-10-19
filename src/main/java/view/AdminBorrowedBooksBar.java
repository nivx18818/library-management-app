package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class AdminBorrowedBooksBar {

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

    }

    @FXML
    void imgViewOnMouseEntered(MouseEvent event) {

    }

    @FXML
    void imgViewOnMouseExited(MouseEvent event) {

    }

    public void setData(String name, String id, int amount, String dueDate, String borrowedDate) {
        idLabel.setText(String.valueOf(id));
        nameLabel.setText(name);
        amountLabel.setText(String.valueOf(amount));
        dueDateLabel.setText(dueDate);
        borrowedDateLabel.setText(borrowedDate);
    }

}
