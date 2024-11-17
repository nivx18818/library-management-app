package app.libmgmt.view.user;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class UserGlobalController {

    private static UserGlobalController controller;

    // Observable Lists for managing data
    private final ObservableList<String[]> borrowedBooksData = FXCollections
            .observableArrayList(preLoadBorrowedBooksData());

    @FXML
    private Pane backgroundPane;

    @FXML
    private HBox globalFormContainer;

    @FXML
    private Pane pagingPane;

    @FXML
    private StackPane stackPaneContainer;

    // Constructor and Singleton Pattern
    public UserGlobalController() {
        controller = this;
    }

    public static UserGlobalController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        System.out.println("User Global Form initialized");
    }

    // Data Pre-loading Methods
    private List<String[]> preLoadBorrowedBooksData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] { "1",
                "https://99designs-blog.imgix.net/blog/wp-content/uploads/2017/01/enceladus.jpg?auto=format&q=60&fit=max&w=930",
                "Story Book", "2", "11/11/2024", "17/11/2024" });
        data.add(new String[] { "1",
                "https://marketplace.canva.com/EAFaQMYuZbo/1/0/1003w/canva-brown-rusty-mystery-novel-book-cover-hG1QhA7BiBU.jpg",
                "Story Book", "2", "11/11/2024", "17/11/2024" });
        data.add(new String[] { "1",
                "https://th.bing.com/th/id/OIP.FTtvbIR52uSVcUrvWBENsAHaLG?w=1170&h=1753&rs=1&pid=ImgDetMain",
                "Story Book", "2", "11/11/2024", "17/11/2024" });
        data.add(new String[] { "1",
                "https://cdn11.bigcommerce.com/s-ep2pzxabtm/images/stencil/1280w/products/395/816/healinggrief__40416.1554653848.jpg?c=2",
                "Story Book", "2", "11/11/2024", "17/11/2024" });
        return data;
    }

    // Getter Methods for Data
    public List<String[]> getBorrowedBooksData() {
        return borrowedBooksData;
    }

    // Getter Methods for UI components
    public Pane getPagingPane() {
        return pagingPane;
    }

    public Pane getBackgroundPane() {
        return backgroundPane;
    }

}
