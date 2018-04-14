package Controller;

import CommonClass.ProjectToUpload;
import CommonCommand.Command;
import CommonCommand.GetStartProject;
import CommonRespone.Respone;
import CommonRespone.SendCreateProject;
import CommonRespone.ResponeType;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.DirectoryChooser;

public class CreateProjectController implements Initializable {

    @FXML
    private JFXTextField txtNameProject;

    @FXML
    private JFXTextField txtLocation;

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
            Command command = new GetStartProject(txtNameProject.getText(), "true");
            networkOutput.writeObject(command);
            networkOutput.flush();

            Respone response = (Respone) networkInput.readObject();

            if (response.TypeRespone == ResponeType.DONE) {
                Respone respone1 = (Respone) networkInput.readObject();

                int IdProject = ((SendCreateProject) respone1).IdProject;
                String Author = ((SendCreateProject) respone1).Author;
                List<String> Contributors = new ArrayList<>();
                Contributors.add(Author);

                ProjectToUpload hiddenFile = new ProjectToUpload(path + "\\.BENKH", 1, IdProject, Contributors, "Master");
                hiddenFile.Save();
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error START PROJECT");
        }
    }

    @FXML
    void Close(ActionEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
