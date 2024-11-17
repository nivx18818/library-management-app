package app.libmgmt.view.user;

import com.jfoenix.controls.JFXButton;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class UserCatalogBorrowedBookBarController {

    @FXML
    private Label amountLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private JFXButton bookReturnedButton;

    @FXML
    private Label borrowedDateLabel;

    @FXML
    private Label dueDateLabel;

    @FXML
    private HBox hBoxReturn;

    @FXML
    private Label nameLabel;

    @FXML
    private Label orderLabel;

    @FXML
    private ImageView returnImage;

    @FXML
    private Label returnLabel;

    @FXML
    private Pane returnPane;

    @FXML
    private Pane viewPane;

    @FXML
    private ImageView imageView;

    private final String hoverReturnedLogo = "/assets/icon/redo 1 (1).png";
    private final String returnedLogo = "/assets/icon/redo 1.png";
    private final String hoverViewLogo = "/assets/icon/Property 1=Variant2.png";
    private final String viewLogo = "/assets/icon/btn view.png";

    @FXML
    void btnBookReturnedOnAction(ActionEvent event) {

    }
    
    @FXML
    void btnBookReturnedOnMouseEntered(MouseEvent event) {
        hBoxReturn.setStyle("-fx-background-color: #f2f2f2; -fx-background-radius: 10px; -fx-border-color: #000; -fx-border-radius: 10px; -fx-border-width: 1px;");
        returnImage.setImage(new Image(getClass().getResource(hoverReturnedLogo).toExternalForm()));
        returnLabel.setStyle("-fx-text-fill: #000;");
    }

    @FXML
    void btnBookReturnedOnMouseExited(MouseEvent event) {
        hBoxReturn.setStyle("-fx-background-color: #000; -fx-background-radius: 10px;");
        returnImage.setImage(new Image(getClass().getResource(returnedLogo).toExternalForm()));
        returnLabel.setStyle("-fx-text-fill: #f2f2f2;");
    }

    @FXML
    void imageViewOnMouseClicked(MouseEvent event) {

    }

    @FXML
    void imageViewOnMouseEntered(MouseEvent event) {
        imageView.setImage(new Image(getClass().getResource(hoverViewLogo).toExternalForm()));
    }

    @FXML
    void imageViewOnMouseExited(MouseEvent event) {
        imageView.setImage(new Image(getClass().getResource(viewLogo).toExternalForm()));
    }

    public void setData(String[] data) {
        orderLabel.setText(data[0]);
        if (data[1] != null) {
            updateImage(data[1], bookImage);
        }
        nameLabel.setText(data[2]);
        amountLabel.setText(data[3]);
        borrowedDateLabel.setText(data[4]);
        dueDateLabel.setText(data[5]);

    }

    private void updateImage(String imgPath, ImageView bookImage) {
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

    public void setVisibleAction(boolean isReturned) {
        returnPane.setVisible(isReturned);
        viewPane.setVisible(!isReturned);
    }

}
