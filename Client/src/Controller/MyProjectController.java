package Controller;

import CommonClass.CommonProject;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import CommonCommand.MyProject;
import CommonRespone.Respone;
import CommonRespone.SendMyProject;
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

public class MyProjectController implements Initializable {

    private AnchorPane roopane;
    @FXML
    private JFXButton open;
    @FXML
    private JFXTreeTableView<TabelProject> TabelView;
    private List< CommonProject> MyProject;

    @FXML
    void btnOpen(ActionEvent event) {
        TreeItem<TabelProject> TI = TabelView.getSelectionModel().getSelectedItem();
        CommonProject CP = null;
        TabelProject TP = null;
        if (TI != null) {
            TP = TI.getValue();
        }
        if (MyProject == null || TP == null) {
            return;
        }
        for (int i = 0; i < MyProject.size(); i++) {
            if (TP.equal(MyProject.get(i))) {
                CP = MyProject.get(i);
            }
        }
        if (CP == null) {
            return;
        }
        //  HER GO TO THE NEXT WINDOW AND SENT CP TO SHOW IT

        FileBrowsersController fileBrowsersController = new FileBrowsersController(CP);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/FileBrowsers.fxml"));
        fxmlLoader.setController(fileBrowsersController);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException ex) {
            System.out.println("Error:::: " + ex.getMessage());
        }
        AnchorPane pane;
        pane = (AnchorPane) root;
        roopane.getChildren().setAll(pane);
    }

    public void setRoopane(AnchorPane roopane) {
        this.roopane = roopane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MyProject = GetMyProject();

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

        for (int i = 0; i < MyProject.size(); i++) {

            CommonProject CP = MyProject.get(i);
            TabelProject TP = new TabelProject(CP.NameProject, ft.format(CP.DateCreate),
                    CP.Author, String.valueOf(CP.Contributors.size()), String.valueOf(CP.way.size()));
            users.add(TP);
        }

//        users.add(new TabelProject("IT", "2018", "Moaz", "5", "23"));
        final TreeItem<TabelProject> root = new RecursiveTreeItem<TabelProject>(users, RecursiveTreeObject::getChildren);
        TabelView.getColumns().setAll(nameProject, dataCreate, author, numberOfContributors, numberOfCommits);
        TabelView.setRoot(root);
        TabelView.setShowRoot(false);
    }

    private List<CommonProject> GetMyProject() {

        try {
            networkOutput.writeObject(new MyProject());
            networkOutput.flush();

        } catch (IOException ex) {
            System.out.println("Error in function : GetMyProject  Class: MyProjectController  : " + ex.getMessage());
            return null;
        }
        try {
            Respone respone = (Respone) networkInput.readObject();
            if (respone.TypeRespone == ResponeType.DONE) {
                return ((SendMyProject) respone).getMylist();
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error3 in function : GetMyProject  Class: MyProjectController  : " + ex.getMessage());
        }

        return null;
    }
}
