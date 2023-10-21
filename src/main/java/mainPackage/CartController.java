package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    public static ArrayList<CartItem> cartItems;
    public static String returnTo;
    @FXML
    private HBox header;

//    @FXML
//    private HBox cartIcon;

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView profileImage;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchBar;

    @FXML
    private TextArea totalBox;
    @FXML
    private Button searchButton;

    public static ArrayList<BookInfo> books;
    @FXML
    void goDashboard(MouseEvent event) throws IOException {
        SceneChanger.changeTo("userDashboard.fxml", header);
    }

    @FXML
    void onCartIconClick(MouseEvent event) {
        // Do Nothing cause already here HAHAHAHA
    }

    @FXML
    void onProfileImageClick(MouseEvent event) throws IOException {
        System.out.println("Profile");
        if(Main.email == null)
            SceneChanger.changeTo("MainMenu.fxml", header);
        else
            SceneChanger.changeTo("UserProfile.fxml", header);
    }

    @FXML
    void onSearchButtonClick(ActionEvent event) {
        System.out.println("Search");
        System.out.println(searchBar.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        books = new ArrayList<>();
        if(Main.email !=null)
        {
            try {
                books = Database.getUserCartBooks(Main.email);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        else
        {
            cartItems = Main.tempCart;
            try {
                books = Database.getBooksGivenCartInfo(cartItems);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i< books.size(); i++)
        {
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
            int x = 3 , y = 2*i+3;
            gridPane.add(root, x, y);
        }
        updateTotal();
    }

    public void updateTotal()
    {
        long total=0;
        for (BookInfo u: books)
        {
            total += u.price * u.cartQuantity;
        }
        totalBox.setText("Total: "+ total +"\n+Delivery Fee");
    }
    @FXML
    void onGobackButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo(returnTo, event);
    }
    @FXML
    void onProceedToBuyClicked(ActionEvent event) {

    }
}
