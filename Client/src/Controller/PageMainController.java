package Controller;

import client.User;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
    void brnCreateProject(ActionEvent event) throws IOException {
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/FXML/CreateProject.fxml"));
        AnchorPane pane = fXMLLoader.load();//FXMLLoader.load(getClass().getResource("/FXML/CreateProject.fxml"));

        roopane.getChildren().setAll(pane);
    }

    @FXML
    void btnmyproject(ActionEvent event) throws IOException {

        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/FXML/MyProject.fxml"));

        AnchorPane pane = fXMLLoader.load();

//        System.out.println("pane: " + pane);

        MyProjectController myProjectController = fXMLLoader.getController();
        myProjectController.setRoopane(roopane);

        roopane.getChildren().setAll(pane);
    }

    @FXML
    private void btnallproject(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/AllProject.fxml"));
        roopane.getChildren().setAll(pane);
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
        File F = new File("Data\\temp.txt");
        F.delete();
    }

    public void setOwner(User Owner) {
        this.Owner = Owner;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void Close(ActionEvent event) {
        Platform.exit();
    }

}
