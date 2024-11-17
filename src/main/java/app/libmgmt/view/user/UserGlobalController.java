package app.libmgmt.view.user;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class UserGlobalController {

  @FXML
  private Pane backgroundPane;

  @FXML
  private HBox globalFormContainer;

  @FXML
  private Pane pagingPane;

  @FXML
  private StackPane stackPaneContainer;

  @FXML
  public void initialize() {
    System.out.println("User Global Form initialized");
  }

}
