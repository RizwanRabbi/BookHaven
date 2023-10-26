package mainPackage;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignUpController {
    public static String previous;
    @FXML
    private TextField email;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Label emptyField;

    boolean NoNumber(String pass)
    {
        boolean f = false;
        // For number
        for(int i = 0; i < pass.length(); i++)
            if(Character.isDigit(pass.charAt(i)))
                f = true;
        if(f == false)
            return true;
        return false;
    }
    boolean NoSpecialCharadter(String pass)
    {
        boolean f = false;
        // For Special Character
        for(int i = 0; i < pass.length(); i++)
            if((Character.isDigit(pass.charAt(i)) || Character.isAlphabetic(pass.charAt(i))) == false)
                f = true;
        if(f) return false;
        else return true;
    }
    @FXML
    protected void onSignupButtonClick(ActionEvent event) throws IOException
    {
        // Register
        String Email = email.getText();
        Main.email = Email;
        String Password = Database.hash(password.getText());
        String ConfirmPassword = Database.hash(confirmPassword.getText());
        Main.password = Password;
        String FirstName = firstName.getText();
        Main.firstName = FirstName;
        String LastName = lastName.getText();
        Main.lastName = LastName;
        int otp = (int) (Math.random() * (999999-100000) + 1000000);
        if(FirstName.length() == 0 || LastName.length() == 0 || Email.length() == 0)
        {
            emptyField.setText("No field can be empty");
            return;
        }
        else emptyField.setText("");

        if(Password.length() < 8)
        {
            emptyField.setText("Password should be at least 8 character");
            return;
        }
        else if(NoNumber(password.getText()))
        {
            emptyField.setText("Password should have at least 1 number");
        }
        else if(NoSpecialCharadter(password.getText()))
        {
            emptyField.setText("Password should have 1 or more special sign");
        }
        else if(!Objects.equals(ConfirmPassword, Password))
        {
            emptyField.setText("Passwords don't match");
            return;
        }
        else if(Database.accountAlreadyExists(Email))
        {
            emptyField.setText("Seems like you are already registered");
        }
        else if(MailService.sendMail(Email,"OTP: " + String.valueOf(otp), "Account Verification"))
        {
            Main.otp = otp;
            SceneChanger.changeTo("SignUpVerification.fxml", event);
        }
        else
        {
            emptyField.setText("Invalid Email address");
        }
    }
    @FXML
    protected void onGoBackButtonClick(ActionEvent event) throws IOException
    {
        SceneChanger.changeTo(previous, event);
    }
}
