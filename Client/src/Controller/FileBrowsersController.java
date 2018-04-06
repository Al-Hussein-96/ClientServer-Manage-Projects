package Controller;

import CommonClass.CommonProject;
import CommonClass.NameAndDirectory;
import CommonClass.ViewfolderClass;
import CommonCommand.Command;
import CommonCommand.GetFile;
import CommonCommand.GetProject;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import CommonRespone.SendFile;
import CommonRespone.SendProject;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import client.TabelBrowsers;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

public class FileBrowsersController implements Initializable {

    CommonProject Owner;

    @FXML
    private TableView<TabelBrowsers> tabelView;

    @FXML
    private TableColumn<TabelBrowsers, String> ImageIcon;

    @FXML
    private TableColumn<TabelBrowsers, String> Name;

    @FXML
    private TableColumn<TabelBrowsers, String> DataModified;

    @FXML
    private TableColumn<TabelBrowsers, String> Size;

    ViewfolderClass current = null;
    List<ViewfolderClass> previous = new ArrayList<>();

    public FileBrowsersController(CommonProject Owner) {
        this.Owner = Owner;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        current = GetMyProject();
        ShowFolder(current);
    }

    private void ShowFolder(ViewfolderClass MyProject) {
        tabelView.getItems().clear();
        List<  NameAndDirectory> MyFile = MyProject.MyFile;
        List<  NameAndDirectory> MyFolder = MyProject.MyFolder;
        ObservableList<TabelBrowsers> list;
        int LengthTable = MyFile.size() + MyFolder.size();
        TabelBrowsers[] st = new TabelBrowsers[LengthTable];
        for (int i = 0; i < MyFolder.size(); i++) {
            String s1 = MyFolder.get(i).Name;
            st[i] = new TabelBrowsers(s1, true, i);
            String Dir = MyFolder.get(i).Directory;
            st[i].setDiectoryServer(Dir.substring(Dir.indexOf(Owner.NameProject)));  //// need it for open File
        }
        for (int i = 0; i < MyFile.size(); i++) {
            String s1 = MyFile.get(i).Name;
            st[i + MyFolder.size()] = new TabelBrowsers(s1, false, i + MyFolder.size());
            String Dir = MyFile.get(i).Directory;
            st[i + MyFolder.size()].setDiectoryServer(Dir); //// need it for open File
        }
        list = FXCollections.observableArrayList(st);
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Size.setCellValueFactory(new PropertyValueFactory<>("Size"));
        DataModified.setCellValueFactory(new PropertyValueFactory<>("DataModified"));
        tabelView.setItems(list);
    }

    @FXML
    void btnBack(ActionEvent event) throws IOException {
        if (previous.size() > 0) {
            current = previous.get(previous.size() - 1);
            ShowFolder(current);
            previous.remove(previous.size() - 1);
        } else {
            // HER THE FIRST PAGE
        }
    }

    @FXML
    void btnOpen(ActionEvent event){
        List< ViewfolderClass> MyFolderView = current.MyFolderView;
        TabelBrowsers TI = tabelView.getSelectionModel().getSelectedItem();
        if (TI == null || MyFolderView == null) {
            return;
        }
        if (TI.Type) {
            int index = TI.id;
            previous.add(current);
            current = MyFolderView.get(index);
            ShowFolder(current);
        } else {
            Command command = new GetFile(TI.DiectoryServer);
            try {
                networkOutput.writeObject(command);
                networkOutput.flush();
                
                FileOutputStream fos = new FileOutputStream(TI.getName());
                SendFile respone;
                do
                {
                    respone = (SendFile) networkInput.readObject();
                    fos.write(respone.getDataFile());
                    System.out.println("HE");    
                }while(!respone.isEndOfFile());
                
                fos.close();
                
                File file = new File(TI.getName());
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Error in GetFile " + ex.getMessage());
            } 
            
            

        }
    }

    ViewfolderClass GetMyProject() {
        try {
            Command command = new GetProject(Owner.NameProject);

            networkOutput.writeObject(command);
            networkOutput.flush();

            Respone respone = (Respone) networkInput.readObject();

            if (respone.TypeRespone == ResponeType.DONE) {
                return ((SendProject) respone).ob;
            } else {
                System.out.println("Error in Project");
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
