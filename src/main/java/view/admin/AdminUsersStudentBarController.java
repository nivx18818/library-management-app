package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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

    @FXML
    void imgDeleteOnMouseClicked(MouseEvent event) {
        System.out.println("Delete");
        ChangeScene.openAdminPopUp(AdminUsersLayoutController.getInstance().stackPaneContainer, "/fxml/admin" +
                "-delete-confirmation-dialog" +
                ".fxml", idLabel.getText(), EnumUtils.PopupList.USER_DELETE);
    }

    @FXML
    void imgViewOnMouseEntered(MouseEvent event) {
        Image hoverImage = new Image(getClass().getResource("/assets/icon/Property 1=Variant2" +
                ".png").toExternalForm());
        viewFunction.setImage(hoverImage);
    }

    @FXML
    void imgViewOnMouseExited(MouseEvent event) {
        Image normalImage = new Image(getClass().getResource("/assets/icon/btn view.png").toExternalForm());
        viewFunction.setImage(normalImage);
    }

    @FXML
    void imgEditOnMouseEntered(MouseEvent event) {
        Image hoverImage = new Image(getClass().getResource("/assets/icon/edit2.png").toExternalForm());
        editFunction.setImage(hoverImage);
    }

    @FXML
    void imgEditOnMouseExited(MouseEvent event) {
        Image normalImage =
                new Image(getClass().getResource("/assets/icon/btn edit.png").toExternalForm());
        editFunction.setImage(normalImage);
    }

    @FXML
    void imgDeleteOnMouseEntered(MouseEvent event) {
        Image hoverImage =
                new Image(getClass().getResource("/assets/icon/red-recycle.png").toExternalForm());
        deleteFunction.setImage(hoverImage);
    }

    @FXML
    void imgDeleteOnMouseExited(MouseEvent event) {
        Image normalImage =
                new Image(getClass().getResource("/assets/icon/btn Delete.png").toExternalForm());
        deleteFunction.setImage(normalImage);
    }

    public void setData(String id, String name, String major, String email) {
        idLabel.setText(id);
        nameLabel.setText(name);
        majorLabel.setText(major);
        emailLabel.setText(email);
    }

    public String[] getData() {
        return new String[]{idLabel.getText(), nameLabel.getText(), majorLabel.getText(), emailLabel.getText()};
    }

}
