package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginPageController {
    @FXML
    public TextField emailBox;
    @FXML
    public TextField passwordBox;
    @FXML
    public Label warningBox;
    @FXML
    protected void signUpButtonClick (ActionEvent e) throws IOException {
//        SceneChanger.changeTo("",e);
    }
    @FXML
    protected void loginButtonClick (ActionEvent e) throws SQLException, IOException {
        String email = emailBox.getText();
        String pword = Database.hash(passwordBox.getText());
        System.out.println(pword);

        UserInfo userInfo = Database.getUserInfo(email);
        if(pword.equals(userInfo.getPassword()))
        {
            HomePageController.userInfo = userInfo;
//            will change to HomePage
//            SceneChanger.changeTo("HomePage.fxml",e);
        }
        else
        {
            warningBox.setText("Incorrect E-mail/ Password");
        }
    }



}
