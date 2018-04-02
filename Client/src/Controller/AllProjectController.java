package Controller;

import CommonClass.CommonProject;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import CommonClass.ResourceManager;
import CommonCommand.AllProject;
import CommonRespone.Respone;
import CommonRespone.SendAllProject;
import CommonRespone.ResponeType;
import client.TabelProject;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public class AllProjectController implements Initializable {

    @FXML
    private JFXTreeTableView<TabelProject> TabelView;
    private List< CommonProject> AllProject;

    @FXML
    void btnOpen(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AllProject = GetAllProject();

        JFXTreeTableColumn<TabelProject, String> nameProject = new JFXTreeTableColumn<>("Name");
        nameProject.setPrefWidth(150);
        nameProject.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabelProject, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabelProject, String> param) {
                return param.getValue().getValue().NameProject;
            }
        });

        JFXTreeTableColumn<TabelProject, String> dataCreate = new JFXTreeTableColumn<>("Date");
        dataCreate.setPrefWidth(130);
        dataCreate.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabelProject, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabelProject, String> param) {
                return param.getValue().getValue().DateCreate;
            }
        });

        JFXTreeTableColumn<TabelProject, String> author = new JFXTreeTableColumn<>("Author");
        author.setPrefWidth(140);
        author.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabelProject, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabelProject, String> param) {
                return param.getValue().getValue().Author;
            }
        });

        JFXTreeTableColumn<TabelProject, String> numberOfContributors = new JFXTreeTableColumn<>("Contributors");
        numberOfContributors.setPrefWidth(140);
        numberOfContributors.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabelProject, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabelProject, String> param) {
                return param.getValue().getValue().NumberOfContributors;
            }
        });

        JFXTreeTableColumn<TabelProject, String> numberOfCommits = new JFXTreeTableColumn<>("Commits");
        numberOfCommits.setPrefWidth(130);
        numberOfCommits.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabelProject, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabelProject, String> param) {
                return param.getValue().getValue().NumberOfCommits;
            }
        });
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");

        ObservableList<TabelProject> users = FXCollections.observableArrayList();
        for (int i = 0; i < AllProject.size(); i++) {
            CommonProject CP = AllProject.get(i);
            TabelProject TP = new TabelProject(CP.NameProject, ft.format(CP.DateCreate),
                    CP.Author, String.valueOf(CP.Contributors.size()), String.valueOf(CP.way.size()));
            users.add(TP);
        }
//        users.add(new TabelProject("IT", "2018", "Moaz", "5", "23"));

        final TreeItem<TabelProject> root = new RecursiveTreeItem<>(users, RecursiveTreeObject::getChildren);
        TabelView.getColumns().setAll(nameProject, dataCreate, author, numberOfContributors, numberOfCommits);
        TabelView.setRoot(root);
        TabelView.setShowRoot(false);
    }

    private List<CommonProject> GetAllProject() {
        try {
            networkOutput.writeObject(new AllProject());
            networkOutput.flush();
        } catch (IOException ex) {
            Logger.getLogger(MyProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Respone respone = (Respone) networkInput.readObject();

            if (respone.TypeRespone == ResponeType.DONE) {
                return ((SendAllProject) respone).getMylist();
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MyProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
