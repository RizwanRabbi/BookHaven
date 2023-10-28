package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.security.PublicKey;

public class MainMenuController {
    public static String next;
    public static String previous;
    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException {
        LoginMenuController.previous = "MainMenu.fxml";
        LoginMenuController.next = next;
        SceneChanger.changeTo("LoginMenu.fxml", event);
    }
    @FXML
    protected void onSignUpButtonClick(ActionEvent event) throws IOException {
        SignUpController.previous = "MainMenu.fxml";
        RegisterSuccessController.returnTo = next;
        SceneChanger.changeTo("SignUpMenu.fxml", event);
    }
    @FXML
    protected void onExitButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo("UserDashboard.fxml", event);
//        Node oldButton = (Node) event.getSource();
//        Stage myStage = (Stage) oldButton.getScene().getWindow();
//        myStage.close();
    }

}
