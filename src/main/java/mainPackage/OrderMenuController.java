package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OrderMenuController implements Initializable {
    public static UserInfo orderUserInfo = new UserInfo();
    public static final int InsideDhakaDeliveryFee = 60;
    public static final int OutsideDhakaDeliveryFee = 150;
    public static int orderTotal;
    @FXML
    private TextArea fnameField;
    @FXML
    private TextArea lnameField;
    @FXML
    private TextArea loginStatusField;

    @FXML
    private Button saveOrLoginButton;

    @FXML
    private Button signupButton;

    @FXML
    private TextArea addressField;

    @FXML
    private ToggleGroup deliveryToggle;

    @FXML
    private Label errorLabel;

    @FXML
    private RadioButton insideDhakaRButton;
    @FXML
    private RadioButton outsideDhakaRButton;


    @FXML
    private TextArea orderSummaryField;

    @FXML
    private TextArea phoneField;
    private static int insideDhaka = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Phonefield only acceapts number as input
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // If the new input doesn't match the pattern (non-digit), set the text to the previous value
                phoneField.setText(oldValue);
            }
        });
        errorLabel.setText(""); // No errors at the beginning

        /*
        *   *******************************************************
        *   Users can order without loggin in
        *
        *   saveOrLoginButton is toggled to either
        *           save info       or      Login
        *                   based on wether the user is logged in or not
        *   if the main email is not set we toggle the button
        *   we also disable it and make it invisible 
        *   ********************************************************
        */

        if(Main.email != null)
        {
            // Change Button to Save Info and hide other button
            saveOrLoginButton.setText("Save Information");
            loginStatusField.setText("");
            signupButton.setDisable(true);
            signupButton.setVisible(false);

            // Populate with data from User Database
            if( Main.userInfo.fname != null )
                fnameField.setText(Main.userInfo.fname);
            if( Main.userInfo.lname != null )
                lnameField.setText(Main.userInfo.lname);
            if( Main.userInfo.phoneNo!=null )
                phoneField.setText(Main.userInfo.phoneNo);
            if( Main.userInfo.address != null )
                addressField.setText(Main.userInfo.address);
        }
        else
        {
            if( orderUserInfo.fname != null )
                fnameField.setText(orderUserInfo.fname);
            if( orderUserInfo.lname != null )
                lnameField.setText(orderUserInfo.lname);
            if( orderUserInfo.phoneNo!=null )
                phoneField.setText(orderUserInfo.phoneNo);
            if( orderUserInfo.address != null )
                addressField.setText(orderUserInfo.address);
        }
        if(insideDhaka == 1)
            insideDhakaRButton.setSelected(true);
        else if (insideDhaka == 2)
            outsideDhakaRButton.setSelected(true);

        updateOrderSummary(null);
    }

    @FXML
    void onGoBackButtonClick(ActionEvent event) throws IOException {
        collectUserDataFields();
        SceneChanger.changeTo("Cart.fxml", event);
    }


    @FXML
    void OrderButtonClick(ActionEvent event) throws SQLException, IOException {

        System.out.println("User has selected to order");
        collectUserDataFields();

        if ( !checkAllFieldsFilled() ) return;
        if(!(insideDhakaRButton.isSelected() || outsideDhakaRButton.isSelected()))
        {
            errorLabel.setText("Please Select Inside/Outside Dhaka");
            return;
        }


        OrderInfo orderInfo = inputAllOrderInfo();
        OrderConfirmORInfoViewController.previous = "OrderMenu.fxml";
        OrderConfirmORInfoViewController.orderInfo = orderInfo;
        OrderConfirmORInfoViewController.books = CartController.books;
        SceneChanger.changeTo("OrderConfirmORInfoView.fxml", event);
    }

    private OrderInfo inputAllOrderInfo() throws SQLException {

        OrderInfo o = new OrderInfo();

        o.orderID = Database.generateOrderID();

        o.email = Main.email;   // If User not logged in it must be null
        o.address = addressField.getText();
        o.name = orderUserInfo.fname + " " + orderUserInfo.lname;
        o.phoneNo = orderUserInfo.phoneNo;

        if(insideDhakaRButton.isSelected())
            o.shippingCosts = InsideDhakaDeliveryFee;
        else
            o.shippingCosts = OutsideDhakaDeliveryFee;

        o.status = OrderInfo.PENDING;
        o.totalAmount = orderTotal;

        return o;
    }

    @FXML
    void updateOrderSummary(ActionEvent event) {

        // Toggles state based on RadioButton Selection
        // Adds Inside Dhaka price and outside Dhaka price based on predefined macros

        String s;
        s = "Books Total:\t" +orderTotal +" taka\n";

        if(insideDhakaRButton.isSelected())
            s+= "Delvery fee:\t" + InsideDhakaDeliveryFee+" taka" +
                    "\n" +
                    "\n" +
                    "Total:\t" +(orderTotal+InsideDhakaDeliveryFee) + " taka";
        else if (outsideDhakaRButton.isSelected())
            s+= "Delvery fee:\t" + OutsideDhakaDeliveryFee+" taka" +
                    "\n" +
                    "\n" +
                    "Total:\t" +(orderTotal+OutsideDhakaDeliveryFee) + " taka";
        else
            s+= "Delvery fee:\t" + "Can't Calculate" +
                    "\n" +
                    "\n" +
                    "Select Inside/ Outside Dhaka";
        orderSummaryField.setText(s);
    }
    void collectUserDataFields()
    {
        orderUserInfo.fname = fnameField.getText();
        orderUserInfo.lname = lnameField.getText();
        orderUserInfo.phoneNo = phoneField.getText();
        orderUserInfo.address = addressField.getText();
        orderUserInfo.print();

        if(insideDhakaRButton.isSelected())
            insideDhaka = 1;
        else if (outsideDhakaRButton.isSelected())
            insideDhaka = 2;
    }
    private boolean checkAllFieldsFilled() {
        String fname = fnameField.getText();
        String lname = lnameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();

        if(address.isEmpty() || allSpaces(address) || isNumber(address)) {
            errorLabel.setText("Please provide a valid address");
            return false;
        }
        if(fname.isEmpty() || allSpaces(fname)) {
            errorLabel.setText("Please fill out First Name");
            return false;
        }
        if(lname.isEmpty() || allSpaces(phone)) {
            errorLabel.setText("Please fill out Last Name");
            return false;
        }
        if(phone.isEmpty()) {
            errorLabel.setText("Please provide a Phone Number");
            return false;
        }
        if(!isNumber(phone))
        {
            errorLabel.setText("Phone Number must only contain digits");
        }
        return true;
    }

    private boolean allSpaces(String s)
    {
        if (s == null || s.isEmpty()) {
            return false; // Return false for empty or null strings
        }
        for (int i=0; i<s.length(); i++)
        {
            if(s.charAt(i)!=' ')
                return false;
        }
        return true;
    }

    private boolean isNumber(String s) {
        if (s == null || s.isEmpty()) {
            return false; // Return false for empty or null strings
        }
        return s.matches("^[0-9]+$");
    }


    public void onSignUpButtonClicked(ActionEvent event) throws IOException {
        collectUserDataFields();
        SignUpController.previous = "OrderMenu.fxml";
        RegisterSuccessController.returnTo = "LoginMenu.fxml";
        LoginMenuController.previous = "OrderMenu.fxml";
        LoginMenuController.next = "Cart.fxml";
        SceneChanger.changeTo("SignupMenu.fxml",event);
    }
    @FXML
    void onSaveorLoginButtonClick(ActionEvent event) throws SQLException, IOException {
        collectUserDataFields();
        if(Main.email == null)
        {
            LoginMenuController.previous = "OrderMenu.fxml";
            LoginMenuController.next = "Cart.fxml";
            SceneChanger.changeTo("LoginMenu.fxml",event);
        }
        else
        {
            if ( ! checkAllFieldsFilled() ) return;
            Database.updateUserInfo(orderUserInfo);
            errorLabel.setText("UserInfo Has been saved");
        }
    }
}