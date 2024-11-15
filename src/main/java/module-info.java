module org.example.initialize {
    requires javafx.fxml;
    requires animatefx;
    requires com.jfoenix;
    requires java.desktop;
    requires java.sql;

    opens app.libmgmt.initializer to javafx.fxml;
    exports app.libmgmt.initializer;
    opens app.libmgmt.view to javafx.fxml;
    exports app.libmgmt.view;
    exports app.libmgmt.view.admin;
    exports app.libmgmt.view.user;
    opens app.libmgmt.view.user to javafx.fxml;
    opens app.libmgmt.view.admin to javafx.fxml;
}