package view.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

}
