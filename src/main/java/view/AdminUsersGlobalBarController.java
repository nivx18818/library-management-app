package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminUsersGlobalBarController {

    @FXML
    private Label categoryLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;

    public void setData(String category, String id, String name, String email) {
        categoryLabel.setText(category);
        idLabel.setText(id);
        nameLabel.setText(name);
        emailLabel.setText(email);
    }

}
