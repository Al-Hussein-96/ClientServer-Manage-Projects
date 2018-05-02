package Controller;

import CommonClass.CommonProject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;


public class OwnProjectUserController implements Initializable{
    
    List<CommonProject> Project;

    public OwnProjectUserController(List<CommonProject> Project) {
        this.Project = new ArrayList<>(Project);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
}
