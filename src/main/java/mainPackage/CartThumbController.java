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
    private ImageView imageBaksho;
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
                + Long.min(book.cartQuantity, book.quantity)  + " = "
                + book.price * Long.min(book.cartQuantity, book.quantity));
    }

    @FXML
    void plusButtonClicked(ActionEvent event) throws SQLException, IOException {
        book.cartQuantity = book.cartQuantity + 1;

        if(Main.email != null)
            Database.updateCart(Main.email, book.ISBN, book.cartQuantity);
        else
            Main.tempCart.get(index).quantity++;

        SceneChanger.changeTo("Cart.fxml", bookNameLabel);
    }

    @FXML
    void minusButtonClicked(ActionEvent event) throws SQLException, IOException {
        book.cartQuantity = book.cartQuantity - 1;

        if(Main.email != null)
            Database.updateCart(Main.email, book.ISBN, book.cartQuantity);
        else
            Main.tempCart.get(index).quantity--;

        SceneChanger.changeTo("Cart.fxml", bookNameLabel);
    }
    void initialize()
    {
        bookNameLabel.setText(book.name);
        authorLabel.setText(book.author);
        stockLabel.setText("* "+ book.quantity +" in stock");
        imageBaksho.setImage(book.image);
        updateLabels();
        if(book.cartQuantity <= 1)
            minusButton.setDisable(true);
//        if(book.quantity == book.cartQuantity)
//            plusButton.setDisable(true);
    }

    @FXML
    void onDeleteIconClicked(MouseEvent event) throws SQLException, IOException {
        SceneChanger.createDeletePopup("deletePopup.fxml", authorLabel, book, index);

    }
}
