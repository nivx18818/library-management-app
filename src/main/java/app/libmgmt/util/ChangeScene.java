package app.libmgmt.util;

import app.libmgmt.view.controller.LoginController;
import app.libmgmt.view.controller.admin.AdminBorrowedBookViewDialogController;
import app.libmgmt.view.controller.admin.AdminDeleteConfirmationDialogController;
import app.libmgmt.view.controller.admin.AdminGlobalController;
import app.libmgmt.view.controller.user.UserGlobalController;
import app.libmgmt.view.controller.user.UserReturnBookConfirmationDialogController;

import com.jfoenix.controls.JFXDialog;
import app.libmgmt.initializer.AdminInitializer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangeScene {
    public static JFXDialog dialog;

    /**
     * Open a popup window for admin
     * 
     * @param stackPane The stack pane to contain the popup
     * @param path      The path to the fxml file
     * @param id        <option>The id which is used to identify the object</option>
     * @param popupList The type of popup
     */
    public static void openAdminPopUp(StackPane stackPane, String path, String id,
            EnumUtils.PopupList popupList) {
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(LoginController.class.getResource(path));
            Pane content = loader.load();
            AdminBorrowedBookViewDialogController borrowedController;

            dialog = new JFXDialog(stackPane, content,
                    JFXDialog.DialogTransition.CENTER);

            switch (popupList) {
                case BORROWED_BOOK_CATALOG:
                    borrowedController = loader.getController();
                    borrowedController.setId(id);
                    borrowedController.setTotalBook();
                    break;
                case OVERDUE_BOOK_DASHBOARD:
                    borrowedController = loader.getController();
                    if (id != null && !id.isEmpty()) {
                        borrowedController.setId(id);
                    }
                    borrowedController.setTotalLoan();
                    break;
                case BOOK_DELETE, STUDENT_DELETE, GUEST_DELETE:
                    AdminDeleteConfirmationDialogController deleteController = loader.getController();
                    if (id != null && !id.isEmpty()) {
                        deleteController.setId(id, popupList);
                    }
                    break;
                case RETURN_BOOK:
                    UserReturnBookConfirmationDialogController returnController = loader.getController();
                    if (id != null && !id.isEmpty()) {
                        returnController.setLoanId(Integer.parseInt(id));
                    }
                case ADD_BOOK:
                case ADD_USER:
                case ACQUIRE_BOOK:
                case BOOK_VIEW:
                case USER_VIEW:
                case BOOK_EDIT:
                case USER_EDIT:
                case CHANGE_CREDENTIALS:
                case FORGOT_PASSWORD:
                case LOGOUT:
                case EMPTY_DATA_NOTIFICATION:
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

    public static void openAdminPopUp(StackPane stackPane, String path,
            EnumUtils.PopupList popupList) {
        openAdminPopUp(stackPane, path, null, popupList);
    }

    public static void closePopUp() {
        Platform.runLater(() -> dialog.close());
    }


    public static void navigateToScene(String fxmlPath, EnumUtils.UserType userType) throws IOException {

        Object controller;

        if (userType == EnumUtils.UserType.ADMIN) {
            controller = AdminGlobalController.getInstance();
        } else {
            controller = UserGlobalController.getInstance();
        }

        Pane pane = new Pane();
        if (fxmlPath.contains("loading")) {
            pane = (userType == EnumUtils.UserType.ADMIN ? ((AdminGlobalController) controller).getBackgroundPane()
                    : ((UserGlobalController) controller).getBackgroundPane());
            pane.getChildren().clear();
            pane.getChildren().add(FXMLLoader.load(AdminGlobalController.class.getResource("/fxml/" + fxmlPath)));
            return;
        } else {
            pane = (userType == EnumUtils.UserType.ADMIN ? ((AdminGlobalController) controller).getPagingPane()
                    : ((UserGlobalController) controller).getPagingPane());
        }

        pane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(
                (userType == EnumUtils.UserType.ADMIN ? AdminGlobalController.class.getResource(
                        "/fxml/admin/" + fxmlPath) : UserGlobalController.class.getResource("/fxml/user/" + fxmlPath)));
        Parent root = loader.load();
        pane.getChildren().add(root);
        AnimationUtils
                .zoomIn((userType == EnumUtils.UserType.ADMIN ? ((AdminGlobalController) controller).getPagingPane()
                        : ((UserGlobalController) controller).getPagingPane()), 1.1);
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
