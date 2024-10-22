package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminBorrowedBookDialogController {

    @FXML
    private Label authorBookLabel;

    @FXML
    private Label idBookLabel;

    @FXML
    private Label nameBookLabel;

    @FXML
    private Label typeBookLabel;

    public void setData(String id, String name, String author, String type) {
        idBookLabel.setText(id);
        nameBookLabel.setText(name);
        authorBookLabel.setText(author);
        typeBookLabel.setText(type);
    }

}
