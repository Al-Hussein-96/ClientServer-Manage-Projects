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
import com.sun.javafx.css.parser.Css2Bin;
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
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class BranchController implements Initializable {

    FileBrowsersController Father;
    List<CommonBranch> NameBranch;
    String CurrBranch;

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
        doubleClick_Open();
    }

    void doubleClick_Open(){
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
        Father.CreateBranchSelected(CB.branchName, CB.way.get(CB.way.size() - 1).Id);
        Open.getScene().getWindow().hide();
    }
    @FXML
    void btnAdd(ActionEvent event) {
        if (BranchName.getText() != null) {

            /*  here must get CurrentBranch from GUI and LastId in CurrentBranch */
            CommonBranch CB = null;
            for (CommonBranch temp : NameBranch) {
                if (temp.branchName.equals(CurrBranch)) {
                    CB = temp;
                    break;
                }
            }
            Command command = new GetAddBranch(Father.Owner.NameProject, BranchName.getText(), CB.branchName, CB.way.get(CB.way.size() - 1).Id);
            try {
                networkOutput.writeObject(command);
                networkOutput.flush();
                Respone respone = (Respone) networkInput.readObject();
                if (respone.TypeRespone == ResponeType.DONE) {
                    /// here Update Tabel With NewBranch Or Back To FileBrowsers
                    BranchName.getScene().getWindow().hide();
                    Notifications notification = Notifications.create()
                            .title("Add Branch")
                            .text("Done Add Branch.")
                            .graphic(null)
                            .hideAfter(Duration.seconds(2))
                            .position(Pos.CENTER);
                    notification.showConfirm();
                } else {
                    Notifications notification = Notifications.create()
                            .title("Add Branch")
                            .text("Can't Add Branch.")
                            .graphic(null)
                            .hideAfter(Duration.seconds(2))
                            .position(Pos.CENTER);
                    notification.showConfirm();
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(BranchController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public BranchController(FileBrowsersController Father, List<CommonBranch> NameBranch, String CurrBranch) {
        this.Father = Father;
        this.NameBranch = NameBranch;
        this.CurrBranch = CurrBranch;
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
        tabelView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                doubleClick_Open();
            }
        });
    }
}
