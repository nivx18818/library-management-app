package view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class AdminBookEditDialogController {

    @FXML
    private Pane addPane;

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
    private ImageView imgClose;

    @FXML
    private Label lblAdd;

    @FXML
    private Label lblCancel;

    @FXML
    private Label notificationLabel;

    @FXML
    private TextField txtAuthor;

    @FXML
    private TextField txtCoverURL;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPublishedDate;

    @FXML
    private TextField txtPublisher;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtType;

    @FXML
    private JFXButton updateButton;

    public void initialize() {
        System.out.println("Admin Edit Book Dialog initialized");

        container.setOnMouseClicked(
            event -> {
                container.requestFocus();
            }
        );

        setInfo();
    }

    public void setInfo() {
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {

    }

    @FXML
    void closeButtonOnAction(ActionEvent event) {

    }

    @FXML
    void updateButtonOnAction(ActionEvent event) {

    }

}
