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
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserDashboardController implements Initializable {
    @FXML
    private Button removeButton;
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
    private Button searchButton;
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
        removeButton.setDisable(true);
        try {
            bookInfos = Database.getAllBooks();
            displayBooks(bookInfos);

            if(Main.email != null)
            {
                Main.userInfo = Database.getUserInfo(Main.email);
                if(!Main.tempCart.isEmpty())
                {
                    for (CartItem u : Main.tempCart)
                        u.addToUserCart(Main.email);
                    Main.tempCart = new ArrayList<>();
                }
                if(Main.userInfo.image != null) {
                    Circle clip = new Circle(22, 22, 22);
                    profileImage.setFitHeight(44);
                    profileImage.setFitWidth(44);
                    profileImage.setClip(clip);
                    profileImage.setImage(Main.userInfo.image);
                }
            }

            if(Main.email == null) numberOfItems.setText(Integer.toString(Main.tempCart.size()));
            else numberOfItems.setText(Integer.toString(Database.numberOfItemsInCart(Main.email)));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onCartIconClick(MouseEvent event) throws IOException {
        CartController.returnTo = "UserDashboard.fxml";
        SceneChanger.changeTo("Cart.fxml", cartIcon);
    }

    @FXML
    void onProfileImageClick(MouseEvent event) throws IOException {
        System.out.println("Profile");
        if(Main.email == null) {
            MainMenuController.next = "UserDashboard.fxml";
            MainMenuController.previous = "UserDashboard.fxml";
            SceneChanger.changeTo("MainMenu.fxml", cartIcon);
        }
        else {
            UserProfileController.returnTo = "UserDashboard.fxml";
            SceneChanger.changeTo("UserProfile.fxml", cartIcon);
        }
    }

    boolean isAlphaNum(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
    }
    ArrayList<String> extractWords(String s) {
        ArrayList<String> words = new ArrayList<>();
        int i = 0;
        while(i < s.length())
        {
            StringBuilder temp = new StringBuilder();
            while(i < s.length() && !Character.isLetterOrDigit(s.charAt(i))) i++;
            while(i < s.length() && Character.isLetterOrDigit(s.charAt(i))) {
                temp.append(Character.toLowerCase(s.charAt(i)));
                i++;
            }
            if(!temp.isEmpty()) words.add(temp.toString());
            i++;
        }
        return words;
    }

    private void displayBooks(ArrayList<BookInfo> bookInfos) throws IOException, SQLException {
        gridPane.getChildren().clear();
        for(int i = 0; i < bookInfos.size(); i++)
        {
            BookInfo bookInfo = bookInfos.get(i);
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("bookThumb.fxml"));
            Parent root = loader.load();
            BookThumbController c = loader.getController();
            c.author.setText(bookInfo.author);
            c.bookName.setText(bookInfo.name);
            c.price.setText( "৳ " + Long.toString(bookInfo.price));
            c.bookImage.setImage(bookInfo.image);
            c.bookImage.setFitHeight(219);
            c.isbn = bookInfo.ISBN;
            if((Main.email != null && Database.alreadyInCart(Main.email, bookInfo.ISBN)) || findInCartArray(bookInfo))
            {
                c.addCartButton.setText("Added to Cart");
                c.addCartButton.setDisable(true);
            }
            int x = i % 5 + 1, y = i / 5 + 1;
            gridPane.add(root, x, y);
        }
    }
    @FXML
    void onSearchButtonClick(ActionEvent event) throws SQLException, IOException {
        onAnythingTyped();
    }
    @FXML
    void onAnythingTyped() throws SQLException, IOException {
        ArrayList<String> words = extractWords(searchBar.getText());
//        System.out.println(words.size());
        System.out.println(words);
        if(!words.isEmpty()){
            removeButton.setDisable(false);
//            bookInfos = Database.searchBooks(words);
            bookInfos = Database.searchBooksByWordSequence(words);
            displayBooks(bookInfos);
        }
    }
    @FXML
    void onRemoveButtonClick(ActionEvent event) throws SQLException, IOException {
        ArrayList<BookInfo> bookInfos = Database.getAllBooks();
        displayBooks(bookInfos);
        searchBar.clear();
        removeButton.setDisable(true);
    }
}
