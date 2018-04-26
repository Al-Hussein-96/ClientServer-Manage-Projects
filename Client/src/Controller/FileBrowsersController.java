package Controller;

import CommonClass.CommitClass;
import CommonClass.CommonBranch;
import CommonClass.CommonProject;
import CommonClass.Contributor;
import CommonClass.NameAndDirectory;
import CommonClass.ViewfolderClass;
import CommonCommand.Command;
import CommonCommand.GetCommits;
import CommonCommand.GetBranch;
import CommonCommand.GetFile;
import CommonCommand.GetListBranch;
import CommonCommand.GetListCommits;
import CommonCommand.GetListContributors;
import CommonCommand.GetProject;
import CommonCommand.GetPull;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import CommonRespone.SendFile;
import CommonRespone.SendListBranch;
import CommonRespone.SendListCommits;
import CommonRespone.SendListContributors;
import CommonRespone.SendProject;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import client.TabelBrowsers;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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

    @FXML
    private JFXButton push;

    @FXML
    private Label idCommit;

    @FXML
    private Label idBranch;

    ViewfolderClass current = null;

    List<ViewfolderClass> previous = new ArrayList<>();

    public FileBrowsersController(CommonProject Owner) {
        this.Owner = Owner;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List< CommonBranch> listB = Owner.BranchNames;
        List< CommitClass> listC = listB.get(listB.size() - 1).way;
        idBranch.setText("Branch : " + listB.get(listB.size() - 1).branchName);
        idCommit.setText("Commit : " + listC.get(listC.size() - 1).Id);
        current = GetMyProject();
        ShowFolder(current);
        List<Contributor> Con = Owner.Contributors;
        boolean Access = false;
        for (int i = 0; i < Con.size(); i++) {
            if (Con.get(i).Name.equals(PageMainController.Owner.getName())) {
                Access = true;
            }
        }
        if (!Access) {
            push.setVisible(false);
        }

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
            st[i].setDiectoryServer(Dir);  //// need it for open File
        }
        for (int i = 0; i < MyFile.size(); i++) {
            String s1 = MyFile.get(i).Name;
            if (s1.equals("BEHKN.BEHKN")) {
                continue;
            }
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
            /**
             * Here Open File in Windows Desktop
             *
             */
            Command command = new GetFile(TI.DiectoryServer);
            try {
                networkOutput.writeObject(command);
                networkOutput.flush();

                FileOutputStream fos = new FileOutputStream(TI.getName());
                SendFile respone;
                do {
                    respone = (SendFile) networkInput.readObject();
                    fos.write(respone.getDataFile());
                } while (!respone.isEndOfFile());

                fos.close();

                File file = new File(TI.getName());
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Error in GetFile " + ex.getMessage());
            }

        }
    }

    @FXML
    void btnPull(ActionEvent event) {

        /// "Master" is temp for try and 1 is temp
        Command command = new GetPull(Owner.NameProject, 1, "Master");
        try {
            networkOutput.writeObject(command);
            networkOutput.flush();
            SendProject respone = (SendProject) networkInput.readObject();
            CreateFolder(respone.ob);
            Receive(respone.ob);

        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error in btnPull : " + ex.getMessage());
        }

    }

    @FXML
    void btnPush(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/PushProject.fxml"));

        Stage stage = new Stage();
        try {
            AnchorPane root = (AnchorPane) fxmlLoader.load();
            PushProjectController pushProjectController = fxmlLoader.getController();
            pushProjectController.setNameProject(Owner.NameProject);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            System.out.println("Error in Load Fxml PushProject: " + ex.getMessage());
        }

    }

    @FXML
    void btnBranch(ActionEvent event) {
        Command command = new GetListBranch(Owner.NameProject);
        try {
            networkOutput.writeObject(command);
            networkOutput.flush();
        } catch (IOException ex) {
            System.out.println("Error: btnBranch");
        }
        Respone respone = null;

        try {
            respone = (Respone) networkInput.readObject();

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Errot in FileBrowsers");
        }

        BranchController branchController = new BranchController(this, ((SendListBranch) respone).getListbranch());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Branch.fxml"));
        fxmlLoader.setController(branchController);
        Stage stage = new Stage();
        try {
            AnchorPane root = (AnchorPane) fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void btnCommits(ActionEvent event) {
        /// Master is temp
        Command command = new GetListCommits(Owner.NameProject, "Master");
        try {
            networkOutput.writeObject(command);
            networkOutput.flush();
        } catch (IOException ex) {
            System.out.println("Error: btnBranch");
        }
        Respone respone = null;
        try {
            respone = (Respone) networkInput.readObject();

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Errot in FileBrowsers");
        }
        CommitsController commitsController = new CommitsController(this, ((SendListCommits) respone).getListCommit());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Commits.fxml"));
        fxmlLoader.setController(commitsController);
        Stage stage = new Stage();
        try {
            AnchorPane root = (AnchorPane) fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void btnContributors(ActionEvent event) {
        Command command = new GetListContributors(Owner.NameProject);
        try {
            networkOutput.writeObject(command);
            networkOutput.flush();
        } catch (IOException ex) {
            System.out.println("Error: btnBranch");
        }
        Respone respone = null;
        try {
            respone = (Respone) networkInput.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Errot in FileBrowsers");
        }
        ContributorsController contributorsController = new ContributorsController(((SendListContributors) respone).getList(), Owner.NameProject);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Contributors.fxml"));
        fxmlLoader.setController(contributorsController);
        Stage stage = new Stage();
        try {
            AnchorPane root = (AnchorPane) fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(FileBrowsersController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void CreateBranchSelected(String BranchName,int ID) {
        previous.clear();
        current = GetMyBranch(BranchName);
        if (current != null) {
            ShowFolder(current);
            idBranch.setText("Branch : " + BranchName);
            idCommit.setText("Commit : " + ID);
        }
    }

    ViewfolderClass GetMyBranch(String BranchName) {
        try {
            Command command = new GetBranch(Owner.NameProject, BranchName);
            networkOutput.writeObject(command);
            networkOutput.flush();
            Respone respone = (Respone) networkInput.readObject();
            if (respone.TypeRespone == ResponeType.DONE) {
                return ((SendProject) respone).ob;
            } else {
                System.out.println("Error in Project");
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FileBrowsersController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void CreateCommitSelected(String BranchName, int ID) {
        previous.clear();
        current = GetMyCommit(BranchName, ID);
        if (current != null) {
            ShowFolder(current);
            idBranch.setText("Branch : " + BranchName);
            idCommit.setText("Commit : " + ID);
        }
    }

    ViewfolderClass GetMyCommit(String BranchName, int ID) {
        try {
            Command command = new GetCommits(Owner.NameProject, BranchName, ID);
            networkOutput.writeObject(command);
            networkOutput.flush();
            Respone respone = (Respone) networkInput.readObject();
            if (respone.TypeRespone == ResponeType.DONE) {
                return ((SendProject) respone).ob;
            } else {
                System.out.println("Error in Project");
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FileBrowsersController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void CreateFolder(ViewfolderClass ob) {
        List<NameAndDirectory> Folder = ob.MyFolder;

        for (NameAndDirectory u : Folder) {
            File folder = new File(u.Directory);
            folder.mkdir();

            for (ViewfolderClass temp : ob.MyFolderView) {
                CreateFolder(temp);
            }
        }

    }

    private void Receive(ViewfolderClass ob) {
        for (NameAndDirectory temp : ob.MyFile) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(temp.Directory);
                SendFile respone;
                do {
                    respone = (SendFile) networkInput.readObject();
//                    fos.write(respone.getDataFile());
                    fos.write(respone.getDataFile(), 0, (int) Math.min(4096, respone.getNumberOfByte()));
                } while (!respone.isEndOfFile());
                fos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        for (ViewfolderClass temp : ob.MyFolderView) {
            Receive(temp);
        }
    }
}
