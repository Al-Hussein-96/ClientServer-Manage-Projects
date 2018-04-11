package Controller;

import CommonClass.CommonBranch;
import CommonClass.Contributor;
import client.TabelBranch;
import client.TabelContributor;
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
    private TableView<TabelContributor> tabelView;

    @FXML
    private TableColumn<TabelContributor, String> C1;
    @FXML
    private TableColumn<TabelContributor, String> C2;

    public ContributorsController(List<Contributor> Contributors) {
        this.Contributors = Contributors;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Size: " + Contributors.size());
        ObservableList<TabelContributor> list;
        TabelContributor[] st = new TabelContributor[Contributors.size()];
        int idx = 0;
        for (Contributor temp : Contributors) {
            st[idx] = new TabelContributor(temp.Name, String.valueOf(temp.NumberOfCommit));
        }
        list = FXCollections.observableArrayList(st);
        C1.setCellValueFactory(new PropertyValueFactory<>("Name"));
        C2.setCellValueFactory(new PropertyValueFactory<>("NumberOfCommit"));
        tabelView.setItems(list);
    }
}
