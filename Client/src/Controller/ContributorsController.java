package Controller;

import CommonClass.Contributor;
import CommonCommand.Command;
import CommonCommand.GetAddContributor;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import client.Notification;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import client.TabelContributor;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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

public class ContributorsController implements Initializable {

    String NameProject;

    List<Contributor> Contributors;

    @FXML
    private TableView<TabelContributor> tabelView;

    @FXML
    private TableColumn<TabelContributor, String> C1;
    @FXML
    private TableColumn<TabelContributor, String> C2;
    @FXML
    private JFXTextField NameContributors;

    @FXML
    void btnAdd(ActionEvent event) {
        /**
         * Add New Contributors
         */
        Command command = new GetAddContributor(NameProject, NameContributors.getText());
        try {
            networkOutput.writeObject(command);
            networkOutput.flush();
            Respone respone = (Respone) networkInput.readObject();
            if (respone.TypeRespone == ResponeType.DONE) {
                //// Update Tabel
                NameContributors.getScene().getWindow().hide();
                
                Notifications notification = Notifications.create()
                        .title("Add Contributor")
                        .text("Done Add Contributor.")
                        .graphic(null)
                        .hideAfter(Duration.seconds(2))
                        .position(Pos.CENTER);
                notification.showConfirm();
            }
            else{
             Notification.Notification("Add Contributes", respone.Message);
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ContributorsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    void btnOpen(ActionEvent event) {

    }

    public ContributorsController(List<Contributor> Contributors, String NameProject) {
        this.Contributors = Contributors;
        this.NameProject = NameProject;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<TabelContributor> list;
        TabelContributor[] st = new TabelContributor[Contributors.size()];
        int idx = 0;
        for (Contributor temp : Contributors) {
            st[idx++] = new TabelContributor(temp.Name, String.valueOf(temp.NumberOfCommit));
        }
        list = FXCollections.observableArrayList(st);
        C1.setCellValueFactory(new PropertyValueFactory<>("Name"));
        C2.setCellValueFactory(new PropertyValueFactory<>("NumberOfCommit"));
        tabelView.setItems(list);
    }
}
