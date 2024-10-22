package view.admin;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.ChangeScene;

public class AdminBorrowedBookViewDialogController {

    @FXML
    private Pane closePane;

    @FXML
    private Label closeLabel;

    @FXML
    private Label lblDueDate;

    @FXML
    private Label lblId;

    @FXML
    private Label lblTotalBooks;

    @FXML
    private VBox vBox;

    @FXML
    private JFXButton closeButton;

    public void initialize() {
        System.out.println("AdminBorrowedBookViewDialogController initialized");

    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        ChangeScene.closePopUp();
    }

    @FXML
    void btnCloseOnMouseEntered(MouseEvent event) {
        closePane.setStyle("-fx-background-color: #d7d7d7; -fx-background-radius: 12px");
        closeLabel.setStyle("-fx-text-fill: #000000");
    }

    @FXML
    void btnCloseOnMouseExited(MouseEvent event) {
        closePane.setStyle("-fx-background-color: #000000; -fx-background-radius: 12px");
        closeLabel.setStyle("-fx-text-fill: #ffffff");
    }

}
