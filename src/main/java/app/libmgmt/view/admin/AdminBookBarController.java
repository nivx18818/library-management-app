package app.libmgmt.view.admin;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;

public class AdminBookBarController {

    private static AdminBookBarController controller;

    @FXML
    private Label idLabel, authorLabel, nameLabel, statusLabel, typeLabel;
    @FXML
    private ImageView bookImage, editFunction, deleteFunction, viewFunction;

    private int quantity = -1;
    private String imgPath = "", publisher = "", publishedDate = "";

    public AdminBookBarController() {
        controller = this;
    }

    public static AdminBookBarController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        AdminGlobalController.getInstance().getObservableBookData().addListener((ListChangeListener<String[]>) change -> {
            while (change.next()) {
                if (change.wasReplaced() && change.getFrom() >= 0 && change.getFrom() < change.getList().size()) {
                    String[] updatedBookData = change.getList().get(change.getFrom());
                    if (idLabel.getText().equals(updatedBookData[0])) {
                        setData(updatedBookData);
                    }
                }
            }
        });
    }

    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        openPopUp("/fxml/admin-book-view-dialog.fxml", EnumUtils.PopupList.BOOK_VIEW);
        AdminBookViewDialogController.getInstance().setData(getData());
    }

    @FXML
    void imgEditOnMouseClicked(MouseEvent event) {
        openPopUp("/fxml/admin-book-edit-dialog.fxml", EnumUtils.PopupList.BOOK_EDIT);
        AdminBookEditDialogController.getInstance().showOriginalBookData(getData());
    }

    @FXML
    void imgDeleteOnMouseClicked(MouseEvent event) {
        openPopUp("/fxml/admin-delete-confirmation-dialog.fxml", EnumUtils.PopupList.BOOK_DELETE);
    }

    @FXML
    void imgOnMouseEntered(MouseEvent event) {
        ImageView source = (ImageView) event.getSource();
        String iconPath = getIconPath(source, true);
        source.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
    }

    @FXML
    void imgOnMouseExited(MouseEvent event) {
        ImageView source = (ImageView) event.getSource();
        String iconPath = getIconPath(source, false);
        source.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
    }

    private String getIconPath(ImageView source, boolean isHovered) {
        if (source == viewFunction) {
            return isHovered ? "/assets/icon/Property 1=Variant2.png" : "/assets/icon/btn view.png";
        } else if (source == editFunction) {
            return isHovered ? "/assets/icon/edit2.png" : "/assets/icon/btn edit.png";
        } else if (source == deleteFunction) {
            return isHovered ? "/assets/icon/red-recycle.png" : "/assets/icon/btn Delete.png";
        }
        return "";
    }

    private void openPopUp(String fxmlPath, EnumUtils.PopupList popupType) {
        ChangeScene.openAdminPopUp(AdminBooksLayoutController.getInstance().stackPaneContainer,
                fxmlPath, idLabel.getText(), popupType);
    }

    public String[] getData() {
        return new String[]{idLabel.getText(), imgPath, nameLabel.getText(), typeLabel.getText(),
                authorLabel.getText(), Integer.toString(quantity), publisher, publishedDate};
    }

    public void setData(String[] data) {
        updateLabelIfChanged(idLabel, data[0]);
        updateImageIfChanged(data[1]);
        updateLabelIfChanged(nameLabel, data[2]);
        updateLabelIfChanged(typeLabel, data[3]);
        updateLabelIfChanged(authorLabel, data[4]);
        updateQuantityAndStatus(data[5]);

        if (!publisher.equals(data[6])) {
            publisher = data[6];
        }
        if (!publishedDate.equals(data[7])) {
            publishedDate = data[7];
        }
    }

    private void updateLabelIfChanged(Label label, String newText) {
        if (!label.getText().equals(newText)) {
            label.setText(newText);
        }
    }

    private void updateImageIfChanged(String newImagePath) {
        if (!imgPath.equals(newImagePath)) {
            try {
                imgPath = newImagePath;
                bookImage.setImage(new Image(imgPath));
            } catch (Exception e) {
                throw new RuntimeException("Failed to load image: " + newImagePath, e);
            }
        }
    }

    private void updateQuantityAndStatus(String newQuantityStr) {
        int newQuantity = Integer.parseInt(newQuantityStr);
        if (quantity != newQuantity) {
            quantity = newQuantity;
            statusLabel.setText(quantity >= 1 ? "Available" : "Borrowed");
            statusLabel.setStyle(quantity >= 1 ? "-fx-text-fill: green" : "-fx-text-fill: red");
        }
    }

    public String getId() {
        return idLabel.getText();
    }
}
