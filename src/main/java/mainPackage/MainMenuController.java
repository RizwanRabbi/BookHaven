package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;


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
        UserDashboardController.bookInfos = null;
        SceneChanger.changeTo("UserDashboard.fxml", event);
    }


    @FXML
    void orderSearch(ActionEvent event) throws IOException {
        SceneChanger.changeTo("OrderSearch.fxml", event);
    }
}
