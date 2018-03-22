package Controller;

import static Controller.PageMainController.Owner;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
            networkOutput.println("STARTPROJECT");
            response = networkInput.readLine();

            if (response.equals("Done")) {
                String path = txtLocation.getText();
                networkOutput.println(txtNameProject.getText());
                /// should not send bath   
                /// should save it in client folders
               /// networkOutput.println(path);
                networkOutput.println("true");
            }
            System.out.println("\nServer : " + response);

        } catch (IOException ex) {
            System.out.println("Error START PROJECT");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("HELLLOOOOOOOO");
        System.out.println(Owner);
    }

}
