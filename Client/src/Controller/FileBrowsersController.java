package Controller;

import CommonClass.CommonProject;
import CommonClass.NameAndDirectory;
import CommonClass.ResourceManager;
import CommonClass.ViewfolderClass;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import client.TabelBrowsers;
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
            st[i] = new TabelBrowsers(s1, true,i);
        }
        for (int i = 0; i < MyFile.size(); i++) {
            String s1 = MyFile.get(i).Name;
            st[i + MyFolder.size()] = new TabelBrowsers(s1, false,i+MyFolder.size());
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
    void btnOpen(ActionEvent event) {
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
            // HER OPEN THE FILE 
        }
    }

    ViewfolderClass GetMyProject() {
        ViewfolderClass mylist = null;
        try {
            networkOutput.writeUTF("GETPROJECT");
            String respone = networkInput.readUTF();
            if (respone.equals("Done")) {
                networkOutput.writeUTF("SendNameProject");
                System.out.println(Owner);
                networkOutput.writeUTF(Owner.NameProject);

                FileOutputStream fos = new FileOutputStream("temp1.data");

                byte[] buffer = new byte[5005];
                System.out.println("YES");
                int filesize = networkInput.readInt(); // Send file size in separate msg
                System.out.println("Size = " + filesize);

                //  int filesize = 17428; // Send file size in separate msg
                int read = 0;
                int totalRead = 0;
                int remaining = filesize;
                if ((read = networkInput.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                    totalRead += read;
                    remaining -= read;
                    System.out.println("read " + totalRead + " bytes.");
                    fos.write(buffer, 0, read);
                }
                fos.close();

                try {
                    mylist = (ViewfolderClass) ResourceManager.load("temp1.data"); /// This List Must put inside TabelView
                    System.out.println("Number Of Project for User is : " + mylist.MyFile.size() + " : " + mylist.MyFolder.size() + " : " + mylist.MyFolderView.size());
                } catch (Exception ex) {
                    System.err.println("Error : " + ex.getMessage());
                }

            } else {

            }

//        CreateTabelView("D:\\ITE 2017\\المشروع 1\\ClientServer-Manage-Projects\\Client");
        } catch (IOException ex) {
            Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mylist;
    }
}
