package Controller;

import CommonClass.CommonProject;
import CommonClass.ResourceManager;
import CommonClass.ViewfolderClass;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import client.TabelBrowsers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    public FileBrowsersController(CommonProject Owner) {
        this.Owner = Owner;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            networkOutput.writeUTF("GETPROJECT");

            String respone = networkInput.readUTF();

            if (respone.equals("Done")) {
                networkOutput.writeUTF("SendNameProject");
                System.out.println(Owner);
                networkOutput.writeUTF(Owner.NameProject);

//                FileOutputStream fos = new FileOutputStream("temp.data");
//
//                byte[] buffer = new byte[5005];
//                System.out.println("YES");
//                int filesize = networkInput.readInt(); // Send file size in separate msg
//                System.out.println("Size = " + filesize);
//
//                //  int filesize = 17428; // Send file size in separate msg
//                int read = 0;
//                int totalRead = 0;
//                int remaining = filesize;
//                if ((read = networkInput.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
//                    totalRead += read;
//                    remaining -= read;
//                    System.out.println("read " + totalRead + " bytes.");
//                    fos.write(buffer, 0, read);
//                }
//                fos.close();
//
//                ViewfolderClass mylist;
//                try {
//                    mylist = (ViewfolderClass) ResourceManager.load("temp.data"); /// This List Must put inside TabelView
//                    System.out.println("Number Of Project for User is : " + mylist);
//                } catch (Exception ex) {
//                    System.err.println("Error : " + ex.getMessage());
//                }

            } else {

            }

//        CreateTabelView("D:\\ITE 2017\\المشروع 1\\ClientServer-Manage-Projects\\Client");
        } catch (IOException ex) {
            Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void CreateTabelView(String path) {
        tabelView.getItems().clear();
        File file = new File(path);

        DateFormat format = new SimpleDateFormat("dd/MM/yy");

        File[] fl = file.listFiles();

        TabelBrowsers[] st = new TabelBrowsers[fl.length];

        ObservableList<TabelBrowsers> list;

        for (int i = 0; i < fl.length; i++) {
            String s1;

            try {
                s1 = fl[i].getName();
                st[i] = new TabelBrowsers(s1);

            } catch (Exception e) {

            }

        }
        list = FXCollections.observableArrayList(st);

        //  ImageIcon.setCellValueFactory(new PropertyValueFactory<>("ImageIcon"));
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Size.setCellValueFactory(new PropertyValueFactory<>("Size"));
        DataModified.setCellValueFactory(new PropertyValueFactory<>("DataModified"));
        System.out.println("fsdfsdf");
        tabelView.setItems(list);

    }

}
