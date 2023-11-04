package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;

public class OrderNotFoundController {

    @FXML
    private Text textField;
    @FXML
    void buttonClicked(ActionEvent event) throws IOException {
        SceneChanger.changeTo("OrderSearch.fxml",event);
    }
}