
package mainPackage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderThumbController {

    public OrderInfo orderInfo;
    private ArrayList<OrderInfo> orders;
    @FXML
    private Label orderIdLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Text addressField;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label totalLabel;

    @FXML
    void onThumbClicked(MouseEvent event) throws IOException, SQLException {
        OrderConfirmORInfoViewController.previous = "ViewPreviousOrdersList.fxml";
        OrderConfirmORInfoViewController.books = Database.getOrderedBooks(orderInfo.orderID);
        OrderConfirmORInfoViewController.orderInfo = orderInfo;
        SceneChanger.changeTo("OrderConfirmORInfoView.fxml", statusLabel);
    }

    public void notFoundInitialize()
    {
        orderIdLabel.setText("");
        phoneLabel.setText("");
        nameLabel.setText("No orders were found");
        totalLabel.setText("");
        addressField.setText("");
        statusLabel.setText("");
    }


    public void init() {
        orderInfo.print();
        orderIdLabel.setText("OrderID : " + orderInfo.orderID);
        phoneLabel.setText("Phone No: : " + orderInfo.phoneNo);
        nameLabel.setText("Name : " + orderInfo.name);
        totalLabel.setText((orderInfo.totalAmount + orderInfo.shippingCosts) + " taka");
        addressField.setText(orderInfo.address);
        statusLabel.setText(OrderInfo.statusToString(orderInfo.status));
    }
}
