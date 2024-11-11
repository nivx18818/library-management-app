module org.example.initialize {
    requires javafx.fxml;
    requires animatefx;
    requires com.jfoenix;
    requires java.desktop;
    requires java.logging;

    opens initialize to javafx.fxml;
    exports initialize;
    opens view to javafx.fxml;
    exports view;
    exports view.admin;
    exports view.user;
    opens view.user to javafx.fxml;
    opens view.admin to javafx.fxml;
}