package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {

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
        SceneChanger.changeTo("ForgotPasswordMenu.fxml", event);
    }

    @FXML
    void onEditButtonClick(ActionEvent event) throws IOException {
        UserProfileEditController.accountInfo = currentUser;
        SceneChanger.changeTo("UserProfileEdit.fxml", event);
    }

    @FXML
    void onGoBackButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo(returnTo, event);
    }

    @FXML
    void onOrderButtonClick(ActionEvent event) {
        //Views previous orders
    }

    @FXML
    void onRemoveButtonClick(ActionEvent event) {

    }

    @FXML
    void onEditPictureButtonClick(ActionEvent event) throws IOException {
        UserProfileEditController.accountInfo = currentUser;
        SceneChanger.changeTo("UserProfileEdit.fxml", event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUser = new UserInfo();
        try {
            currentUser = Database.getUserInfo(Main.email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        emailField.setText("Email: "+Main.email);
//        phoneField.setText("Phone No: "+currentUser.phoneNo);
        nameField.setText("Name: "+currentUser.fname + " " + currentUser.lname);

//        addressField.setText("Address : " + currentUser.address);
    }
}
