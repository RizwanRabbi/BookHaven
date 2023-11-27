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
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReviewBookController implements Initializable {
    public static BookInfo bookInfo;
    @FXML
    private Button writeReview;
    @FXML
    private Button addCartButton;

    @FXML
    private ImageView bookImage;

    @FXML
    private TextField bookName1;

    @FXML
    private Text bookNameField;

    @FXML
    private HBox cartIcon;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button minusButton;

    @FXML
    private Label numberOfItemsLabel;

    @FXML
    private Button plusButton;

    @FXML
    private ImageView profileImage;

    @FXML
    private Label quantityLabel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchButton;

    @FXML
    private TextField stockLabel;
    private int numberItemsInCart;
    private int indx;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Assume that the user has not bought the book first
        plusButton.setDisable(true);
        plusButton.setVisible(false);

        minusButton.setDisable(true);
        minusButton.setVisible(false);

        numberItemsInCart = 0; // initialize
        // Book Fields
        bookNameField.setText(bookInfo.name);
        stockLabel.setText(bookInfo.quantity + " copies available");
        // Book Image
        bookImage.setImage(bookInfo.image);
        bookImage.setFitHeight(350);
        try {
            ArrayList<Pair<Pair<String,String>, Double>> reviews = Database.getAllReviews(bookInfo);
            int i = 0;
            for(Pair<Pair<String,String>, Double> p : reviews) {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("CommentThumb.fxml"));
                Parent root = loader.load();
                CommentThumbController c = loader.getController();
                UserInfo u = Database.getUserInfo(p.getKey().getValue());
                c.rating.setRating(p.getValue());
                c.name.setText(u.fname + " " + u.lname);
                c.reviewText.setText(p.getKey().getKey());
                int x = 0 , y = i+1;
                gridPane.add(root, x, y);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(Main.userInfo !=null)
        {
            try {
                if(!Database.boughtTheBook(bookInfo))
                    writeReview.setVisible(false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // Profile Picture
            Circle clip = new Circle(22,22,22);
            profileImage.setFitHeight(44);
            profileImage.setFitWidth(44);
            profileImage.setClip(clip);
            profileImage.setImage(Main.userInfo.image);

            try {
                // Number of Cart Items
                numberItemsInCart = Database.numberOfItemsInCart(Main.email);
                // Check cart IF LOGGED IN
                if(Database.alreadyInCart(Main.email, bookInfo.ISBN)) {
                    bookInfo.willingToPurchaseQuantity = Database.getQuantityFromCart(Main.email, bookInfo.ISBN);
                    alreadyInCartInit();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            writeReview.setVisible(false);
            numberItemsInCart = Main.tempCart.size();

            // Check Cart IF NOT LOGGED IN
            for(int i =0; i< Main.tempCart.size(); i++) {
                if(Main.tempCart.get(i).ISBN.equals(bookInfo.ISBN)) {
                    bookInfo.willingToPurchaseQuantity = Main.tempCart.get(i).quantity;
                    alreadyInCartInit();
                    // INDEX IS INITIALIZED SO WE DONT SEARCH REPEATEDLY
                    indx = i;
                }
            }
        }

        numberOfItemsLabel.setText(String.valueOf(numberItemsInCart));
    }
    void alreadyInCartInit() {

        quantityLabel.setText(String.valueOf(bookInfo.willingToPurchaseQuantity));

        addCartButton.setDisable(true);
        addCartButton.setText("Added to Cart");

        plusButton.setDisable(false);
        plusButton.setVisible(true);

        minusButton.setVisible(true);

        if(bookInfo.willingToPurchaseQuantity > 1) {
            minusButton.setDisable(false);
        }
    }
    @FXML
    void goBack(ActionEvent event) throws IOException {
        SceneChanger.changeTo("UserDashboard.fxml",event);
    }

    @FXML
    void minusButtonClicked(ActionEvent event) throws SQLException {
        bookInfo.willingToPurchaseQuantity = bookInfo.willingToPurchaseQuantity - 1;

        if(bookInfo.willingToPurchaseQuantity == 1) // If quantity is down to 1 we disable the button
            minusButton.setDisable(true);

        quantityLabel.setText(String.valueOf(bookInfo.willingToPurchaseQuantity));

        if(Main.email != null)
            Database.updateCart(Main.email, bookInfo.ISBN, bookInfo.willingToPurchaseQuantity);
        else
            Main.tempCart.get(indx).quantity--;
    }

    @FXML
    void onAddtoCart(ActionEvent event) throws SQLException {
        if(Main.email != null)
        {
            Database.addToCart(Main.email, bookInfo.ISBN, 1);
        }
        else
        {
            indx = Main.tempCart.size();
            CartItem t = new CartItem(bookInfo.ISBN, 1);
            Main.tempCart.add(t);
        }
        bookInfo.willingToPurchaseQuantity = 1;
        numberItemsInCart++;
        numberOfItemsLabel.setText(String.valueOf(numberItemsInCart));
        alreadyInCartInit();
    }

    @FXML
    void onCartIconClick(MouseEvent event) throws IOException {
        CartController.returnTo = "BookView.fxml";
        SceneChanger.changeTo("Cart.fxml", cartIcon);
    }

    @FXML
    void onProfileImageClick(MouseEvent event) throws IOException {
        System.out.println("Profile");
        if(Main.email == null) {
            MainMenuController.next = "BookView.fxml";
            MainMenuController.previous = "BookView.fxml";
            SceneChanger.changeTo("MainMenu.fxml", cartIcon);
        }
        else {
            UserProfileController.returnTo = "BookView.fxml";
            SceneChanger.changeTo("UserProfile.fxml", cartIcon);
        }
    }

    @FXML
    void onSearchButtonClick(ActionEvent event) {
        System.out.println("Search");
    }

    @FXML
    void onShowBookInfoButtonClick(ActionEvent event) throws IOException {
        BookViewController.isbn = bookInfo.ISBN;
        SceneChanger.changeTo("BookView.fxml", event);
    }

    @FXML
    void onWriteReviewButtonClick(ActionEvent event) throws IOException {
        System.out.println("Write Review");
        ReviewThumbController.bookInfo = bookInfo;
        ReviewThumbController.returnTo = "ReviewBook.fxml";
        SceneChanger.changeTo("ReviewThumb.fxml", event);
    }

    @FXML
    void plusButtonClicked(ActionEvent event) throws SQLException {
        if(bookInfo.willingToPurchaseQuantity == 1) // if previously was 1 we enable the minus button
            minusButton.setDisable(false);

        // Increase Quantity
        bookInfo.willingToPurchaseQuantity++;

        quantityLabel.setText(String.valueOf(bookInfo.willingToPurchaseQuantity));

        // Store Info in cart
        if(Main.email != null)
            Database.updateCart(Main.email, bookInfo.ISBN, bookInfo.willingToPurchaseQuantity);
        else
            Main.tempCart.get(indx).quantity++;
    }

}
