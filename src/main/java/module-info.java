module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires java.mail;
    requires junit;
    requires java.net.http;
    requires org.controlsfx.controls;
    opens mainPackage to javafx.fxml;
    exports mainPackage;
}