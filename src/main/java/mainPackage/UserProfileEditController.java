package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

public class UserProfileEditController implements Initializable {

    public static File selectedFile;
    int imageChosen;
    Image defaultImage;
    @FXML
    private TextArea addressField;

    @FXML
    private TextArea fnameField;

    @FXML
    private TextArea lnameField;

    @FXML
    private TextArea phoneField;

    @FXML
    private ImageView profilePicture;

    @FXML
    private Button removeButton;

    @FXML
    void onGoBackButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo("UserProfile.fxml", event);
    }

    @FXML
    void onRemoveButtonClick(ActionEvent event) {
        profilePicture.setImage(defaultImage);
        removeButton.setDisable(true);
        imageChosen = 0;
        selectedFile = null;
    }

    @FXML
    void onSaveButtonClick(ActionEvent event) throws SQLException, IOException {
        UserInfo u = new UserInfo();
        u.fname = fnameField.getText();
        u.lname = lnameField.getText();
        u.phoneNo = phoneField.getText();
        u.address = addressField.getText();
        if(imageChosen == 0)
            u.image = null;
        else
            u.image = profilePicture.getImage();
        if( imageChosen == -1 )
            Database.updateUserInfo(u);
        else
            Database.updateUserInfo(u, selectedFile);

        SceneChanger.changeTo("UserProfile.fxml", event);
    }

    @FXML
    void onUploadButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File (.jpg or .png)");

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Show open file Dialog
        selectedFile = fileChooser.showOpenDialog(new Stage());

        if(selectedFile != null)
        {
            Image image = new Image(selectedFile.toURI().toString());
            imageChosen = 1;
            Circle clip = new Circle(100,100,100);
            profilePicture.setClip(clip);
            profilePicture.setFitHeight(200);
            profilePicture.setFitWidth(200);
            profilePicture.setClip(clip);
            profilePicture.setImage(image);
            removeButton.setDisable(false);
            System.out.println("Selected File: " + selectedFile.getAbsolutePath());
        }
        else {
            imageChosen = -1;
            System.out.println("No file selected!");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // If the new input doesn't match the pattern (non-digit), set the text to the previous value
                phoneField.setText(oldValue);
            }
        });
        fnameField.setText(Main.userInfo.fname);
        lnameField.setText(Main.userInfo.lname);
        addressField.setText(Main.userInfo.address);
        phoneField.setText(Main.userInfo.phoneNo);
        defaultImage = profilePicture.getImage();
        if(Main.userInfo.image != null)
        {
            Circle clip = new Circle(100,100,100);
            profilePicture.setFitHeight(200);
            profilePicture.setFitWidth(200);
            profilePicture.setClip(clip);
            profilePicture.setImage(Main.userInfo.image);
            imageChosen = 1;
        }
        else
        {
            imageChosen = 0;
            removeButton.setDisable(true);
        }
    }
}
