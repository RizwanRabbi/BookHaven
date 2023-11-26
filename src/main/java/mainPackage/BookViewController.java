package mainPackage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BookViewController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            book = Database.getBookGivenISBN(isbn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        book.print();

        // Assume that the user has not bought the book first
        plusButton.setDisable(true);
        plusButton.setVisible(false);

        minusButton.setDisable(true);
        minusButton.setVisible(false);

        numberItemsInCart = 0; // initialize

        // Book Fields
        bookNameField.setText(book.name);
        authorField.setText("Author/s : " + book.author);
        priceField.setText("Price : "+book.price);
        stockLabel.setText(book.quantity + " copies available");
        descriptionField.setText(book.description);
        genreField.setText("Genre : " +book.getGenresOfBook());
        isbnField.setText("ISBN : " + book.ISBN);

        // Book Image

        bookImage.setImage(book.image);
        bookImage.setFitHeight(350);

        if(Main.userInfo !=null)
        {
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
                if(Database.alreadyInCart(Main.email, book.ISBN)) {
                    book.willingToPurchaseQuantity = Database.getQuantityFromCart(Main.email, book.ISBN);
                    alreadyInCartInit();

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            numberItemsInCart = Main.tempCart.size();

            // Check Cart IF NOT LOGGED IN
            for(int i =0; i< Main.tempCart.size(); i++) {
                if(Main.tempCart.get(i).ISBN.equals(book.ISBN)) {
                    book.willingToPurchaseQuantity = Main.tempCart.get(i).quantity;
                    alreadyInCartInit();
                    // INDEX IS INITIALIZED SO WE DONT SEARCH REPEATEDLY
                    indx = i;
                }
            }
        }

        numberOfItemsLabel.setText(String.valueOf(numberItemsInCart));


    }
    @FXML
    void minusButtonClicked(ActionEvent ignoredEvent) throws SQLException {
        book.willingToPurchaseQuantity = book.willingToPurchaseQuantity - 1;

        if(book.willingToPurchaseQuantity == 1) // If quantity is down to 1 we disable the button
            minusButton.setDisable(true);

        quantityLabel.setText(String.valueOf(book.willingToPurchaseQuantity));

        if(Main.email != null)
            Database.updateCart(Main.email, book.ISBN, book.willingToPurchaseQuantity);
        else
            Main.tempCart.get(indx).quantity--;
    }
    @FXML
    void plusButtonClicked(ActionEvent event) throws SQLException {

        if(book.willingToPurchaseQuantity == 1) // if previously was 1 we enable the minus button
            minusButton.setDisable(false);

        // Increase Quantity
        book.willingToPurchaseQuantity++;

        quantityLabel.setText(String.valueOf(book.willingToPurchaseQuantity));

        // Store Info in cart Cart
        if(Main.email != null)
            Database.updateCart(Main.email, book.ISBN, book.willingToPurchaseQuantity);
        else
            Main.tempCart.get(indx).quantity++;
    }
    void alreadyInCartInit() {

        quantityLabel.setText(String.valueOf(book.willingToPurchaseQuantity));

        addCartButton.setDisable(true);
        addCartButton.setText("Added to Cart");

        plusButton.setDisable(false);
        plusButton.setVisible(true);

        minusButton.setVisible(true);

        if(book.willingToPurchaseQuantity > 1) {
            minusButton.setDisable(false);
        }
    }

    @FXML
    void onAddtoCart(ActionEvent event) throws SQLException {
        if(Main.email != null)
        {
            Database.addToCart(Main.email, book.ISBN, 1);
        }
        else
        {
            indx = Main.tempCart.size();
            CartItem t = new CartItem(book.ISBN, 1);
            Main.tempCart.add(t);
        }
        book.willingToPurchaseQuantity = 1;
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
    void goBack(ActionEvent event) throws IOException {
        SceneChanger.changeTo("UserDashboard.fxml",event);
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
        System.out.println("search");
    }

    @FXML
    void reviewButtonClicked(ActionEvent event) {
        System.out.println("Review");
    }

    public static BookInfo book;

    private int indx = -1;

    private int numberItemsInCart;
    @FXML
    private Button addCartButton;

    @FXML
    private TextField authorField;

    @FXML
    private Text bookNameField;

    @FXML
    private HBox cartIcon;

    @FXML
    private Text genreField;

    @FXML
    private TextField isbnField;

    @FXML
    private Button minusButton;

    @FXML
    private Label numberOfItemsLabel;

    @FXML
    private Button plusButton;

    @FXML
    private TextField priceField;

    @FXML
    private ImageView profileImage;

    @FXML
    public ImageView bookImage;
    @FXML
    private Label quantityLabel;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchButton;

    @FXML
    private TextField stockLabel;
    @FXML
    private TextArea descriptionField;

    public static String isbn;
}
