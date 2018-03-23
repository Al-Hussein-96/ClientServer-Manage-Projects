package Controller;

import client.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    @FXML
    private void SignOut(MouseEvent event) {
        try {
            roopane.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/WindowsSelect.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(WindowsSelectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
