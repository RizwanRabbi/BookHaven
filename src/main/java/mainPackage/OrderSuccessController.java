package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderSuccessController implements Initializable {

    public static OrderInfo orderInfo;
    @FXML
    private Text infoText;
    @FXML
    private TextArea orderIDLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        orderIDLabel.setText(String.valueOf(orderInfo.orderID));

        if(Main.email == null)
        {
            infoText.setText(
                    "Since you didn't Provide us an Email please remember your\n\nOrderID : "
                            + orderInfo.orderID +" and \nPhone Number : " + orderInfo.phoneNo+
                            " \n\nTo keep track of your order");
        }
    }

    @FXML
    void continueButtonClicked(ActionEvent event) throws IOException {
        SceneChanger.changeTo("UserDashboard.fxml",event);
    }
}
