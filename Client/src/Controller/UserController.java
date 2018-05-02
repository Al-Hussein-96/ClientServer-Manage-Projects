package Controller;

import CommonClass.User;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class UserController implements Initializable {

    List<User> ListUser;
    AnchorPane roopane;

    public UserController(List<User> ListUser, AnchorPane roopane) {
        this.ListUser = ListUser;
        this.roopane = roopane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        /// here must fill tabel with ListUser
        /// too must create new Class with Name TabelUser
    }

}
