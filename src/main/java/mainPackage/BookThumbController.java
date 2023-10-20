package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.Objects;

public class BookThumbController {
    @FXML
    Button addCartButton;
    public String isbn;
    @FXML
    public TextField author;

    @FXML
    public VBox backVB;

    @FXML
    public ImageView bookImage;

    @FXML
    public TextField bookName;

    @FXML
    public TextField price;

    @FXML
    void onAddToCartButtonClick(ActionEvent event) throws SQLException {

        if (Main.email != null)
        {
            if(!Database.alreadyInCart(Main.email, isbn))
            {
                Database.addToCart(Main.email,isbn, 1);
            }
        }
        else
        {
            boolean flag = true;
            for (CartItem i: Main.tempCart)
            {
                if(Objects.equals(i.ISBN, isbn)) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                CartItem t = new CartItem(isbn, 1);
                Main.tempCart.add(t);
            }
        }

        addCartButton.setText("Added To Cart");
        addCartButton.setDisable(true);
    }
    @FXML
    void onThumbClick(MouseEvent event) {
        System.out.println("Click Click " + isbn);
    }


}
