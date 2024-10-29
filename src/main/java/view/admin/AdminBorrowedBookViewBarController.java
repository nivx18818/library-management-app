package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AdminBorrowedBookViewBarController {

    @FXML
    private Label dueDateLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label nameBookLabel;

    @FXML
    private Label authorBookLabel;

    public void setData(String path, String name, String author, String dueDate) {
        try {
            bookImage.setImage(new Image(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        nameBookLabel.setText(name);
        authorBookLabel.setText(author);
        dueDateLabel.setText(dueDate);
    }

}
