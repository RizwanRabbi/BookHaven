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
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    private static int total;
    @FXML
    private VBox vb;
    public static String returnTo;
    public static ArrayList<CartItem> cartItems;
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

    @FXML
    private Button buyButton;

    public static ArrayList<BookInfo> books;
    @FXML
    void goDashboard(MouseEvent event) throws IOException {
        SceneChanger.changeTo("UserDashboard.fxml", vb);
    }

    @FXML
    void onProfileImageClick(MouseEvent event) throws IOException {
        System.out.println("Profile");
        if(Main.email == null){
            MainMenuController.next = "Cart.fxml";
            MainMenuController.previous = "Cart.fxml";
            SceneChanger.changeTo("MainMenu.fxml", vb);
            LoginMenuController.previous = "Cart.fxml";
        }
        else {
            UserProfileController.returnTo = "Cart.fxml";
            SceneChanger.changeTo("UserProfile.fxml", vb);
        }
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
                UserInfo u = Database.getUserInfo(Main.email);
                Circle clip = new Circle(22,22,22);
                profileImage.setFitHeight(44);
                profileImage.setFitWidth(44);
                profileImage.setClip(clip);
                profileImage.setImage(u.image);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                books = Database.getUserCartBooks(Main.email);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
//            cartItems = Main.tempCart;
            try {
                books = Database.getBooksGivenCartInfo(Main.tempCart);
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
        if(books.size() == 0)
            buyButton.setDisable(true);

        updateTotal();
    }

    public void updateTotal()
    {
        total = 0;
        for (BookInfo u: books)
        {
            total += u.price * Long.min(u.willingToPurchaseQuantity, u.quantity);
        }
        totalBox.setText("Total: "+ total +"\n+Delivery Fee");
    }
    public void onGobackButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo(returnTo, event);
    }
    @FXML
    void onProceedToBuyClicked(ActionEvent event) throws IOException {
        OrderMenuController.orderTotal = total;
        SceneChanger.changeTo("OrderMenu.fxml",event);
    }

    public void reload() throws IOException {
        SceneChanger.changeTo("Cart.fxml", vb);
    }
}
