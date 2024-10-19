package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminDashboardOverdueBar {

    @FXML
    private Label name;
    @FXML
    private Label id;

    public void setData(String nameText, String idText) {
        name.setText(nameText);
        id.setText("ID: " + idText);
    }

}
