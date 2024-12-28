package app.libmgmt.view.controller.admin;

import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class BookwormAdminBarController {

    @FXML
    private Label lblEmailAdmin;
    @FXML
    private Text txtNameAdmin;
    private String username;


    public void setData(String name, String email, String username) {
        txtNameAdmin.setText(name);
        lblEmailAdmin.setText(email);
        this.username = username;
    }

    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        ChangeScene.openAdminPopUp(AdminDashboardController.getInstance().stackPaneContainer,
                "/fxml/admin/admin-users-view-dialog.fxml", lblEmailAdmin.getText(),
                EnumUtils.PopupList.USER_VIEW);
        String[] data = {
            txtNameAdmin.getText(),
            "N/A",
            lblEmailAdmin.getText(),
            username,
        };
        AdminUsersViewDialogController.getInstance().setData(data, EnumUtils.UserType.ADMIN);
    }

}
