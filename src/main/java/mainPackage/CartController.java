package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CartController implements Initializable {

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

    public static ArrayList<BookInfo> books;
    @FXML
    void goDashboard(MouseEvent event) throws IOException {
        SceneChanger.changeTo("userDashboard.fxml", vb);
    }

    @FXML
    void onCartIconClick(MouseEvent event) {
        // Do Nothing cause already here HAHAHAHA
    }

    @FXML
    void onProfileImageClick(MouseEvent event) throws IOException {
        System.out.println("Profile");
        UserProfileController.returnTo = "Cart.fxml";
        if(Main.email == null)
            SceneChanger.changeTo("MainMenu.fxml", vb);
        else
            SceneChanger.changeTo("UserProfile.fxml", vb);
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
        updateTotal();
    }

    public void updateTotal()
    {
        long total=0;
        for (BookInfo u: books)
        {
            total += u.price * Long.min(u.cartQuantity, u.quantity);
        }
        totalBox.setText("Total: "+ total +"\n+Delivery Fee");
    }
    public void onGobackButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo(returnTo, event);
    }
    @FXML
    void onProceedToBuyClicked(ActionEvent event) {

    }
}
