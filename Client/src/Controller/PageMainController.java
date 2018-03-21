package Controller;

import client.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class PageMainController implements Initializable {

    static User Owner;

    @FXML
    private AnchorPane roopane;

    @FXML
    void btnmyproject(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/MyProject.fxml"));
        roopane.getChildren().setAll(pane);
    }

    public void setOwner(User Owner) {
        this.Owner = Owner;
    }

    @FXML
    void brnCreateProject(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/CreateProject.fxml"));
        roopane.getChildren().setAll(pane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
