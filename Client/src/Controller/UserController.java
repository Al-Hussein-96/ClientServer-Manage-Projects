package Controller;

import CommonClass.Profile;
import CommonClass.User;
import CommonCommand.Command;
import CommonCommand.GetProfile;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import CommonRespone.SendProfile;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import client.TabelUsers;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class UserController implements Initializable {

    @FXML
    private TableView<TabelUsers> tabelview;

    @FXML
    private TableColumn<TabelUsers, String> c1;

    User MyUser;
    List<User> ListUser;
    AnchorPane roopane;

    public UserController(List<User> ListUser, User MyUser, AnchorPane roopane) {
        this.ListUser = ListUser;
        this.MyUser = MyUser;
        this.roopane = roopane;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /// here must fill tabel with ListUser
        /// too must create new Class with Name TabelUser
        ObservableList<TabelUsers> list;
        TabelUsers[] st = new TabelUsers[ListUser.size()];
        int idx = 0;
        for (User temp : ListUser) {
            st[idx++] = new TabelUsers(temp.getName());
        }
        list = FXCollections.observableArrayList(st);
        c1.setCellValueFactory(new PropertyValueFactory<TabelUsers, String>("UserName"));
        tabelview.setItems(list);

        tabelview.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                doubleClick_Open();
            }
        });
    }

    @FXML
    void btnOpen(ActionEvent event) {
        doubleClick_Open();
    }

    void doubleClick_Open() {
        TabelUsers TB = tabelview.getSelectionModel().getSelectedItem();
        if (TB == null) {
            return;
        }
        Command command = new GetProfile(TB.getUserName());
        try {
            networkOutput.writeObject(command);
            networkOutput.flush();
            Respone respone = (Respone) networkInput.readObject();
            Profile InfoProfile = ((SendProfile) respone).getProfile();
            if (respone.TypeRespone == ResponeType.DONE) {
                ProfileController profileController = new ProfileController();
                profileController.setRoopane(roopane);
                profileController.setProfile(InfoProfile);
                profileController.setUser(MyUser);
                FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/FXML/Profile.fxml"));
                fXMLLoader.setController(profileController);
                AnchorPane pane = (AnchorPane) fXMLLoader.load();
                roopane.getChildren().setAll(pane);
            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
