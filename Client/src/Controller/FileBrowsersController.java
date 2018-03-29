package Controller;

import client.TabelBrowsers;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.activation.MimetypesFileTypeMap;

public class FileBrowsersController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateTabelView("D:\\ITE 2017\\المشروع 1\\ClientServer-Manage-Projects\\Client");

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
