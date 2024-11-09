package util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import initialize.AdminInitializer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.admin.*;

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

    public static void openAdminPopUp(StackPane stackPane, String path, String id,
                                      EnumUtils.PopupList popupList) {
        try {
            FXMLLoader loader = new FXMLLoader(AdminNavigationController.class.getResource(path));
            Pane content = loader.load();

            dialog = new JFXDialog(stackPane, content,
                    JFXDialog.DialogTransition.CENTER);

            switch (popupList) {
                case BORROWED_BOOK_CATALOG:
                case OVERDUE_BOOK_DASHBOARD:
                    AdminBorrowedBookViewDialogController borrowedController = loader.getController();
                    borrowedController.setId(id);
                    break;
                case BOOK_VIEW:
                case STUDENT_VIEW:
                case BOOK_EDIT:
                case USER_EDIT:
                    break;
                case BOOK_DELETE, USER_DELETE:
                    AdminDeleteConfirmationDialogController deleteController = loader.getController();
                    if (popupList == EnumUtils.PopupList.BOOK_DELETE) {
                        deleteController.setId(id, popupList);
                    } else if (popupList == EnumUtils.PopupList.USER_DELETE) {
                        deleteController.setId(id, popupList);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + popupList);
            }

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

    public static void navigateToScene(JFXButton button, String fxmlPath, EnumUtils.NavigationButton
            latestButtonClicked) throws IOException {
        if (latestButtonClicked.equals(button.getText().toLowerCase())) {
            return;
        }

        AdminGlobalFormController controller = AdminGlobalFormController.getInstance();

        Pane pane = new Pane();
        if (fxmlPath.contains("loading")) {
            pane = controller.getBackgroundPane();
        }
        else {
            pane = controller.getPagingPane();
        }

        pane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(AdminGlobalFormController.class.getResource(
                "/fxml/" + fxmlPath));
        Parent root = loader.load();
        pane.getChildren().add(root);
        AnimationUtils.zoomIn(controller.getPagingPane(), 1.0);
    }

    public static void changeInterfaceWindow(Stage stage, String fxmlPath, String title) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AdminInitializer.class.getResource(
                    fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
