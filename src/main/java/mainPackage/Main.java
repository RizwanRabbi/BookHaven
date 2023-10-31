package mainPackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {
    public static UserInfo userInfo;
    public static ArrayList<CartItem> tempCart;
    public static String email, firstName, lastName, password, accountType;
    public static int otp;
    @Override
    public void start(Stage stage) throws IOException {

//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainMenu.fxml"));
        tempCart = new ArrayList<>();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("UserDashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        scene.setUserData(fxmlLoader.getController());
        stage.setTitle("BOOKHAVEN!");
        stage.setScene(scene);
        stage.setMinHeight(700);
        stage.setMinWidth(1000);
        stage.show();
        System.out.println(tempCart.size() + "size");
    }

    public static void run(String[] args) {
        launch();
    }

}
