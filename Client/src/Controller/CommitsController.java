package Controller;

import CommonClass.CommitClass;
import CommonClass.CommonBranch;
import client.TabelBranch;
import client.TabelCommit;
import java.net.URL;
import java.text.SimpleDateFormat;
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

    List<CommitClass> Commits;

    @FXML
    private TableView<TabelCommit> tabelView;

    @FXML
    private TableColumn<TabelCommit, String> C1;
    @FXML
    private TableColumn<TabelCommit, String> C2;
    @FXML
    private TableColumn<TabelCommit, String> C3;
    @FXML
    private TableColumn<TabelCommit, String> C4;
    @FXML
    private TableColumn<TabelCommit, String> C5;

    public CommitsController(List<CommitClass> Commits) {
        this.Commits = Commits;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Size: " + Commits.size());
        ObservableList<TabelCommit> list;
        TabelCommit[] st = new TabelCommit[Commits.size()];
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        int idx = 0;
        for (CommitClass temp : Commits) {
            TabelCommit CC = new TabelCommit(temp.Author, temp.branchName, String.valueOf(temp.Id),
                     ft.format(temp.MyDate), temp.Detail);
            st[idx++] = CC;
        }
        list = FXCollections.observableArrayList(st);
        C1.setCellValueFactory(new PropertyValueFactory<>("Author"));
        C2.setCellValueFactory(new PropertyValueFactory<>("BranchName"));
        C3.setCellValueFactory(new PropertyValueFactory<>("Id"));
        C4.setCellValueFactory(new PropertyValueFactory<>("MyDate"));
        C5.setCellValueFactory(new PropertyValueFactory<>("Detail"));
        tabelView.setItems(list);
    }

}
