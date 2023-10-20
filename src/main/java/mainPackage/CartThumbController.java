package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CartThumbController {

    public BookInfo book;
    public int index;

    @FXML
    private Label authorLabel;

    @FXML
    private Label bookNameLabel;


    @FXML
    private Label priceLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label stockLabel;
    @FXML
    private Button minusButton;
    @FXML
    private Button plusButton;

    void updateLabels() {
        quantityLabel.setText(String.valueOf(book.cartQuantity));
        priceLabel.setText("Price: " + book.price + " * "
                + book.cartQuantity + " = "
                + book.price * book.cartQuantity);
    }
    @FXML
    void minusButtonClicked(ActionEvent event) throws SQLException {
        book.cartQuantity = book.cartQuantity - 1;
        if(book.cartQuantity == 1)
            minusButton.setDisable(true);
        plusButton.setDisable(false);
        updateLabels();
        if(Main.email != null)
            Database.updateCart(Main.email, book.ISBN, book.cartQuantity);
        else ;
        Main.tempCart.get(index).quantity--;
    }

    @FXML
    void plusButtonClicked(ActionEvent event) throws SQLException {
        book.cartQuantity = book.cartQuantity + 1;
        if(book.quantity == book.cartQuantity)
            plusButton.setDisable(true);
        minusButton.setDisable(false);
        updateLabels();
        Database.updateCart(Main.email, book.ISBN, book.cartQuantity);
        Main.tempCart.get(index).quantity++;
    }
    void initialize()
    {
        book.cartQuantity =  Long.min(book.quantity,book.cartQuantity);
        bookNameLabel.setText(book.name);
        authorLabel.setText(book.author);
        stockLabel.setText("** "+ book.quantity +" in stock");
        updateLabels();
        if(book.cartQuantity == 1)
            minusButton.setDisable(true);
        if(book.quantity == book.cartQuantity)
            plusButton.setDisable(true);
    }

    @FXML
    void onDeleteIconClicked(MouseEvent event) throws SQLException, IOException {
        if(Main.email!=null) {
            Database.removeFromCart(Main.email, book.ISBN);
        }
        else
            Main.tempCart.remove(index);
        SceneChanger.changeTo("Cart.fxml", bookNameLabel);

    }
}
