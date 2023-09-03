package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginPageController {
    @FXML
    public TextField emailBox;
    @FXML
    public TextField passwordBox;

    @FXML
    protected void signUpButtonClick (ActionEvent e) throws IOException {
//        SceneChanger.changeTo("",e);
    }
    @FXML
    protected void loginButtonClick (ActionEvent e)
    {
        String email = emailBox.getText();
        String pword = Database.hash(passwordBox.getText());


        //  verification;
    }



}
