package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class deletePopupController implements Initializable {
    public static BookInfo book;
    public static int index;
    public static Stage parent;
    @FXML
    private Text bookName;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookName.setText("\"" + book.name + "\" ?");
    }
    @FXML
    void onNoClicked(ActionEvent event) {
        Node old = (Node) event.getSource();
        Stage stage = (Stage) old.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onYesClicked(ActionEvent event) throws SQLException, IOException {
        if(Main.email!=null) Database.removeFromCart(Main.email, book.ISBN);
        else Main.tempCart.remove(index);
        ((CartController) parent.getScene().getUserData()).reload();
        Node old = (Node) event.getSource();
        Stage stage = (Stage) old.getScene().getWindow();
        stage.close();
    }
}