module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires java.mail;
    opens mainPackage to javafx.fxml;
    exports mainPackage;
}