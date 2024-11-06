package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    private int quantity;
    private String imgPath;
    private String publisher;
    private String publishedDate;

    private static AdminBookBarController controller;

    public AdminBookBarController() {
        controller = this;
    }

    public static AdminBookBarController getInstance() {
        return controller;
    }

    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        ChangeScene.openAdminPopUp(AdminBooksLayoutController.getInstance().stackPaneContainer,
                "/fxml/admin-book-view-dialog.fxml", idLabel.getText(), EnumUtils.PopupList.BOOK_VIEW);
        AdminBookViewDialogController.getInstance().setData(getData());
    }

    @FXML
    void imgEditOnMouseClicked(MouseEvent event) {
        ChangeScene.openAdminPopUp(AdminBooksLayoutController.getInstance().stackPaneContainer,
                "/fxml/admin-book-edit-dialog.fxml", idLabel.getText(), EnumUtils.PopupList.BOOK_EDIT);
        AdminBookEditDialogController.getInstance().showOriginalBookData(getData());
    }

    @FXML
    void imgDeleteOnMouseClicked(MouseEvent event) {
        ChangeScene.openAdminPopUp(AdminBooksLayoutController.getInstance().stackPaneContainer,
                "/fxml/admin-delete-confirmation-dialog.fxml", idLabel.getText(),
                EnumUtils.PopupList.BOOK_DELETE);
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

    public void setData(String[] data) {
        idLabel.setText(data[0]);
        try {
            imgPath = data[1];
            bookImage.setImage(new Image(imgPath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        nameLabel.setText(data[2]);
        typeLabel.setText(data[3]);
        authorLabel.setText(data[4]);
        quantity = Integer.parseInt(data[5]);
        statusLabel.setText((quantity >= 1) ? "Available" : "Borrowed");
        statusLabel.setStyle(statusLabel.getText().equals("Available") ? "-fx-text-fill: green" : "-fx" +
                "-text-fill: red");
        publisher = data[6];
        publishedDate = data[7];
    }

    public String[] getData() {
        return new String[]{idLabel.getText(), imgPath, nameLabel.getText(), typeLabel.getText(),
                authorLabel.getText(), Integer.toString(quantity), publisher, publishedDate};
    }

    public String getId() {
        return idLabel.getText();
    }

}
