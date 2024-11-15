package app.libmgmt.view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;

public class AdminDashboardOverdueBarController {

    @FXML
    private Label name;
    @FXML
    private Label id;

    public void setData(String nameText, String idText) {
        name.setText(nameText);
        id.setText("ID: " + idText);
    }

    public void setId(String idText) {
        id.setText("ID: " + idText);
    }

    @FXML
    void handleViewImageOnMouseClicked(MouseEvent event) {
        ChangeScene.openAdminPopUp(AdminDashboardController.getInstance().stackPaneContainer,
                "/fxml/admin-borrowed-book-view-dialog.fxml", id.getText(), EnumUtils.PopupList.OVERDUE_BOOK_DASHBOARD);
    }

}
