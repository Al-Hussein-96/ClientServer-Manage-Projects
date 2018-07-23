package Controller;

import CommonClass.ProjectToUpload;
import CommonClass.ResourceManager;
import CommonCommand.Command;
import CommonCommand.GetPull;
import CommonRespone.SendProject;
import static client.Project.Args;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class PullProjectController implements Initializable {

    String NameProject;
    int Commit;
    String Branch;
    FileBrowsersController Father;

    File selectedFile = null;
    @FXML
    private JFXTextField Path;

    public void setAll(FileBrowsersController Father, String NameProject, int Commit, String Branch) {
        this.Father = Father;
        this.NameProject = NameProject;
        this.Commit = Commit;
        this.Branch = Branch;
    }

    @FXML
    void btnBrowsers(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        selectedFile = dc.showDialog(null);
        if (selectedFile != null) {
            Path.setText(selectedFile.getPath());
            Args = Path.getText();
        }
    }

    @FXML
    void btnPull(ActionEvent event) {

        if (Path == null) {
            return;
        }
        Args = Path.getText();
        selectedFile = new File(Path.getText());
        // String Commit = idCommit.getText().substring(9), Branch = idBranch.getText().substring(9);
        /// "Master" is temp for try and 1 is temp
        Command command = new GetPull(NameProject, Commit, Branch);
        try {
            networkOutput.writeObject(command);
            networkOutput.flush();
            SendProject respone = (SendProject) networkInput.readObject();
            Father.openProgressBar();

            Father.CreateFolder(respone.ob, selectedFile.getPath() + "\\");
            Father.Receive(respone.ob, selectedFile.getPath() + "\\");

            ProjectToUpload hiddenFile = (ProjectToUpload) networkInput.readObject();
            try {
                /// save File in directory of Project
                ResourceManager.save(hiddenFile, selectedFile.getPath() + "\\" + "BEHKN.BEHKN");
            } catch (Exception ex) {
                Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
            }
            /**
             * **
             */
            Notifications notification = Notifications.create()
                    .title("Pull Project")
                    .text("Done Pull Project")
                    .graphic(null)
                    .hideAfter(Duration.seconds(2))
                    .position(Pos.CENTER);
            notification.showConfirm();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error in btnPull : " + ex.getMessage());
            Notifications notification = Notifications.create()
                    .title("Pull Project")
                    .text("Can't Pull Project")
                    .graphic(null)
                    .hideAfter(Duration.seconds(2))
                    .position(Pos.CENTER);
            notification.showError();

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (!"".equals(Args)) {
            Path.setText(Args);
        }
    }

}
