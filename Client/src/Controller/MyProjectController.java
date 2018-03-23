package Controller;

import CommonClass.CommonProject;
import static client.Project.PORT1;
import static client.Project.host;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import client.ResourceManager;
import client.TabelProject;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public class MyProjectController implements Initializable {

    @FXML
    private JFXTreeTableView<TabelProject> TabelView;

    @FXML
    void btnOpen(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List< CommonProject> MyProject = GetMyProject();

        JFXTreeTableColumn<TabelProject, String> nameProject = new JFXTreeTableColumn<>("Name");
        nameProject.setPrefWidth(150);
        nameProject.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabelProject, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabelProject, String> param) {
                return param.getValue().getValue().NameProject;
            }
        });

        JFXTreeTableColumn<TabelProject, String> dataCreate = new JFXTreeTableColumn<>("Date");
        dataCreate.setPrefWidth(130);
        dataCreate.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabelProject, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabelProject, String> param) {
                return param.getValue().getValue().DateCreate;
            }
        });

        JFXTreeTableColumn<TabelProject, String> author = new JFXTreeTableColumn<>("Author");
        author.setPrefWidth(140);
        author.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabelProject, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabelProject, String> param) {
                return param.getValue().getValue().Author;
            }
        });

        JFXTreeTableColumn<TabelProject, String> numberOfContributors = new JFXTreeTableColumn<>("Contributors");
        numberOfContributors.setPrefWidth(140);
        numberOfContributors.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabelProject, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabelProject, String> param) {
                return param.getValue().getValue().NumberOfContributors;
            }
        });

        JFXTreeTableColumn<TabelProject, String> numberOfCommits = new JFXTreeTableColumn<>("Commits");
        numberOfCommits.setPrefWidth(130);
        numberOfCommits.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TabelProject, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TabelProject, String> param) {
                return param.getValue().getValue().NumberOfCommits;
            }
        });

        ObservableList<TabelProject> users = FXCollections.observableArrayList();
        users.add(new TabelProject("Computer", "2012", "Mohammad", "3", "2"));
        users.add(new TabelProject("Sales", "2015", "Ahmad", "2", "3"));
        users.add(new TabelProject("IT", "2018", "Moaz", "5", "23"));

        final TreeItem<TabelProject> root = new RecursiveTreeItem<TabelProject>(users, RecursiveTreeObject::getChildren);
        TabelView.getColumns().setAll(nameProject, dataCreate, author, numberOfContributors, numberOfCommits);
        TabelView.setRoot(root);
        TabelView.setShowRoot(false);

    }

    private List<CommonProject> GetMyProject() {
        System.out.println("Send Request to Server for create List of myproject");
        networkOutput.println("MYPROJECT");

        try {
            String respone = networkInput.readLine();

            if (respone.equals("Done")) {
                
                /*
                * here another Connect To Second ServerSocket For Send Files
                */

                Socket socket1 = new Socket(host, PORT1);

                DataInputStream dis = new DataInputStream(socket1.getInputStream());

                FileOutputStream fos = new FileOutputStream("temp.data");

                byte[] buffer = new byte[4096];

                int filesize = 15123; // Send file size in separate msg
                int read = 0;
                int totalRead = 0;
                int remaining = filesize;
                while ((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                    totalRead += read;
                    remaining -= read;
                    System.out.println("read " + totalRead + " bytes.");
                    fos.write(buffer, 0, read);
                }

                fos.close();
                dis.close();

                List<CommonProject> mylist;

                try {
                    mylist = (List<CommonProject>) ResourceManager.load("temp.data"); /// This List Must put inside TabelView
                    System.out.println("Number Of Project for User is : " + mylist.size());
                } catch (Exception ex) {

                    System.err.println("Error : " + ex.getMessage());
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(MyProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }
}
