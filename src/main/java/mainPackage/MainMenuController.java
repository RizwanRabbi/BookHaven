package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo("LoginMenu.fxml", event);
    }
    @FXML
    protected void onSignUpButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo("SignUpMenu.fxml", event);
    }
    @FXML
    protected void onExitButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo("userDashboard.fxml", event);
//        Node oldButton = (Node) event.getSource();
//        Stage myStage = (Stage) oldButton.getScene().getWindow();
//        myStage.close();
    }

}
