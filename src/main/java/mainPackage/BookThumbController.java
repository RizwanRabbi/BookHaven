package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
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

    private void updateCartIconWithoutLogin() {
        Stage stage = SceneChanger.getCurrentStage();
        Scene scene = stage.getScene();
        UserDashboardController userDashboardController = (UserDashboardController) scene.getUserData();
        userDashboardController.numberOfItems.setText(Integer.toString(Main.tempCart.size()));
    }
    private void updateCartIconWithLogin() throws SQLException {
        Stage stage = SceneChanger.getCurrentStage();
        Scene scene = stage.getScene();
        UserDashboardController userDashboardController = (UserDashboardController) scene.getUserData();
        userDashboardController.numberOfItems.setText(Integer.toString(Database.numberOfItemsInCart(Main.email)));
    }
    @FXML
    void onAddToCartButtonClick(ActionEvent event) throws SQLException {

        if (Main.email != null)
        {
            if(!Database.alreadyInCart(Main.email, isbn))
            {
                Database.addToCart(Main.email,isbn, 1);
                updateCartIconWithLogin();
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
            updateCartIconWithoutLogin();
        }

        addCartButton.setText("Added To Cart");
        addCartButton.setDisable(true);
    }
    @FXML
    void onThumbClick(MouseEvent event) throws IOException {
        System.out.println("Click Click " + isbn);
        BookViewController.isbn = isbn;
        SceneChanger.changeTo("BookView.fxml", backVB);
    }

}
