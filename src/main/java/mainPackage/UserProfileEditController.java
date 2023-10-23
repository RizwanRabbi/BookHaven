package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileEditController implements Initializable {
    int imageChosen;
    Image defaultImage;
    public static UserInfo accountInfo;

    @FXML
    private TextArea addressField;

    @FXML
    private TextArea emailField;

    @FXML
    private TextArea nameField;

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
    }

    @FXML
    void onSaveButtonClick(ActionEvent event) {
        String editedName = nameField.getText();
        String editedPhone = phoneField.getText();
        String editedEmail = emailField.getText();
        String editedAddress = addressField.getText();

//        System.out.println(editedName + editedPhone + editedEmail + editedAddress);

    }

    @FXML
    void onUploadButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File (.jpg or .png)");

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", ".jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Show open file Dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if(selectedFile != null)
        {
            Image image = new Image(selectedFile.toURI().toString());
            imageChosen = 1;
            profilePicture.setImage(image);
            removeButton.setDisable(false);
            System.out.println("Selected File: " + selectedFile.getAbsolutePath());
        }
        else {
            imageChosen = 0;
            System.out.println("No file selected!");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Default image Should come from database
        defaultImage = profilePicture.getImage();
        imageChosen = 0;

    }
}
