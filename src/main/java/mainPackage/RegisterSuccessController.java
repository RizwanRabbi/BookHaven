package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class RegisterSuccessController {
    @FXML protected Button ok;

    public static String returnTo;
    @FXML public void onOkButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo(returnTo, event);
    }
}
