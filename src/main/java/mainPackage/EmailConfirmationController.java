package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class EmailConfirmationController {
    @FXML protected Label label;
    @FXML protected TextField OTP;
    @FXML public void onConfirmButtonClick(ActionEvent event) throws IOException {
        if(Integer.valueOf(OTP.getText()).equals(Main.otp))
        {
            SceneChanger.changeTo("ChangePasswordMenu.fxml", event);
        }
        else
        {
            label.setText("OTP doesn't match");
        }
    }
    @FXML public void onBackButtonClick(ActionEvent event) throws IOException
    {
        SceneChanger.changeTo("ForgotPasswordMenu.fxml", event);
    }
}
