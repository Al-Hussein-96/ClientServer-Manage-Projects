package Controller;

import CommonClass.CommitClass;
import CommonClass.CommonBranch;
import CommonClass.CommonProject;
import CommonClass.Contributor;
import CommonClass.NameAndDirectory;
import CommonClass.NameAndDirectoryAndState;
import CommonClass.ProjectToUpload;
import CommonClass.ResourceManager;
import CommonClass.StateType;
import CommonClass.User;
import CommonClass.ViewDiff_folderClass;
import CommonClass.ViewfolderClass;
import CommonCommand.Command;
import CommonCommand.CommandType;
import CommonCommand.GetCommits;
import CommonCommand.GetBranch;
import CommonCommand.GetDiffFile;
import CommonCommand.GetFile;
import CommonCommand.GetListBranch;
import CommonCommand.GetListCommits;
import CommonCommand.GetListContributors;
import CommonCommand.GetProject;
import CommonCommand.GetPull;
import CommonCommand.GetPullAndMerge;
import CommonCommand.GetPush;
import CommonCommand.Get_Diff_Two_Commit;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import CommonRespone.SendDiffFile;
import CommonRespone.SendFile;
import CommonRespone.SendListBranch;
import CommonRespone.SendListCommits;
import CommonRespone.SendListContributors;
import CommonRespone.SendProject;
import CommonRespone.Send_Diff_Two_Commit;
import Different.Changes;
import Different.Delete;
import Different.Diff;
import Different.Insert;
import Different.NoChange;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class FileBrowsersController implements Initializable {

    AnchorPane roopane;
    boolean previousPageIsMyProject = true;

    boolean ShowDiff = true;

    User user;

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

    ViewDiff_folderClass current = null;

    List<ViewDiff_folderClass> previous = new ArrayList<>();

    public FileBrowsersController(CommonProject Owner, User user) {
        this.Owner = Owner;
        this.user = user;
    }

    public void setOwner(CommonProject Owner) {
        this.Owner = Owner;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRoopane(AnchorPane roopane) {
        this.roopane = roopane;
    }

    public void setIf_PreviousPageIsMyProject(boolean previousPageIsMyProject) {
        this.previousPageIsMyProject = previousPageIsMyProject;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List< CommonBranch> listB = Owner.BranchNames;
        List< CommitClass> listC = listB.get(listB.size() - 1).way;
        idBranch.setText("Branch : " + listB.get(listB.size() - 1).branchName);
        idCommit.setText("Commit : " + listC.get(listC.size() - 1).Id);
        current = getViewDiff(GetMyProject());
        ShowFolderWithDiff(current);
        List<Contributor> Con = Owner.Contributors;
        boolean Access = false;
        for (int i = 0; i < Con.size(); i++) {
            if (Con.get(i).Name.equals(user.getName())) {
                Access = true;
            }
        }
        if (!Access) {
            push.setVisible(false);
        }
        tabelView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                doubleClick_Open();
            }
        });

        tabelView.setRowFactory((param) -> new TableRow<TabelBrowsers>() {

            @Override
            public void updateItem(TabelBrowsers item, boolean empty) {
                super.updateItem(item, empty);
                setStyle("-fx-background-color: white");

                if (item == null) {
                } else if (ShowDiff) {

//                    TabelBrowsers x = item;
                    if (null != item.getState()) {
                        switch (item.getState()) {
                            case Add:
                                setStyle("-fx-background-color: green");
                                break;
                            case Delete:
                                setStyle("-fx-background-color: red");
                                break;
                            case Change:
                                setStyle("-fx-background-color: orange");
                                break;
                            default:
                                break;
                        }
                    }

                }
            }
        }
        );

        tabelView.setStyle("-fx-selection-bar-non-focused: #009fff");

    }

    private ViewDiff_folderClass getViewDiff(ViewfolderClass VF) {

        ViewDiff_folderClass VD = new ViewDiff_folderClass();

        List<  NameAndDirectoryAndState> MyFile = new ArrayList<>();
        List<  NameAndDirectoryAndState> MyFolder = new ArrayList<>();
        List< ViewDiff_folderClass> MyFolderView = new ArrayList<>();

        for (NameAndDirectory temp : VF.MyFile) {
            NameAndDirectoryAndState t = new NameAndDirectoryAndState(temp, null, null);
            MyFile.add(t);
        }
        int i = 0;
        for (NameAndDirectory temp : VF.MyFolder) {
            NameAndDirectoryAndState t = new NameAndDirectoryAndState(temp, null, null);
            MyFolder.add(t);

            ViewDiff_folderClass tt = getViewDiff(VF.MyFolderView.get(i));
            MyFolderView.add(tt);
            i++;
        }
        VD.MyFile = MyFile;
        VD.MyFolder = MyFolder;
        VD.MyFolderView = MyFolderView;
        VD.MyState = StateType.NoChange;
        return VD;
    }

    private void ShowFolder(ViewfolderClass MyProject) {
        System.out.println("ShowFolder");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        tabelView.getItems().clear();
        List< NameAndDirectory> MyFile = MyProject.MyFile;
        List< NameAndDirectory> MyFolder = MyProject.MyFolder;
        ObservableList<TabelBrowsers> list;
        int LengthTable = MyFile.size() + MyFolder.size();
        TabelBrowsers[] st = new TabelBrowsers[LengthTable];
        for (int i = 0; i < MyFolder.size(); i++) {
            String s1 = MyFolder.get(i).Name;
            st[i] = new TabelBrowsers(s1, true, i, null);
            String Dir = MyFolder.get(i).Directory;
            st[i].setDiectoryServer(Dir);  //// need it for open File
            String size = "";
            st[i].setSize(size);
            Date date = new Date(MyFolder.get(i).DateModified);
            String DateModified = ft.format(date);
            st[i].setDateModified(DateModified);
        }
        for (int i = 0; i < MyFile.size(); i++) {
            String s1 = MyFile.get(i).Name;
            if (s1.equals("BEHKN.BEHKN")) {
                continue;
            }
            st[i + MyFolder.size()] = new TabelBrowsers(s1, false, i + MyFolder.size(), null);
            String Dir = MyFile.get(i).Directory;
            st[i + MyFolder.size()].setDiectoryServer(Dir); //// need it for open File
            long temp = MyFile.get(i).Size / 1024;
            if (MyFile.get(i).Size % 1024 != 0) {
                temp++;
            }
            String size = String.valueOf(temp + " KB ");
            st[i + MyFolder.size()].setSize(size);
            Date date = new Date(MyFile.get(i).DateModified);
            String DateModified = ft.format(date);
            st[i + MyFolder.size()].setDateModified(DateModified);
        }
        list = FXCollections.observableArrayList(st);
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Size.setCellValueFactory(new PropertyValueFactory<>("Size"));
        DataModified.setCellValueFactory(new PropertyValueFactory<>("DateModified"));
        tabelView.setItems(list);
    }

    @FXML
    void btnBack(ActionEvent event) throws IOException {
        if (previous.size() > 0) {
            current = previous.get(previous.size() - 1);
            ShowFolderWithDiff(current);
            previous.remove(previous.size() - 1);
        } else {
            // HER THE FIRST PAGE
            if (previousPageIsMyProject) {
                try {
                    FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/FXML/MyProject.fxml"));
                    MyProjectController myProjectController = new MyProjectController(roopane, user);
                    fXMLLoader.setController(myProjectController);
                    AnchorPane pane = (AnchorPane) fXMLLoader.load();
                    roopane.getChildren().setAll(pane);
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage() + "End Message");
                }
            } else {
                try {
                    FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/FXML/AllProject.fxml"));
                    AnchorPane pane = (AnchorPane) fXMLLoader.load();
                    AllProjectController allProjectController = fXMLLoader.getController();
                    allProjectController.setRoopane(roopane);
                    allProjectController.setUser(user);
                    roopane.getChildren().setAll(pane);
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage() + "End Message");
                }
            }
        }
    }

    @FXML
    void btnOpen(ActionEvent event) {
        doubleClick_Open();
    }

    void doubleClick_Open() {
        List< ViewDiff_folderClass> MyFolderView = current.MyFolderView;
        TabelBrowsers TI = tabelView.getSelectionModel().getSelectedItem();
        if (TI == null || MyFolderView == null) {
            return;
        }
        if (TI.Type) {
            int index = TI.id;
            previous.add(current);
            current = MyFolderView.get(index);
            ShowFolderWithDiff(current);
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
//                    fos.write(respone.getDataFile());
                    fos.write(respone.getDataFile(), 0, (int) Math.min(4096, respone.getNumberOfByte()));
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
        DirectoryChooser dc = new DirectoryChooser();
        File selectedFile = dc.showDialog(null);
        if (selectedFile == null) {
            return;
        }
        String Commit = idCommit.getText().substring(9), Branch = idBranch.getText().substring(9);
        /// "Master" is temp for try and 1 is temp
        Command command = new GetPull(Owner.NameProject, Integer.parseInt(Commit), Branch);
        try {
            networkOutput.writeObject(command);
            networkOutput.flush();
            SendProject respone = (SendProject) networkInput.readObject();
            openProgressBar();
            CreateFolder(respone.ob, selectedFile.getPath() + "\\");
            Receive(respone.ob, selectedFile.getPath() + "\\");

            ProjectToUpload hiddenFile = (ProjectToUpload) networkInput.readObject();
            try {
                /// save File in directory of Project
                ResourceManager.save(hiddenFile, selectedFile.getPath() + "\\" + "BEHKN.BEHKN");
            } catch (Exception ex) {
                Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
            }
            /**
             * **
             */
            Notifications notification = Notifications.create()
                    .title("Pull Project")
                    .text("Done Pull Project")
                    .graphic(null)
                    .hideAfter(Duration.seconds(2))
                    .position(Pos.CENTER);
            notification.showConfirm();

        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error in btnPull : " + ex.getMessage());
            Notifications notification = Notifications.create()
                    .title("Pull Project")
                    .text("Can't Pull Project")
                    .graphic(null)
                    .hideAfter(Duration.seconds(2))
                    .position(Pos.CENTER);
            notification.showError();

        }

    }

    @FXML
    void btnPush(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/PushProject.fxml"));
        String Commit = idCommit.getText().substring(9), Branch = idBranch.getText().substring(9);

        Stage stage = new Stage();
        try {
            AnchorPane root = (AnchorPane) fxmlLoader.load();
            PushProjectController pushProjectController = fxmlLoader.getController();
            pushProjectController.setNameProject(Owner.NameProject);
            pushProjectController.setFather(this);
            pushProjectController.setNameBranch(Branch);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
//            String Commit = idCommit.getText().substring(9), Branch = idBranch.getText().substring(9);
//            CommonBranch CB = Owner.BranchNames.get(Owner.BranchNames.size() - 1);
//            int ID = CB.way.get(CB.way.size() - 1).Id;
//            CreateCommitSelected(Branch, ID + 1);
        } catch (IOException ex) {
            System.out.println("Error in Load Fxml PushProject: " + ex.getMessage());
        }

    }

    private void openProgressBar() {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/ProgressBar.fxml"));
            AnchorPane root = (AnchorPane) fxmlLoader.load();

            ((ProgressBarController) fxmlLoader.getController()).setStage(stage);
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

        String Commit = idCommit.getText().substring(9), Branch = idBranch.getText().substring(9);
        BranchController branchController = new BranchController(this, ((SendListBranch) respone).getListbranch(), Branch);
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
        String Branch = idBranch.getText().substring(9);
        Command command = new GetListCommits(Owner.NameProject, Branch);
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
    
    
   

    @FXML
    void btnDiffTo(ActionEvent event) {

        String Branch = idBranch.getText().substring(9);
        Command command = new GetListCommits(Owner.NameProject, Branch);
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
        DiffToController DiffToController = new DiffToController(this, ((SendListCommits) respone).getListCommit(), Integer.valueOf(idCommit.getText().charAt(9) - '0'));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/DiffTo.fxml"));
        fxmlLoader.setController(DiffToController);
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
    void btnDisplayDiff(ActionEvent event) {
        List< ViewDiff_folderClass> MyFolderView = current.MyFolderView;

        TabelBrowsers TI = tabelView.getSelectionModel().getSelectedItem();
        if (TI == null || MyFolderView == null) {
            return;
        }
        if (TI.Type) {
            /// not File Do nothing

        } else {
            /**
             * Here Open File in Windows Desktop
             *
             */
            String DirFile1 = null, DirFile2 = null;
            for (int i = 0; i < current.MyFile.size(); i++) {
                if ((current.MyFile.get(i).MyFile.Name).equals(TI.Name.get())) {
                    if (current.MyFile.get(i).MyState == StateType.NoChange || current.MyFile.get(i).MyState == null) {
                        return;
                    }
                    DirFile1 = current.MyFile.get(i).MyFile.Directory;
                    DirFile2 = current.MyFile.get(i).OldFile.Directory;
                    break;
                }
            }
            if (DirFile1 == null) {
                return;
            }
            Command command = new GetDiffFile(DirFile1, DirFile2);
            /**
             * @here must receive Respone : SendDiffFile , inside it must
             * Contain List , every Element one Line from File and State
             *
             */
            try {
                networkOutput.writeObject(command);
                networkOutput.flush();

            } catch (IOException ex) {
                System.out.println("Error in GetFile " + ex.getMessage());
            }

            Respone respone = null;
            try {
                respone = (Respone) networkInput.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Error in FileBrowsers");
            }
            Diff Difference = null;
            if (respone != null && respone.TypeRespone == ResponeType.DONE) {
                Difference = ((SendDiffFile) respone).Difference;
//                for (Changes change : Difference.getChanges()) {
//                    if (change instanceof Insert) {
//                        System.out.print("Insert : ");
//                    }
//                    if (change instanceof Delete) {
//                        System.out.print("Delete : ");
//                    }
//                    if (change instanceof NoChange) {
//                        System.out.print("NoChange : ");
//                    }
//                    if ("".equals(change.getObject().trim())) {
//                        System.out.println("NEW LINE");
//                    } else {
//                        System.out.println(change.getObject());
//                    }
//                }
            }
            if (Difference == null) {
                return;
            }
            String Branch = idBranch.getText().substring(9);
            DisplayDiffController displayDiffController = new DisplayDiffController(this, Difference);
            displayDiffController.setBranchName(Branch);
            displayDiffController.setProjectName(Owner.NameProject);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/DisplayDiff.fxml"));
            fxmlLoader.setController(displayDiffController);
            Stage stage = new Stage();
            try {
                BorderPane root = (BorderPane) fxmlLoader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    void btnMerge(ActionEvent event) {
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

        String Commit = idCommit.getText().substring(9), Branch = idBranch.getText().substring(9);
        BranchMergeController branchController = new BranchMergeController(this, ((SendListBranch) respone).getListbranch(), Branch);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/BranchMerge.fxml"));
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

    public void CreateBranchSelected(String BranchName, int ID) {
        previous.clear();
        current = getViewDiff(GetMyBranch(BranchName));
        if (current != null) {
            ShowFolderWithDiff(current);
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

    public void CreateCommitSelected(String BranchName, int FR_ID, int SC_ID) {
        previous.clear();
        current = GetMyCommitDiff(BranchName, FR_ID, SC_ID);
        // ViewfolderClass CurrentDiff = GetMyCommit(BranchName, ID);

        if (current != null) {
            ShowFolderWithDiff(current);
            //  ShowFolder(CurrentDiff);
            idBranch.setText("Branch : " + BranchName);
            idCommit.setText("Commit : " + FR_ID);
        }
    }

    private ViewDiff_folderClass GetMyCommitDiff(String BranchName, int FR_ID, int SC_ID) {
        /// we will remove First Comment from GUI and this problem will fix
        if (FR_ID == 1) {
            return null;
        }

        Command command = new Get_Diff_Two_Commit(CommandType.GetDiffrent, Owner.NameProject, BranchName, SC_ID, FR_ID);
        try {

            networkOutput.writeObject(command);
            networkOutput.flush();

            Respone respone = (Send_Diff_Two_Commit) networkInput.readObject();

            if (respone.TypeRespone == ResponeType.DONE) {
                return ((Send_Diff_Two_Commit) respone).ob;
            } else {
                return null;
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FileBrowsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /// this Show Folder for Color Folder and File 
    private void ShowFolderWithDiff(ViewDiff_folderClass MyProject) {

        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        tabelView.getItems().clear();
        List< NameAndDirectoryAndState> MyFile = MyProject.MyFile;
        List< NameAndDirectoryAndState> MyFolder = MyProject.MyFolder;
        ObservableList<TabelBrowsers> list;
        int LengthTable = MyFile.size() + MyFolder.size();
        TabelBrowsers[] st = new TabelBrowsers[LengthTable];
        for (int i = 0; i < MyFolder.size(); i++) {
            String s1 = MyFolder.get(i).MyFile.Name;
            st[i] = new TabelBrowsers(s1, true, i, MyFolder.get(i).MyState);
            String Dir = MyFolder.get(i).MyFile.Directory;
            st[i].setDiectoryServer(Dir);  //// need it for open File
            String size = "";
            st[i].setSize(size);
            Date date = new Date(MyFolder.get(i).MyFile.DateModified);
            String DateModified = ft.format(date);
            st[i].setDateModified(DateModified);
        }
        for (int i = 0; i < MyFile.size(); i++) {
            String s1 = MyFile.get(i).MyFile.Name;
            if (MyFile.get(i).OldFile != null) {

            }
            if (s1.equals("BEHKN.BEHKN")) {
                continue;
            }
            st[i + MyFolder.size()] = new TabelBrowsers(s1, false, i + MyFolder.size(), MyFile.get(i).MyState);
            String Dir = MyFile.get(i).MyFile.Directory;
            st[i + MyFolder.size()].setDiectoryServer(Dir); //// need it for open File
            long temp = MyFile.get(i).MyFile.Size / 1024;
            if (MyFile.get(i).MyFile.Size % 1024 != 0) {
                temp++;
            }
            String size = String.valueOf(temp + " KB ");
            st[i + MyFolder.size()].setSize(size);
            Date date = new Date(MyFile.get(i).MyFile.DateModified);
            String DateModified = ft.format(date);
            st[i + MyFolder.size()].setDateModified(DateModified);
        }
        list = FXCollections.observableArrayList(st);
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Size.setCellValueFactory(new PropertyValueFactory<>("Size"));
        DataModified.setCellValueFactory(new PropertyValueFactory<>("DateModified"));
        tabelView.setItems(list);

        /// this for Color The Files
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

    public void CreateFolder(ViewfolderClass ob, String path) {
        List<NameAndDirectory> Folder = ob.MyFolder;
        for (NameAndDirectory u : Folder) {
            String tem = u.Directory;
            Path dir = Paths.get(tem);
            File folder = new File(path + dir.subpath(4, dir.getNameCount())); //// cur from (four slash --> end)
            folder.mkdir();
            for (ViewfolderClass temp : ob.MyFolderView) {
                CreateFolder(temp, path);
            }
        }

    }

    public void CreateFolder(ViewDiff_folderClass ob, String path) {
        List<NameAndDirectoryAndState> Folder = ob.MyFolder;
        for (NameAndDirectoryAndState u : Folder) {
            String tem = u.MyFile.Directory;
            Path dir = Paths.get(tem);
            File folder = new File(path + dir.subpath(4, dir.getNameCount())); //// cur from (four slash --> end)
            folder.mkdir();
            for (ViewDiff_folderClass temp : ob.MyFolderView) {
                CreateFolder(temp, path);
            }
        }

    }

    public void Receive(ViewfolderClass ob, String path) {
        for (NameAndDirectory temp : ob.MyFile) {
            FileOutputStream fos = null;
            try {
                String tem = temp.Directory;
                Path dir = Paths.get(tem);
                fos = new FileOutputStream(path + dir.subpath(4, dir.getNameCount()));
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
            Receive(temp, path);
        }
    }

    public void Receive(ViewDiff_folderClass ob, String path) {
        for (NameAndDirectoryAndState temp : ob.MyFile) {
            FileOutputStream fos = null;
            try {
                String tem = temp.MyFile.Directory;
                Path dir = Paths.get(tem);
                fos = new FileOutputStream(path + dir.subpath(4, dir.getNameCount()));
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
        for (ViewDiff_folderClass temp : ob.MyFolderView) {
            Receive(temp, path);
        }
    }

    @FXML
    void btnInformation(ActionEvent event) {
        List<CommitClass> listCommit = null;
        List<Contributor> listContributor = null;

        /// get listCommit
        String Branch = idBranch.getText().substring(9);
        Command command = new GetListCommits(Owner.NameProject, Branch);
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
        listCommit = ((SendListCommits) respone).getListCommit();
        /// get listContributor
        command = new GetListContributors(Owner.NameProject);
        try {
            networkOutput.writeObject(command);
            networkOutput.flush();
        } catch (IOException ex) {
            System.out.println("Error: btnBranch");
        }
        respone = null;
        try {
            respone = (Respone) networkInput.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Errot in FileBrowsers");
        }
        listContributor = ((SendListContributors) respone).getList();

        /// go to Drow Charts for info
        InformationController InfoController = new InformationController(listCommit, listContributor);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Information.fxml"));
        fxmlLoader.setController(InfoController);
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

}
