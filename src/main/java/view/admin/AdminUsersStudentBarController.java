package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import util.ChangeScene;
import util.EnumUtils;

public class AdminUsersStudentBarController {

    @FXML
    private ImageView deleteFunction;
    @FXML
    private ImageView editFunction;
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
    @FXML
    private ImageView viewFunction;

    public void setData(String id, String name, String major, String email) {
        idLabel.setText(id);
        nameLabel.setText(name);
        majorLabel.setText(major);
        emailLabel.setText(email);
    }

    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        System.out.println("View");
        ChangeScene.openAdminPopUp(AdminUsersLayoutController.getInstance().stackPaneContainer, "/fxml/admin" +
                "-users" +
                "-view-dialog" +
                ".fxml");
        AdminUsersViewDialogController.getInstance().setData(getData(), EnumUtils.UserType.STUDENT);
    }

    @FXML
    void imgEditOnMouseClicked(MouseEvent event) {
        System.out.println("Edit");
        ChangeScene.openAdminPopUp(AdminUsersLayoutController.getInstance().stackPaneContainer, "/fxml/admin" +
                "-users" +
                "-edit-dialog" +
                ".fxml");
        AdminUsersEditDialogController.getInstance().showOriginalUserData(getData(), EnumUtils.UserType.STUDENT);
    }

    public String[] getData() {
        return new String[]{idLabel.getText(), nameLabel.getText(), majorLabel.getText(), emailLabel.getText()};
    }

}
