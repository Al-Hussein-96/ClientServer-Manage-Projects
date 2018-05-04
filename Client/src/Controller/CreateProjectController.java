package Controller;

import CommonClass.ProjectToUpload;
import CommonClass.ResourceManager;
import CommonCommand.Command;
import CommonCommand.GetStartProject;
import CommonRespone.Respone;
import CommonRespone.SendCreateProject;
import CommonRespone.ResponeType;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class CreateProjectController implements Initializable {

    @FXML
    private JFXTextField txtNameProject;

    @FXML
    private JFXTextField txtLocation;
    @FXML
    private JFXCheckBox PrivateProject;
    @FXML
    private JFXButton btnCreate;

    @FXML
    void btnBrowsers(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            txtLocation.setText(selectedFile.getPath());
        }
    }

    @FXML
    void btnCreate(ActionEvent event) {
        try {
            String path = txtLocation.getText();
            String Access = PrivateProject.isSelected() ? "false" : "true";
            Command command = new GetStartProject(txtNameProject.getText(), Access);
            networkOutput.writeObject(command);
            networkOutput.flush();
            Respone response = (Respone) networkInput.readObject();
            if (response.TypeRespone == ResponeType.DONE) {

                Respone respone1 = (Respone) networkInput.readObject();
                ProjectToUpload hiddenFile = ((SendCreateProject) respone1).getBenkhFile();

                /// save File in directory of Project
                ResourceManager.save(hiddenFile, path + "\\" + "BEHKN.BEHKN");
                System.out.println("Done Create Project.");

                /// 
                Notifications notification = Notifications.create()
                        .title("Create Project")
                        .text("Done Create Project")
                        .graphic(null)
                        .hideAfter(Duration.seconds(2))
                        .position(Pos.CENTER);
                notification.showConfirm();
            } else {
                Notifications notification = Notifications.create()
                        .title("Create Project")
                        .text("Can't Create Project")
                        .graphic(null)
                        .hideAfter(Duration.seconds(2))
                        .position(Pos.CENTER);
                notification.showError();

            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error START PROJECT: " + ex.getMessage());
        } catch (Exception ex) {
        }

    }

    void Close(ActionEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
