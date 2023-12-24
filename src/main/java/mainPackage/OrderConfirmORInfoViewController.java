package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderConfirmORInfoViewController implements Initializable {


    public static String previous;
    public static OrderInfo orderInfo;
    public static ArrayList<BookInfo> books;
    @FXML
    private TextArea addressField;

    @FXML
    private CheckBox agreedCheckBox;

    @FXML
    private GridPane gridoPain;

    @FXML
    private Label nameField;

    @FXML
    private Label phoneField;

    @FXML
    private Label orderIDField;

    @FXML
    private Label statusLabel;

    @FXML
    private Label totalField;

    @FXML
    private Button confirmButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        confirmButton.setDisable(true);
        for(int i = 0; i < books.size(); i++)
        {
            if(books.get(i).quantity == 0)
            {
                books.remove(i);
                i--;
            }
        }
        for (int i = 0; i< books.size(); i++)
        {
            if(previous == "OrderMenu.fxml")
                books.get(i).willingToPurchaseQuantity = Integer.min((int)books.get(i).willingToPurchaseQuantity, (int)books.get(i).quantity);
            books.get(i).print();

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("CartThumb.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CartThumbController c = loader.getController();
            c.book = books.get(i);
            c.index = i;
            c.initialize();
            c.initializeForViewing();
            try {
                if((previous.equals("ViewPreviousOrdersList.fxml" ) || previous.equals("OrderSearch.fxml"))
                    && Database.boughtTheBook(c.book))
                    c.addReview.setVisible(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int x = 20 , y = 2*i+3;
            gridoPain.add(root, x, y);
        }

        orderIDField.setText("Order ID : " +  orderInfo.orderID );
        nameField.setText("Name : "+orderInfo.name);
        phoneField.setText("Phone No : "+orderInfo.phoneNo);
        addressField.setText("Address : "+orderInfo.address);
        totalField.setText(orderInfo.totalAmount +
                " + Delivery fee: " +
                orderInfo.shippingCosts +
                " = "+ (orderInfo.totalAmount + orderInfo.shippingCosts) +
                " taka" );
        if(previous.equals("ViewPreviousOrdersList.fxml" )
                || previous.equals("OrderSearch.fxml"))
            initializeForView();
    }

    @FXML
    void confirmOrder(ActionEvent event) throws SQLException, IOException {
        if( !agreedCheckBox.isSelected())
        {
            return;
        }
        else
        {
            // Insert Into Database
            Database.insertOrder(orderInfo);
            Database.insertOrderBooks(books,orderInfo.orderID);

            // Delete the carts
            // From Database and if not logged in the tempCart in main
            if(Main.email == null)
                Main.tempCart = new ArrayList<>();
            else
                Database.deleteUserCart();

            OrderSuccessController.orderInfo = orderInfo;
            SceneChanger.changeTo("OrderSuccess.fxml",event);

        }
    }

    @FXML
    void onGoBackButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo(previous, event);
    }

    @FXML
    void agreedToCondition(ActionEvent event) {
        if(agreedCheckBox.isSelected())
            confirmButton.setDisable(false);
        else
            confirmButton.setDisable(true);
    }

    public void initializeForView()
    {
        ReviewThumbController.returnTo = "OrderConfirmORInfoView.fxml";
        statusLabel.setText("Status: " + OrderInfo.statusToString(orderInfo.status));
        agreedCheckBox.setDisable(true);
        agreedCheckBox.setVisible(false);
        confirmButton.setVisible(false);
        confirmButton.setDisable(true);
        addressField.setEditable(false);
    }
}