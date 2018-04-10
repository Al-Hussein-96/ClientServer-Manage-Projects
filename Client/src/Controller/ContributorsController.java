package Controller;

import CommonClass.Contributor;
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

public class ContributorsController implements Initializable {

    List<Contributor> Contributors;

    @FXML
    private TableView<TabelBranch> tabelView;

    @FXML
    private TableColumn<TabelBranch, String> C1;

    public ContributorsController(List<Contributor> Contributors) {
        this.Contributors = Contributors;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        System.out.println("Size: " + Contributors.size());
//        ObservableList<TabelBranch> list;
//        TabelBranch[] st = new TabelBranch[Contributors.size()];
//        int idx = 0;
//        for (String temp : Contributors) {
//            st[idx] = new TabelBranch(temp);
//        }
//        list = FXCollections.observableArrayList(st);
//        C1.setCellValueFactory(new PropertyValueFactory<>("NameBranch"));
//        tabelView.setItems(list);
    }
}
