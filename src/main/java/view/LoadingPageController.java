package view;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import util.AnimationUtils;
import view.admin.AdminDashboardController;
import view.admin.AdminGlobalFormController;

public class LoadingPageController {

    @FXML
    private ImageView loadingGif;

    @FXML
    private ImageView loadingLogo;

    @FXML
    private Pane loadingPane;

    private static LoadingPageController controller;

    public LoadingPageController() {
        controller = this;
    }

    public static LoadingPageController getInstance() {
        return controller;
    }

    public void initialize() {
        System.out.println("Loading Pane Controller initialized");
        AnimationUtils.zoomIn(loadingPane, 1);
    }

    public Pane getLoadingPane() {
        return loadingPane;
    }
}
