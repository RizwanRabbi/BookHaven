package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class LoginMenuController
{
    @FXML
    public TextField email;
    @FXML
    public TextField password;
    @FXML
    Label wrongPassword;
    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException, SQLException {
        // Register
        String Email = email.getText();
        String Password = Database.hash(password.getText());
//        String Password = password.getText();
        UserInfo accountInfo = Database.getUserInfo(Email);
        Main.email = Email;
        Main.firstName = accountInfo.fname;
        Main.lastName = accountInfo.lname;
        Main.accountType = "user";

        int loginVal = Database.loginValidation(Email, Password);
        if(loginVal == 0)
        {
            wrongPassword.setText("Incorrect Email / Password");
            return;
        }
        else if(loginVal == 1 || loginVal == 2)
        {
            SceneChanger.changeTo("userDashboard.fxml", event);
        }
    }
    @FXML
    protected void onGoBackButtonClick(ActionEvent event) throws IOException
    {
        SceneChanger.changeTo("MainMenu.fxml", event);
    }
    @FXML
    public void onForgotPasswordButtonClick(ActionEvent event) throws IOException
    {
        SceneChanger.changeTo("ForgotPasswordMenu.fxml", event);
    }
}
