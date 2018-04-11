package Controller;

import CommonClass.CommonBranch;
import client.TabelBranch;
import client.TabelProject;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class BranchController implements Initializable {

    List<CommonBranch> NameBranch;

    @FXML
    private TableView<TabelBranch> tabelView;

    @FXML
    private TableColumn<TabelBranch, String> C1;
    @FXML
    private TableColumn<TabelBranch, String> C2;
    @FXML
    private TableColumn<TabelBranch, String> C3;

    public BranchController(List<CommonBranch> NameBranch) {
        this.NameBranch = NameBranch;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Size: " + NameBranch.size());
        ObservableList<TabelBranch> list;
        TabelBranch[] st = new TabelBranch[NameBranch.size()];
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        int idx = 0;
        for (CommonBranch temp : NameBranch) {
            st[idx++] = new TabelBranch(temp.branchName, temp.userCreateBranch, ft.format(temp.lastCommite));
//            System.out.println(temp.branchName + "  " + temp.userCreateBranch + "  " + temp.lastCommite.toString());
        }
        list = FXCollections.observableArrayList(st);
        C1.setCellValueFactory(new PropertyValueFactory<TabelBranch, String>("BranchName"));
        C2.setCellValueFactory(new PropertyValueFactory<TabelBranch, String>("UserCreateBranch"));
        C3.setCellValueFactory(new PropertyValueFactory<TabelBranch, String>("LastCommite"));
        tabelView.setItems(list);
    }
}
