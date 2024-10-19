package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AdminUsersGuestBarController {

    @FXML
    private Label emailLabel;
    @FXML
    private HBox hBoxUser;
    @FXML
    private Label idLabel;
    @FXML
    private Label majorLabel;
    @FXML
    private Label nameLabel;

    public void setData(String id, String name, String major, String email) {
        idLabel.setText(id);
        nameLabel.setText(name);
        majorLabel.setText(major);
        emailLabel.setText(email);
    }

}
