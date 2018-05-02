package Controller;

import CommonClass.Profile;
import CommonClass.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ProfileController implements Initializable {

    private Profile profile;
    private AnchorPane roopane;

    @FXML
    private Text UserName;

    @FXML
    private Text email;

    @FXML
    void btnProject(ActionEvent event) {
      
    }

    @FXML
    void btnContributorProject(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /// here must initlize Text Above By profile
    }

    public void setRoopane(AnchorPane roopane) {
        this.roopane = roopane;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

}
