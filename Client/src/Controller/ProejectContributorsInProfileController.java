package Controller;

import CommonClass.CommonBranch;
import CommonClass.CommonProject;
import CommonClass.User;
import client.TabelBranch;
import client.TabelProject;
import com.jfoenix.controls.JFXButton;
import static java.awt.PageAttributes.MediaType.C1;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class ProejectContributorsInProfileController implements Initializable {

    List<CommonProject> Myproject;
    User user;
    AnchorPane roopane;

    @FXML
    private TableView<TabelProject> tabelView;
    @FXML
    private TableColumn<TabelProject, String> C1;
    @FXML
    private TableColumn<TabelProject, String> C2;
    @FXML
    private TableColumn<TabelProject, String> C3;
    @FXML
    private TableColumn<TabelProject, String> C4;
    @FXML
    private TableColumn<TabelProject, String> C5;
    @FXML
    private JFXButton Open;

    @FXML
    private void btnOpen(ActionEvent event) {
        doubleClick_Open();
    }

    void doubleClick_Open() {
        TabelProject TP = tabelView.getSelectionModel().getSelectedItem();
        CommonProject CP = null;
        if (Myproject == null || TP == null) {
            return;
        }
        for (int i = 0; i < Myproject.size(); i++) {
            if (TP.equal(Myproject.get(i))) {
                CP = Myproject.get(i);
                break;
            }
        }
        if (CP == null) {
            return;
        }

        FileBrowsersController fileBrowsersController = new FileBrowsersController(CP, user);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/FileBrowsers.fxml"));

        fxmlLoader.setController(fileBrowsersController);
        AnchorPane root = null;
        try {
            root = (AnchorPane) fxmlLoader.load();
        } catch (IOException ex) {
            System.out.println("Error:::: " + ex.getMessage());
        }
        System.out.println("Bug is initlize in FileBrowsersController: " + roopane + " : " + root);
        roopane.getChildren().setAll(root);
        Open.getScene().getWindow().hide();

    }

    public ProejectContributorsInProfileController(AnchorPane roopane, User user, List<CommonProject> Myproject) {
        this.roopane = roopane;
        this.user = user;
        this.Myproject = Myproject;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<TabelProject> list;
        TabelProject[] st = new TabelProject[Myproject.size()];
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        int idx = 0;
        System.out.println("Size = " + Myproject.size());
        for (CommonProject temp : Myproject) {
            int numCommit = 0;
            for (CommonBranch t : temp.BranchNames) {
                numCommit += t.way.size();
            }
            st[idx++] = new TabelProject(temp.NameProject, ft.format(temp.DateCreate), temp.Author, String.valueOf(temp.Contributors.size()), String.valueOf(numCommit));
        }
        list = FXCollections.observableArrayList(st);
        C1.setCellValueFactory(new PropertyValueFactory<TabelProject, String>("NameProject"));
        C2.setCellValueFactory(new PropertyValueFactory<TabelProject, String>("DateCreate"));
        C3.setCellValueFactory(new PropertyValueFactory<TabelProject, String>("Author"));
        C4.setCellValueFactory(new PropertyValueFactory<TabelProject, String>("NumberOfContributors"));
        C5.setCellValueFactory(new PropertyValueFactory<TabelProject, String>("NumberOfCommits"));
        tabelView.setItems(list);

        tabelView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                doubleClick_Open();
            }
        });
    }

}
