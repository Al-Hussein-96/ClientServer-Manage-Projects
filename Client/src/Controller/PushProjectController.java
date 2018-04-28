package Controller;

import CommonClass.NameAndDirectory;
import CommonClass.ProjectToUpload;
import CommonClass.ResourceManager;
import static CommonClass.ResourceManager.load;
import CommonClass.ViewfolderClass;
import CommonCommand.Command;
import CommonCommand.GetFile;
import CommonCommand.GetPush;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import CommonRespone.SendFile;
import CommonRespone.SendProject;
import static client.Project.networkInput;
import static client.Project.networkOutput;
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
import javafx.stage.DirectoryChooser;

public class PushProjectController implements Initializable {

    String NameProject;

    @FXML
    private JFXTextField Comment;

    @FXML
    private JFXTextField Path;

    File selectedFile;

    @FXML
    void btnBrowsers(ActionEvent event) {

        DirectoryChooser dc = new DirectoryChooser();
        selectedFile = dc.showDialog(null);
        if (selectedFile != null) {
            Path.setText(selectedFile.getPath());
        }
    }

    @FXML
    void btnPush(ActionEvent event) {
        if (Path == null) {
            return;
        }
        ProjectToUpload hiddenFile = null;
        for (File file : selectedFile.listFiles()) {
            if (file.isFile() && "BEHKN.BEHKN".equals(file.getName())) {
                try {
                    hiddenFile = (ProjectToUpload) load(file.getPath());
                    file.delete();
                    break;
                } catch (Exception ex) {

                }
            }
        }
        if (hiddenFile == null) {
            return;
        }

        Command command = new GetPush(NameProject, hiddenFile, selectedFile.getName(), Comment.getText());
//        System.out.println("");
        try {
            networkOutput.writeObject(command);
            networkOutput.flush();
        } catch (IOException ex) {
            Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Respone respone = null;
        try {
            respone = (Respone) networkInput.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("ERROR in GETPUSH: " + ex.getMessage());
        }
        System.out.println("Respone For Push: " + respone.TypeRespone);

        if (respone.TypeRespone == ResponeType.DONE) {
            ViewfolderClass ob = ResourceManager.ViewProject(new File(selectedFile.getPath()));
            ResourceManager.ShowViewfolder(ob);
            Respone newRespone = new SendProject(ob);
            try {
                networkOutput.writeObject(newRespone);
                networkOutput.flush();
            } catch (IOException ex) {
                Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
            }
            SendFolder(ob);

            try {
                System.out.println("Path HiddenFile: " + Path.getText());
                ProjectToUpload BenkhFile = (ProjectToUpload) networkInput.readObject();
                System.out.println("Path HiddenFile: " + Path.getText());
                ResourceManager.save(BenkhFile, Path.getText() + "\\" + "BEHKN.BEHKN");
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(PushProjectController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PushProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

        }
    }

    private void SendFolder(ViewfolderClass ob) {
        for (NameAndDirectory temp : ob.MyFile) {
            GetFile get = new GetFile(temp.Directory);
            System.out.println("SendFolder: " + temp.Directory);
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
            NameAndDirectory My = new NameAndDirectory(file.getName(), dir);
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

    public void setNameProject(String NameProject) {
        this.NameProject = NameProject;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
