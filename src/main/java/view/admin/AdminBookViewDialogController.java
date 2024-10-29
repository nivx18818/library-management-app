package view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import util.AnimationUtils;
import util.ChangeScene;

public class AdminBookViewDialogController {

    @FXML
    private Label authorLabel;

    @FXML
    private Label idLabel;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private Pane cancelPane;

    @FXML
    private JFXButton closeDialogButton;

    @FXML
    private Pane closePane;

    @FXML
    private Pane container;

    @FXML
    private ImageView coverBookImage;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblCancel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label publishedDateLabel;

    @FXML
    private Label publisherLabel;

    @FXML
    private ImageView qrCodeImage;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label typeLabel;

    AdminGlobalFormController adminGlobalFormController = AdminGlobalFormController.getInstance();

    public void initialize() {
        System.out.println("AdminViewBookDialogController initialized!");
        AnimationUtils.hoverCloseIcons(closeDialogButton, imgClose);
    }

    public void setInfo() {
        String curId = idLabel.getText().split(" : ")[1];
        String[] data = adminGlobalFormController.getBookData(curId);
        for (int i = 0; i < data.length; i++) {
            switch (i) {
                case 0:
                    idLabel.setText("Book ID : " + data[i]);
                    break;
                case 1:
                    try {
                        coverBookImage.setImage(new Image(data[i]));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                    nameLabel.setText("Name : " + data[i]);
                    break;
                case 3:
                    typeLabel.setText("Type : " + data[i]);
                    break;
                case 4:
                    authorLabel.setText("Author : " + data[i]);
                    break;
                case 5:
                    quantityLabel.setText(data[i]);
                    break;
                case 6:
                    publisherLabel.setText("Publisher : " + data[i]);
                    break;
                case 7:
                    publishedDateLabel.setText("Published Date : " + data[i]);
                    break;
            }
        }
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    public void setId(String id) {
        idLabel.setText("Book ID : " + id);
        setInfo();
    }
}
