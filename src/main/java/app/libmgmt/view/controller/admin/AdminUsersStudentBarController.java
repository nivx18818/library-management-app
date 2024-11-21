package app.libmgmt.view.controller.admin;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.util.EnumUtils.UserType;

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
    public void initialize() {
        // Add listener to update user information if there's a change in the observable
        // student data
        AdminGlobalController.getInstance().getObservableUsersData(EnumUtils.UserType.STUDENT)
                .addListener((ListChangeListener<String[]>) change -> {
                    while (change.next()) {
                        if (change.wasReplaced() && change.getFrom() >= 0
                                && change.getFrom() < change.getList().size()) {
                            String[] updatedUserData = change.getList().get(change.getFrom());

                            if (idLabel.getText().equals(updatedUserData[4])) {
                                updateUserData(updatedUserData);
                            }
                        }
                    }
                });
    }

    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        System.out.println("View");
        ChangeScene.openAdminPopUp(AdminUsersLayoutController.getInstance().stackPaneContainer,
                "/fxml/admin/admin-users-view-dialog.fxml", null, EnumUtils.PopupList.USER_VIEW);
        AdminUsersViewDialogController.getInstance().setData(getData(), EnumUtils.UserType.STUDENT);
    }

    @FXML
    void imgEditOnMouseClicked(MouseEvent event) {
        System.out.println("Edit");
        ChangeScene.openAdminPopUp(AdminUsersLayoutController.getInstance().stackPaneContainer,
                "/fxml/admin/admin-users-edit-dialog.fxml", null, EnumUtils.PopupList.USER_EDIT);
        AdminUsersEditDialogController.getInstance().showOriginalUserData(getData(), EnumUtils.UserType.STUDENT);
    }

    @FXML
    private void imgDeleteOnMouseClicked(MouseEvent event) {
        System.out.println("Delete");
        ChangeScene.openAdminPopUp(
                AdminUsersLayoutController.getInstance().stackPaneContainer,
                "/fxml/admin/admin-delete-confirmation-dialog.fxml",
                idLabel.getText(),
                EnumUtils.PopupList.STUDENT_DELETE);
    }

    @FXML
    private void imgViewOnMouseEntered(MouseEvent event) {
        updateImage(viewFunction, "/assets/icon/Property 1=Variant2.png");
    }

    @FXML
    private void imgViewOnMouseExited(MouseEvent event) {
        updateImage(viewFunction, "/assets/icon/btn view.png");
    }

    @FXML
    private void imgEditOnMouseEntered(MouseEvent event) {
        updateImage(editFunction, "/assets/icon/edit2.png");
    }

    @FXML
    private void imgEditOnMouseExited(MouseEvent event) {
        updateImage(editFunction, "/assets/icon/btn edit.png");
    }

    @FXML
    private void imgDeleteOnMouseEntered(MouseEvent event) {
        updateImage(deleteFunction, "/assets/icon/red-recycle.png");
    }

    @FXML
    private void imgDeleteOnMouseExited(MouseEvent event) {
        updateImage(deleteFunction, "/assets/icon/btn Delete.png");
    }

    // Set original user data to the fields
    public void setData(String[] data) {
        if (data != null && data.length >= 4) {
            idLabel.setText(data[0]);
            nameLabel.setText(data[1]);
            majorLabel.setText(data[2]);
            emailLabel.setText(data[3]);
        }
    }

    // Update user data in the fields
    private void updateUserData(String[] data) {
        if (data != null && data.length >= 5) {
            idLabel.setText(data[4]);
            nameLabel.setText(data[1]);
            majorLabel.setText(data[2]);
            emailLabel.setText(data[3]);
        }
    }

    public String[] getData() {
        return new String[] { idLabel.getText(), nameLabel.getText(), majorLabel.getText(), emailLabel.getText() };
    }

    // Utility method to update the image on hover and exit events
    private void updateImage(ImageView imageView, String imagePath) {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        imageView.setImage(image);
    }

}
