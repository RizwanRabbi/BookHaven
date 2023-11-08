package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OrderSearchController implements Initializable {

    @FXML
    private Label errorLabel;

    @FXML
    private TextField orderIDField;

    @FXML
    private TextField phoneField;

    @FXML
    void backButtonClicked(ActionEvent event) throws IOException {
        SceneChanger.changeTo("MainMenu.fxml", event);
    }

    @FXML
    void searchClicked(ActionEvent event) throws IOException, SQLException {
        String phone, idStr;


        phone = phoneField.getText();
        idStr = orderIDField.getText();
        System.out.println(phone +"<- Phone   ID->"+ idStr);

        if( phone.isEmpty())
        {
            errorLabel.setText("Please Provide a Phone No.");
            return;
        }
        if(idStr.isEmpty())
        {
            errorLabel.setText("Please Provide Your Order ID");
            return;
        }
        if( !isNumber(phone) )
        {
            errorLabel.setText("Phone No must only contain numbers");
            return;
        }
        if( !isNumber(idStr))
        {
            errorLabel.setText("Order ID only contains numbers");
            return;
        }

        int orderID = Integer.parseInt(idStr);

        OrderInfo o = Database.searchOrder(orderID, phone);
        if(o==null)
        {
            SceneChanger.changeTo("OrderNotFound.fxml", event);
        }
        else
        {
            OrderConfirmORInfoViewController.previous = "OrderSearch.fxml";
            OrderConfirmORInfoViewController.orderInfo = o;
            OrderConfirmORInfoViewController.books = Database.getOrderedBooks(o.orderID);
            SceneChanger.changeTo("OrderConfirmORInfoView.fxml", event);
        }
    }

    private boolean isNumber(String s) {
        if (s == null || s.isEmpty()) {
            return false; // Return false for empty or null strings
        }
        return s.matches("^[0-9]+$");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Only Allows Number Input

        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneField.setText(oldValue);
            }
        });

        orderIDField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneField.setText(oldValue);
            }
        });
    }
}
