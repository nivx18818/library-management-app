package app.libmgmt.view.controller.user;

import com.jfoenix.controls.JFXCheckBox;

import app.libmgmt.util.ChangeScene;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.view.controller.admin.AdminBookViewDialogController;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class UserBookBarController {

    private static UserBookBarController controller;

    @FXML
    private Label authorLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private JFXCheckBox checkBoxButton;

    @FXML
    private Label nameLabel;

    @FXML
    private Label orderLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private ImageView viewImage;

    private int quantity = -1;
    private String imgPath = "", publisher = "", publishedDate = "", bookID = "";

    private final String hoverViewLogo = "/assets/icon/Property 1=Variant2.png";
    private final String viewLogo = "/assets/icon/btn view.png";

    public UserBookBarController() {
        controller = this;
    }

    public static UserBookBarController getInstance() {
        return controller;
    }

    // --- Event Handlers ---
    @FXML
    void imgViewOnMouseClicked(MouseEvent event) {
        openPopUp("/fxml/admin/admin-book-view-dialog.fxml", EnumUtils.PopupList.BOOK_VIEW);
        AdminBookViewDialogController.getInstance().setData(getData());
    }

    @FXML
    void imgViewOnMouseEntered(MouseEvent event) {
        viewImage.setImage(new Image(getClass().getResource(hoverViewLogo).toExternalForm()));
    }

    @FXML
    void imgViewOnMouseExited(MouseEvent event) {
        viewImage.setImage(new Image(getClass().getResource(viewLogo).toExternalForm()));
    }

    public void setData(String[] data) {
        // Form of global data: [id, imgPath, name, type, author, quantity, publisher,
        // publishedDate]
        // Form of book bar: [No., imgPath, name, type, author, quantity(available)]
        bookID = data[0];
        orderLabel.setText(
                Integer.toString(UserGlobalController.getInstance().getObservableBooksData().indexOf(data) + 1));
        updateImageIfChanged(data[1], bookImage);
        nameLabel.setText(data[2]);
        typeLabel.setText(data[3]);
        authorLabel.setText(data[4]);
        setQuantityAndStatus(data[5]);
        publisher = data[6];
        publishedDate = data[7];
    }

    public String[] getData() {
        return new String[] { bookID, imgPath, nameLabel.getText(), typeLabel.getText(), authorLabel.getText(),
                Integer.toString(quantity), publisher, publishedDate };
    }

    /**
     * Updates the image if the path has changed. Uses a placeholder image
     * initially,
     * and loads the new image asynchronously if needed.
     */
    private void updateImageIfChanged(String newImagePath, ImageView bookImage) {
        if (!imgPath.equals(newImagePath)) {
            imgPath = newImagePath;

            // Load the image asynchronously
            Task<Image> imageLoadTask = new Task<>() {
                @Override
                protected Image call() {
                    Image image = new Image(imgPath);
                    return image;
                }
            };

            imageLoadTask.setOnSucceeded(event -> bookImage.setImage(imageLoadTask.getValue()));
            imageLoadTask.setOnFailed(event -> System.out.println("Failed to load image: " + imgPath));

            new Thread(imageLoadTask).start();
        }
    }

    private void setQuantityAndStatus(String newQuantityStr) {
        int newQuantity = Integer.parseInt(newQuantityStr);
        quantity = newQuantity;
        if (quantity >= 1) {
            checkBoxButton.setDisable(false);
            statusLabel.setText("Available");
            statusLabel.setStyle("-fx-text-fill: green");
        } else {
            checkBoxButton.setDisable(true);
            statusLabel.setText("Borrowed");
            statusLabel.setStyle("-fx-text-fill: red");
        }
    }

    private void openPopUp(String fxmlPath, EnumUtils.PopupList popupType) {
        ChangeScene.openAdminPopUp(UserGlobalController.getInstance().getStackPaneContainer(),
                fxmlPath, bookID, popupType);
    }

    // Getters and Setters methods
    public JFXCheckBox getCheckBoxButton() {
        return checkBoxButton;
    }

}
