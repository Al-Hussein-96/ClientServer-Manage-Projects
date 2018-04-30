package Controller;

import CommonClass.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class UserInterfaceController implements Initializable {

    User user;
    private AnchorPane roopane;
    @FXML
    private Label NameUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setRoopane(AnchorPane roopane) {
        this.roopane = roopane;
    }

    public void setUser(User user) {
        this.user = user;
        NameUser.setText(user.getName());
    }

}
