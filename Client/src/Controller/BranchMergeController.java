/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import CommonClass.CommonBranch;
import CommonClass.ProjectToUpload;
import CommonClass.ResourceManager;
import CommonCommand.Command;
import CommonCommand.GetMerge;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import CommonRespone.SendProject;
import CommonRespone.SendProject_Merge;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import client.TabelBranch;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
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
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author Al-Hussein
 */
public class BranchMergeController implements Initializable {

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
    private JFXTextField txtLocation;

    @FXML
    private JFXButton Open;

    @FXML
    private JFXTextField BranchName;

    public BranchMergeController(FileBrowsersController Father, List<CommonBranch> NameBranch, String CurrBranch) {
        this.Father = Father;
        this.NameBranch = NameBranch;
        this.CurrBranch = CurrBranch;
    }

    @FXML
    void btnBroswers(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        File selectedFile = dc.showDialog(null);
        if (selectedFile == null) {
            return;
        }
        txtLocation.setText(selectedFile.getPath());
    }

    @FXML
    void btnMerge(ActionEvent event) {
        Command command = new GetMerge(Father.Owner.NameProject, CurrBranch, tabelView.getSelectionModel().getSelectedItem().getBranchName());

        try {
            networkOutput.writeObject(command);
            networkOutput.flush();
            SendProject_Merge respone = (SendProject_Merge) networkInput.readObject();

            if (respone.TypeRespone == ResponeType.DONE) {
                System.out.println("Size Respone : " + respone.ob.MyFile.size());
                Father.CreateFolder(respone.ob, txtLocation.getText() + "\\");
                Father.Receive(respone.ob, txtLocation.getText() + "\\");

                ProjectToUpload hiddenFile = (ProjectToUpload) networkInput.readObject();
                try {
                    /// save File in directory of Project
                    ResourceManager.save(hiddenFile, txtLocation.getText() + "\\" + "BEHKN.BEHKN");
                } catch (Exception ex) {
                    Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } else {

            }

        } catch (IOException e) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BranchMergeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<TabelBranch> list;
        TabelBranch[] st = new TabelBranch[NameBranch.size()];
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        int idx = 0;
        System.out.println("Size = " + NameBranch.size());
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
