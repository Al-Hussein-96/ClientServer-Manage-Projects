package Controller;

import CommonClass.CommonBranch;
import CommonClass.CommonProject;
import CommonClass.User;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import CommonCommand.GetAllProject;
import CommonRespone.Respone;
import CommonRespone.SendAllProject;
import CommonRespone.ResponeType;
import client.TabelProject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class AllProjectController implements Initializable {

    User user;

    private AnchorPane roopane;
    @FXML
    private JFXButton open;
    @FXML
    private JFXTreeTableView<TabelProject> tabelview;
    private List< CommonProject> AllProject;

    @FXML
    void btnOpen(ActionEvent event) {
        doubleClick_Open();
    }

    void doubleClick_Open() {
        TreeItem<TabelProject> TI = tabelview.getSelectionModel().getSelectedItem();
        CommonProject CP = null;
        TabelProject TP = null;
        if (TI != null) {
            TP = TI.getValue();
        }
        if (AllProject == null || TP == null) {
            return;
        }
        for (int i = 0; i < AllProject.size(); i++) {
            if (TP.equal(AllProject.get(i))) {
                CP = AllProject.get(i);
                break;
            }
        }
        if (CP == null) {
            return;
        }
        //  HER GO TO THE NEXT WINDOW AND SENT CP TO SHOW IT

        FileBrowsersController fileBrowsersController = new FileBrowsersController(CP, user);
        fileBrowsersController.setRoopane(roopane);
        fileBrowsersController.setIf_PreviousPageIsMyProject(false);
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/FileBrowsers.fxml"));
        fxmlLoader.setController(fileBrowsersController);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
            AnchorPane pane;
            pane = (AnchorPane) root;
            roopane.getChildren().setAll(pane);
        } catch (IOException ex) {
            System.out.println("Error:::: " + ex.getMessage());
        }
    }

    public void setRoopane(AnchorPane roopane) {
        this.roopane = roopane;
    }

    public void setUser(User user) {
        this.user = user;
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
            int numCommit = 0;
            for (CommonBranch t : CP.BranchNames) {
                numCommit += t.way.size();
            }
            TabelProject TP = new TabelProject(CP.NameProject, ft.format(CP.DateCreate),
                    CP.Author, String.valueOf(CP.Contributors.size()), String.valueOf(numCommit));
            users.add(TP);
        }
//        users.add(new TabelProject("IT", "2018", "Moaz", "5", "23"));

        final TreeItem<TabelProject> root = new RecursiveTreeItem<>(users, RecursiveTreeObject::getChildren);
        tabelview.getColumns().setAll(nameProject, dataCreate, author, numberOfContributors, numberOfCommits);
        tabelview.setRoot(root);
        tabelview.setShowRoot(false);
        tabelview.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                doubleClick_Open();
            }
        });
    }

    private List<CommonProject> GetAllProject() {
        try {
            networkOutput.writeObject(new GetAllProject());
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
