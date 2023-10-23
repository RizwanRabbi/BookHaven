package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

public class UserDashboardController implements Initializable {
    @FXML
    private HBox cartIcon;

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView profileImage;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchBar;

    @FXML
    private Button serachButton;
    @FXML
    public Label numberOfItems;
    ArrayList<BookInfo> bookInfos;
    private boolean findInCartArray(BookInfo bookInfo) {
        for(CartItem cartItem : Main.tempCart) {
            if(cartItem.ISBN.equals(bookInfo.ISBN))
                return true;
        }
        return false;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            bookInfos = Database.getAllBooks();
            for(int i = 0; i < bookInfos.size(); i++)
            {
                BookInfo bookInfo = bookInfos.get(i);
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("bookThumb.fxml"));
                Parent root = loader.load();
                BookThumbController c = loader.getController();
                c.author.setText(bookInfo.author);
                c.bookName.setText(bookInfo.name);
                c.price.setText( "৳ " + Long.toString(bookInfo.price));
                c.bookImage.setFitHeight(250);
                c.bookImage.setImage(bookInfo.image);
                c.isbn = bookInfo.ISBN;
                if((Main.email != null && Database.alreadyInCart(Main.email, bookInfo.ISBN)) || findInCartArray(bookInfo))
                {
                    c.addCartButton.setText("Added to Cart");
                    c.addCartButton.setDisable(true);
                }
                int x = i % 5 + 1, y = i / 5 + 1;
                gridPane.add(root, x, y);
            }
            if(Main.email == null) numberOfItems.setText(Integer.toString(Main.tempCart.size()));
            else numberOfItems.setText(Integer.toString(Database.numberOfItemsInCart(Main.email)));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        if(Main.email!=null)
        {
            try {
                Main.userInfo = Database.getUserInfo(Main.email);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    @FXML
    void onCartIconClick(MouseEvent event) throws IOException {
        CartController.returnTo = "userDashboard.fxml";
        SceneChanger.changeTo("Cart.fxml", cartIcon);
    }

    @FXML
    void onProfileImageClick(MouseEvent event) throws IOException {
        System.out.println("Profile");
        UserProfileController.returnTo = "userDashboard.fxml";
        if(Main.email == null)
            SceneChanger.changeTo("MainMenu.fxml", cartIcon);
        else
            SceneChanger.changeTo("UserProfile.fxml", cartIcon);
    }

    @FXML
    void onSearchButtonClick(ActionEvent event) {
        System.out.println("Search");
        System.out.println(searchBar.getText());
    }

}
