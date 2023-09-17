package mainPackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static String email, firstName, lastName, password, accountType;
    public static int otp;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("BOOKHAVEN!");
        stage.setScene(scene);
        stage.setMinHeight(700);
        stage.setMinWidth(1000);
        stage.show();
        System.out.println(Database.hash("12341234!"));
    }

    public static void run(String[] args) {
        launch();
    }
}
