module app.libmgmt {
    requires javafx.fxml;
    requires javafx.controls;
    requires transitive javafx.graphics;
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