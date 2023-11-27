package mainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ReviewThumbController implements Initializable {

    public static String returnTo;
    public static BookInfo bookInfo;
    @FXML
    private Button saveButton;
    @FXML
    private Text bookname;

    @FXML
    private Rating rating;

    @FXML
    private TextArea reviewText;

    @FXML
    void onGobackClick(ActionEvent event) throws IOException {
        SceneChanger.changeTo(returnTo, event);
    }

    @FXML
    void onPostReviewClick(ActionEvent event) throws SQLException {
        if(Database.hasReviewed(bookInfo))
            Database.updateReview(reviewText.getText(),rating.getRating(),bookInfo);
        else
            Database.addReview(reviewText.getText(),rating.getRating(),bookInfo);
        saveButton.setDisable(true);
    }

    @FXML
    void onRatingClicked(MouseEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reviewText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(newValue, oldValue)) {
                saveButton.setDisable(false);
            }
        });
        saveButton.setDisable(true);
        bookname.setText(bookInfo.name);
        try {
            if(Database.hasReviewed(bookInfo))
            {
                Pair<String, Double> p = Database.getReview(bookInfo);
                reviewText.setText(p.getKey());
                rating.setRating(p.getValue());
            }
            else
            {
                reviewText.setText("");
                rating.setRating(0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
