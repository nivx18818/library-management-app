package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import util.ChangeScene;
import util.EnumUtils;

public class AdminBookBarController {

    @FXML
    private Label idLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private ImageView bookImage;
    @FXML
    private ImageView editFunction;
    @FXML
    private ImageView deleteFunction;
    @FXML
    private ImageView viewFunction;


    public void setData(String id, String imagePath, String name, String type, String author,
                        String status) {
        idLabel.setText(id);
        try {
            bookImage.setImage(new Image(imagePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        nameLabel.setText(name);
        typeLabel.setText(type);
        authorLabel.setText(author);
        statusLabel.setText(status);
        statusLabel.setStyle(status.equals("Available") ? "-fx-text-fill: green" : "-fx-text-fill: red");
    }

    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        ChangeScene.openAdminPopUp(AdminGlobalFormController.getInstance().getStackPaneContainer(),
                "/fxml/admin-book-view-dialog.fxml", idLabel.getText(), EnumUtils.PopupList.BOOK_VIEW);
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

}
