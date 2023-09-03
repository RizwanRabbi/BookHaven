package mainPackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {
    public static UserInfo userInfo;

    @FXML
    Label t;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        t.setText(
//                userInfo.getName() +
//                        " \n" +
//                        userInfo.getEmail() +
//                        "\n has landed in the house"
//        );
    }
}
