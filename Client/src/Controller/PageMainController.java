package Controller;

import CommonClass.Profile;
import CommonClass.User;
import CommonCommand.Command;
import CommonCommand.GetListUser;
import CommonCommand.GetProfile;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import CommonRespone.SendListUser;
import CommonRespone.SendProfile;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    User Owner;

    @FXML
    private AnchorPane roopane;

    @FXML
    void brnCreateProject(ActionEvent event) throws IOException {
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/FXML/CreateProject.fxml"));
        AnchorPane pane = fXMLLoader.load();//FXMLLoader.load(getClass().getResource("/FXML/CreateProject.fxml"));
        roopane.getChildren().setAll(pane);
    }

    @FXML
    void btnmyproject(ActionEvent event) {
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/FXML/MyProject.fxml"));
            MyProjectController myProjectController = new MyProjectController(roopane, Owner);
            fXMLLoader.setController(myProjectController);
            AnchorPane pane = (AnchorPane) fXMLLoader.load();
            roopane.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage() + "End Message");
        }
    }

    @FXML
    private void btnallproject(ActionEvent event) throws IOException {
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/FXML/AllProject.fxml"));
            AnchorPane pane = (AnchorPane) fXMLLoader.load();
            AllProjectController allProjectController = fXMLLoader.getController();
            allProjectController.setRoopane(roopane);
            allProjectController.setUser(Owner);
            roopane.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage() + "End Message");
        }
    }

    @FXML
    private void btnProfile(ActionEvent event) {
        goToProfile();
    }

    public void goToProfile() {
        try {
            /// this for Display MyProfile Only without Edit
            Command command = new GetProfile(Owner.getName());
            networkOutput.writeObject(command);
            networkOutput.flush();
            Respone respone = (Respone) networkInput.readObject();
            Profile InfoProfile = ((SendProfile) respone).getProfile();
            if (respone.TypeRespone == ResponeType.DONE) {
                ProfileController profileController = new ProfileController();
                profileController.setRoopane(roopane);
                profileController.setProfile(InfoProfile);
                profileController.setUser(Owner);
                FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/FXML/Profile.fxml"));
                fXMLLoader.setController(profileController);
                AnchorPane pane = (AnchorPane) fXMLLoader.load();
                roopane.getChildren().setAll(pane);
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage() + "End Message");
        }
    }

    @FXML
    void btnUsers(ActionEvent event) {
        Command command = new GetListUser();

        try {
            networkOutput.writeObject(command);
            networkOutput.flush();

            SendListUser respone = (SendListUser) networkInput.readObject();

            if (respone.TypeRespone == ResponeType.DONE) {

                List<User> ListUser = new ArrayList<>(respone.getListUser());
                UserController userController = new UserController(ListUser, Owner, roopane);

                FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/FXML/Users.fxml"));
                fXMLLoader.setController(userController);

                AnchorPane pane = (AnchorPane) fXMLLoader.load();

                roopane.getChildren().setAll(pane);

            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(PageMainController.class.getName()).log(Level.SEVERE, null, ex);
        }

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
        File F = new File("Data\\temp");
        F.delete();
    }

    public void setOwner(User Owner) {
        this.Owner = Owner;
        goToProfile();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void Close(ActionEvent event) {
        Platform.exit();
    }

}
