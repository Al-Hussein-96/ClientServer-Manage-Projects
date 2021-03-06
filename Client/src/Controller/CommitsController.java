package Controller;

import CommonClass.CommitClass;
import CommonClass.CommonBranch;
import client.TabelBranch;
import client.TabelCommit;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CommitsController implements Initializable {

    FileBrowsersController Father;
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

    @FXML
    private JFXButton Open;

    @FXML
    void btnOpen(ActionEvent event) {
        doubleClick_Open();
    }

    void doubleClick_Open() {
        TabelCommit TC = tabelView.getSelectionModel().getSelectedItem();
        CommitClass CC = null;
        if (Commits == null || TC == null) {
            return;
        }
        for (int i = 0; i < Commits.size(); i++) {
            if (TC.equal(Commits.get(i))) {
                CC = Commits.get(i);
                break;
            }
        }
        if (CC == null) {
            return;
        }
        if (CC.Id == 1) {
            Father.CreateCommitSelectedFirstCommit(CC.branchName, CC.Id);
        } else {
            Father.CreateCommitSelected(CC.branchName, CC.Id, CC.Id - 1);
        }
        Open.getScene().getWindow().hide();
    }

    public CommitsController(FileBrowsersController Father, List<CommitClass> Commits) {
        this.Father = Father;
        this.Commits = Commits;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        tabelView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                doubleClick_Open();
            }
        });
    }

}
