package Controller;

import static Controller.PageMainController.Owner;
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
        String response;
        try {
            networkOutput.writeUTF("STARTPROJECT");
            String path = txtLocation.getText();
            networkOutput.writeUTF(txtNameProject.getText());
           
            networkOutput.writeUTF("true");
            
            response = networkInput.readUTF();
            if (response.equals("Done")) {
              int IdProject = networkInput.readInt();
              String Author = networkInput.readUTF();
              int lastCommit = 1;
              List<String> Contributors = new ArrayList<>();
              Contributors.add(Author);
              ProjectToUpload Temp = new ProjectToUpload(path +"\\.BENKH",1,IdProject , Contributors , "Master" );
              Temp.Save();
            }
            System.out.println("\nServer : " + response);
            
        } catch (IOException ex) {
            System.out.println("Error START PROJECT");
        }
    }
    
    @FXML
    void Close(ActionEvent event)
    {
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("HELLLOOOOOOOO");
        System.out.println(Owner);
    }

}
