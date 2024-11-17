package app.libmgmt.view.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class BookwormAdminBarController {

    @FXML
    private Label lblEmailAdmin;
    @FXML
    private Text txtNameAdmin;

    public void setData(String name, String email) {
        txtNameAdmin.setText(name);
        lblEmailAdmin.setText(email);
    }

}
