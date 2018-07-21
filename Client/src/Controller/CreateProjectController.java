package Controller;

import CommonClass.NameAndDirectory;
import CommonClass.ProjectToUpload;
import CommonClass.ResourceManager;
import CommonClass.ViewfolderClass;
import CommonCommand.Command;
import CommonCommand.GetFile;
import CommonCommand.GetStartProject;
import CommonRespone.Respone;
import CommonRespone.SendCreateProject;
import CommonRespone.ResponeType;
import CommonRespone.SendFile;
import CommonRespone.SendProject;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class CreateProjectController implements Initializable {

    @FXML
    private JFXTextField txtNameProject;

    @FXML
    private JFXTextField txtLocation;
    @FXML
    private JFXCheckBox PrivateProject;
    @FXML
    private JFXButton btnCreate;

    File selectedFile;

    @FXML
    void btnBrowsers(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            txtLocation.setText(selectedFile.getPath());
        }
    }

    @FXML
    void btnCreate(ActionEvent event) {
        try {
            String path = txtLocation.getText();
            String Access = PrivateProject.isSelected() ? "false" : "true";
            Command command = new GetStartProject(txtNameProject.getText(), Access, selectedFile.getName());
            networkOutput.writeObject(command);
            networkOutput.flush();
            Respone response = (Respone) networkInput.readObject();
            if (response.TypeRespone == ResponeType.DONE) {

                ViewfolderClass ob = ResourceManager.ViewProject(new File(path));
                ResourceManager.ShowViewfolder(ob);
                Respone newRespone = new SendProject(ob);
                try {
                    networkOutput.writeObject(newRespone);
                    networkOutput.flush();
                } catch (IOException ex) {
                    Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
                }
                SendFolder(ob);

                Respone respone1 = (Respone) networkInput.readObject();
                ProjectToUpload hiddenFile = ((SendCreateProject) respone1).getBenkhFile();

                /// save File in directory of Project
                ResourceManager.save(hiddenFile, path + "\\" + "BEHKN.BEHKN");

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

    private void SendFolder(ViewfolderClass ob) {
        for (NameAndDirectory temp : ob.MyFile) {
            GetFile get = new GetFile(temp.Directory);
            GETFILE(get);
        }
        for (ViewfolderClass temp : ob.MyFolderView) {
            SendFolder(temp);
        }
    }

    private void GETFILE(Command command) {
        FileInputStream fis = null;
        try {
            byte[] DataFile = new byte[4096];
            String dir = ((GetFile) command).getDirectoryFile();
            File file = new File(dir);
            NameAndDirectory My = new NameAndDirectory(file.getName(), file.length(), file.lastModified(), dir);
            fis = new FileInputStream(file);
            long fileSize = file.length();
            int n;
            if (fileSize == 0) {
                Respone respone = new SendFile(DataFile, fileSize == 0, My, 0);
                networkOutput.writeObject(respone);
                networkOutput.flush();
            }
            while (fileSize > 0 && (n = fis.read(DataFile, 0, (int) Math.min(4096, fileSize))) != -1) {
                long tmp = fileSize;
                fileSize -= n;
                Respone respone = new SendFile(DataFile, fileSize == 0, My, tmp);
                networkOutput.writeObject(respone);
                networkOutput.flush();
            }

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
            }
        }

    }

}
