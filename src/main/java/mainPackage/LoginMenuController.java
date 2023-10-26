package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginMenuController
{
    public static String previous;
    public static String next;
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

        int loginVal = Database.loginValidation(Email, Password);
        if(loginVal == 0)
        {
            wrongPassword.setText("Incorrect Email / Password");
            return;
        }
        else if(loginVal == 1 || loginVal == 2)
        {
            Main.email = Email;
            UserInfo accountInfo = Database.getUserInfo(Email);
            Main.firstName = accountInfo.fname;
            Main.lastName = accountInfo.lname;
            Main.accountType = "user";
            if(!Main.tempCart.isEmpty())
            {
                for (CartItem u : Main.tempCart)
                    u.addToUserCart(Main.email);
                Main.tempCart = new ArrayList<>();
            }
            Main.userInfo = Database.getUserInfo(Main.email);
            SceneChanger.changeTo(next, event);
        }
    }
    @FXML
    protected void onGoBackButtonClick(ActionEvent event) throws IOException
    {
        SceneChanger.changeTo(previous, event);
    }
    @FXML
    public void onForgotPasswordButtonClick(ActionEvent event) throws IOException
    {
        ForgotPasswordController.returnTo = "LoginMenu.fxml";
        SceneChanger.changeTo("ForgotPasswordMenu.fxml", event);
    }
}
