module org.example.main {
    requires javafx.fxml;
    requires animatefx;
    requires com.jfoenix;
    requires java.desktop;
    requires java.logging;

    opens main to javafx.fxml;
    exports main;
    opens view to javafx.fxml;
    exports view;
}