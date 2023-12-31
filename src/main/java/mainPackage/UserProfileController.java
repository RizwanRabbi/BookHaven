package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {
    public static Image defaultImage;
    public static UserInfo currentUser;
    public static String returnTo;
    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private ImageView profilePicture;

    @FXML
    private TextArea addressField;

    @FXML
    void onChangePwordButtonClick(ActionEvent event) throws IOException {
        ForgotPasswordController.returnTo = "UserProfile.fxml";
        SceneChanger.changeTo("ForgotPasswordMenu.fxml", event);
    }

    @FXML
    void onEditButtonClick(ActionEvent event) throws IOException {
//        UserProfileEditController.accountInfo = currentUser;
        SceneChanger.changeTo("UserProfileEdit.fxml", event);
    }

    @FXML
    void onGoBackButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo(returnTo, event);
    }

    @FXML
    void onOrderButtonClick(ActionEvent event) throws IOException {
        //Views previous orders
        SceneChanger.changeTo("ViewPreviousOrdersList.fxml", event);
    }


    @FXML
    void onEditPictureButtonClick(ActionEvent event) throws IOException {
//        UserProfileEditController.accountInfo = currentUser;
        SceneChanger.changeTo("UserProfileEdit.fxml", event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        defaultImage = profilePicture.getImage();
        currentUser = Main.userInfo;
        emailField.setText("Email: "+Main.email);
        phoneField.setText("Phone No: "+currentUser.phoneNo);
        nameField.setText("Name: "+currentUser.fname + " " + currentUser.lname);
        if(currentUser.image !=null) {
            Circle clip = new Circle(100,100,100);
            profilePicture.setFitHeight(200);
            profilePicture.setFitWidth(200);
            profilePicture.setClip(clip);
            profilePicture.setImage(currentUser.image);
        }
        else
            profilePicture.setImage(defaultImage);
        addressField.setText("Address : " + currentUser.address);
    }
}
