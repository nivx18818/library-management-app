package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import util.ChangeScene;
import util.EnumUtils;

public class AdminUsersGuestBarController {

    @FXML
    private Label emailLabel;
    @FXML
    private HBox hBoxUser;
    @FXML
    private Label idLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label nameLabel;

    public void setData(String id, String name, String phone, String email) {
        idLabel.setText(id);
        nameLabel.setText(name);
        phoneLabel.setText(phone);
        emailLabel.setText(email);
    }

    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        System.out.println("View");
        ChangeScene.openAdminPopUp(AdminUsersLayoutController.getInstance().stackPaneContainer, "/fxml/admin" +
                "-users" +
                "-view-dialog" +
                ".fxml");
        AdminUsersViewDialogController.getInstance().setData(getData(), EnumUtils.UserType.GUEST);
    }

    @FXML
    void imgEditOnMouseClicked(MouseEvent event) {
        System.out.println("Edit");
        ChangeScene.openAdminPopUp(AdminUsersLayoutController.getInstance().stackPaneContainer, "/fxml/admin" +
                "-users" +
                "-edit-dialog" +
                ".fxml");
        AdminUsersEditDialogController.getInstance().showOriginalUserData(getData(), EnumUtils.UserType.GUEST);
    }

    @FXML
    void imgDeleteOnMouseClicked(MouseEvent event) {
        System.out.println("Delete");
        ChangeScene.openAdminPopUp(AdminUsersLayoutController.getInstance().stackPaneContainer, "/fxml/admin" +
                "-delete-confirmation-dialog" +
                ".fxml", idLabel.getText(), EnumUtils.PopupList.USER_DELETE);
    }

    public String[] getData() {
        return new String[]{idLabel.getText(), nameLabel.getText(), phoneLabel.getText(),
                emailLabel.getText()};
    }

}
