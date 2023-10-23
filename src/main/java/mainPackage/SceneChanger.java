package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class SceneChanger {

    public static void changeTo(String s, ActionEvent event) throws IOException
    {
        FXMLLoader root = new FXMLLoader(Main.class.getResource(s));
        Node oldButton = (Node) event.getSource();
        Scene scene = new Scene(root.load(), oldButton.getScene().getWidth(), oldButton.getScene().getHeight());
        scene.setUserData(root.getController());
        Stage myStage = (Stage) oldButton.getScene().getWindow();
        myStage.setScene(scene);
        myStage.setMinHeight(700);
        myStage.setMinWidth(1000);
        myStage.show();
    }


    public static Stage getCurrentStage()
    {
        for (Window window : Window.getWindows()) {
            if (window instanceof Stage) {
                Stage stage = (Stage) window;
                if (stage.isShowing()) {
                    return stage;
                }
            }
        }
        return null;
    }

    public static void changeTo(String s, Label event) throws IOException
    {
        FXMLLoader root = new FXMLLoader(Main.class.getResource(s));
        Node oldButton = event;
        Scene scene = new Scene(root.load(), oldButton.getScene().getWidth(), oldButton.getScene().getHeight());
        scene.setUserData(root.getController());
        Stage myStage = getCurrentStage();
        myStage.setScene(scene);
        myStage.setMinHeight(700);
        myStage.setMinWidth(1000);
        myStage.show();
    }
    public static void changeTo(String s, VBox event) throws IOException
    {
        FXMLLoader root = new FXMLLoader(Main.class.getResource(s));
        Node oldButton = event;
        Scene scene = new Scene(root.load(), oldButton.getScene().getWidth(), oldButton.getScene().getHeight());
        scene.setUserData(root.getController());
        Stage myStage = getCurrentStage();
        myStage.setScene(scene);
        myStage.setMinHeight(700);
        myStage.setMinWidth(1000);
        myStage.show();
    }
    public static void changeTo(String s, HBox event) throws IOException
    {
        FXMLLoader root = new FXMLLoader(Main.class.getResource(s));
        Node oldButton = event;
        Scene scene = new Scene(root.load(), oldButton.getScene().getWidth(), oldButton.getScene().getHeight());
        scene.setUserData(root.getController());
        Stage myStage = getCurrentStage();
        myStage.setScene(scene);
        myStage.setMinHeight(700);
        myStage.setMinWidth(1000);
        myStage.show();
    }
}
