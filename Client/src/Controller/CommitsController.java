package Controller;

import CommonClass.CommitClass;
import CommonClass.CommonBranch;
import client.TabelBranch;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CommitsController implements Initializable {

    List<CommonBranch> Commits;

    @FXML
    private TableView<CommitClass> tabelView;

    @FXML
    private TableColumn<CommitClass, String> C1;
    @FXML
    private TableColumn<CommitClass, String> C2;
    @FXML
    private TableColumn<CommitClass, String> C3;
    @FXML
    private TableColumn<CommitClass, String> C4;
    @FXML
    private TableColumn<CommitClass, String> C5;

    public CommitsController(List<CommonBranch> Commits) {
        this.Commits = Commits;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        System.out.println("Size: " + Commits.size());
//        ObservableList<CommitClass> list;
//        CommitClass[] st = new CommitClass[Commits.size()];
//        int idx = 0;
//        for (CommitClass temp : Commits) {
//            CommitClass CC = new CommitClass(temp.branchName, temp.Author, temp.Directory, temp.Detail, temp.Id);
//            CC.MyDate = temp.MyDate;
//            st[idx] = CC;
//        }
//        list = FXCollections.observableArrayList(st);
//        C1.setCellValueFactory(new PropertyValueFactory<>("NameBranch"));
//        tabelView.setItems(list);
    }

}
