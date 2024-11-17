module app.libmgmt {
    requires animatefx;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;
    requires java.sql;
    requires org.json;
    requires com.zaxxer.hikari;

    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires transitive com.jfoenix;

    exports app.libmgmt.initializer;
    exports app.libmgmt.view;
    exports app.libmgmt.view.admin;
    exports app.libmgmt.view.user;
    exports app.libmgmt.util;
    exports app.libmgmt.model;

    opens app.libmgmt.initializer to javafx.fxml;
    opens app.libmgmt.view to javafx.fxml;
    opens app.libmgmt.view.admin to javafx.fxml;
    opens app.libmgmt.view.user to javafx.fxml;
}