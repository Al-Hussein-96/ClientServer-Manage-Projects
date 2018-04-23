package Controller;

import CommonClass.CommonBranch;
import CommonCommand.Command;
import CommonCommand.GetAddBranch;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import client.TabelBranch;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BranchController implements Initializable {

    FileBrowsersController Father;
    List<CommonBranch> NameBranch;

    @FXML
    private TableView<TabelBranch> tabelView;

    @FXML
    private TableColumn<TabelBranch, String> C1;
    @FXML
    private TableColumn<TabelBranch, String> C2;
    @FXML
    private TableColumn<TabelBranch, String> C3;
    @FXML
    private JFXButton Open;

    @FXML
    private JFXTextField BranchName;

    @FXML
    void btnOpen(ActionEvent event) {
        TabelBranch TB = tabelView.getSelectionModel().getSelectedItem();
        CommonBranch CB = null;
        if (NameBranch == null || TB == null) {
            return;
        }
        for (int i = 0; i < NameBranch.size(); i++) {
            if (TB.equal(NameBranch.get(i))) {
                CB = NameBranch.get(i);
                break;
            }
        }
        if (CB == null) {
            return;
        }
        Father.CreateBranchSelected(CB.branchName);
        Open.getScene().getWindow().hide();
    }

    @FXML
    void btnAdd(ActionEvent event) {
        if (BranchName.getText() != null) {

            /*  here must get CurrentBranch from GUI and LastId in CurrentBranch */
            Command command = new GetAddBranch(Father.Owner.NameProject, BranchName.getText(), "hdfgdf", 1);

            try {
                networkOutput.writeObject(command);
                networkOutput.flush();

                Respone respone = (Respone) networkInput.readObject();

                if (respone.TypeRespone == ResponeType.DONE) {
                    /// here Update Tabel With NewBranch Or Back To FileBrowsers

                } else {

                }

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(BranchController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public BranchController(FileBrowsersController Father, List<CommonBranch> NameBranch) {
        this.Father = Father;
        this.NameBranch = NameBranch;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<TabelBranch> list;
        TabelBranch[] st = new TabelBranch[NameBranch.size()];
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        int idx = 0;
        for (CommonBranch temp : NameBranch) {
            st[idx++] = new TabelBranch(temp.branchName, temp.userCreateBranch, ft.format(temp.lastCommite));
        }
        list = FXCollections.observableArrayList(st);
        C1.setCellValueFactory(new PropertyValueFactory<TabelBranch, String>("BranchName"));
        C2.setCellValueFactory(new PropertyValueFactory<TabelBranch, String>("UserCreateBranch"));
        C3.setCellValueFactory(new PropertyValueFactory<TabelBranch, String>("LastCommite"));
        tabelView.setItems(list);
    }
}
