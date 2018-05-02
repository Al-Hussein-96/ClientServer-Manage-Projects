package Controller;

import CommonClass.Profile;
import CommonClass.User;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
    private Text DateCreate;

    @FXML
    private Text email;

    @FXML
    void btnContributorProject(ActionEvent event) {
        //// here we have List Of Proeject Contributors in profile we send it to new fxml and display it

    }

    @FXML
    void btnProject(ActionEvent event) {
        //// here we have List Of Proeject Own in profile we send it to new fxml and display it

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /// here must initlize Text Above By profile
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        UserName.setText(profile.getUser().getName());
        DateCreate.setText(ft.format(profile.getUser().getDateCreate()));
        email.setText(profile.getUser().getEmail());

    }

    public void setRoopane(AnchorPane roopane) {
        this.roopane = roopane;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

}
