package util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import view.admin.AdminGlobalFormController;
import view.admin.AdminNavigationController;

import java.io.IOException;

public class ChangeScene {
    public static JFXDialog dialog;

    public static void openAdminPopUp(StackPane stackPane, String path) {
        try {
            FXMLLoader loader = new FXMLLoader(AdminNavigationController.class.getResource(path));
            Pane content = loader.load();

            dialog = new JFXDialog(stackPane, content,
                    JFXDialog.DialogTransition.CENTER);

            dialog.setOverlayClose(false);

            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openUserPopUp(StackPane stackPane, String path) {
        //TODO: Implement user pop up
    }

    public static void closePopUp() {
        dialog.close();
    }

    public static void navigateToScene(JFXButton button, String fxmlPath, String latestButtonClicked) throws IOException {
        if (latestButtonClicked.equals(button.getText().toLowerCase())) {
            return;
        }

        AdminGlobalFormController controller = AdminGlobalFormController.getInstance();

        Pane pane = controller.getPagingPane();
        pane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(AdminGlobalFormController.class.getResource("/fxml/" + fxmlPath));
        Parent root = loader.load();
        pane.getChildren().add(root);
        Animation.zoomIn(controller.getPagingPane(), 1.0);
    }
}
