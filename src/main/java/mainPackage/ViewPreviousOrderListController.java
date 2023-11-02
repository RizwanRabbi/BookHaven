package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewPreviousOrderListController implements Initializable {

    ArrayList<OrderInfo> orders;
    @FXML
    private Text userNameLabel;
    @FXML
    private GridPane gridPane;

    @FXML
    private HBox header;

    @FXML
    private ImageView profileImage;

    @FXML
    private ScrollPane scrollPane;

//    @FXML
//    private TextField searchBar;
//
//    @FXML
//    private Button searchButton;

    @FXML
    private VBox vb;

    @FXML
    void goDashboard(MouseEvent event) throws IOException {
        SceneChanger.changeTo("UserDashboard.fxml", vb);
    }

    @FXML
    void onGobackButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo("UserProfile.fxml", vb);
    }

    @FXML
    void onProfileImageClick(MouseEvent event) throws IOException {
        SceneChanger.changeTo("UserProfile.fxml", vb);
    }

    @FXML
    void onSearchButtonClick(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        orders = Database.getOrdersByUserEmail(Main.email);
        try {
            UserInfo u = Database.getUserInfo(Main.email);
            Circle clip = new Circle(22,22,22);
            profileImage.setFitHeight(44);
            profileImage.setFitWidth(44);
            profileImage.setClip(clip);
            if( u.image != null )
                profileImage.setImage(u.image);

            String p = "Previous Orders";
            String name = new String();
            if(u.fname != null)
                name = u.fname;
            if(u.lname != null)
                name = name + " "+ u.lname;
            if(u.fname != null && u.lname != null )
                p = name + "'s " +p;

            userNameLabel.setText(p);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(orders.size() == 0)
        {

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("OrderThumb.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            OrderThumbController c = loader.getController();
            c.orderInfo = new OrderInfo();
            c.notFoundInitialize();
            int x = 3 , y = 3;
            gridPane.add(root, x, y);
        }
        else
        {
            System.out.println("not zero");
            for (int i = 0; i < orders.size(); i++)
            {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("OrderThumb.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Index "+ (i+1));
                orders.get(i).print();
                OrderThumbController c = loader.getController();
                c.orderInfo = orders.get(i);
                c.init();
                int x = 3 , y = 2*i+3;
                gridPane.add(root, x, y);
            }
        }
    }
}