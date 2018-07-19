package client;

import CommonClass.CommonProject;
import CommonClass.ProjectToUpload;
import CommonClass.ResourceManager;
import CommonClass.User;
import CommonCommand.Command;
import CommonCommand.GetLOGIN;
import CommonCommand.GetMyProject;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import CommonRespone.SendMyProject;
import Controller.FileBrowsersController;
import Controller.LoginMainController;
import Controller.PageMainController;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Project extends Application {

    static boolean GUI = false;
    static ProjectToUpload benkh;
    public static InetAddress host;
    public static final int PORT = 4321;
    public static final int PORT1 = 4322;
    public static Socket socket;
    public static ObjectInputStream networkInput;
    public static ObjectOutputStream networkOutput;

    private static ProjectToUpload getBenkh(String arg) {
        File folder = new File(arg + "/");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].getName().equals("BEHKN.BEHKN")) {
                try {
                    ProjectToUpload benkh = (ProjectToUpload) ResourceManager.load(listOfFiles[i].getPath());
                    return benkh;
                } catch (Exception ex) {
                    Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    @Override
    public void start(Stage stage) throws Exception {
        if (!GUI) {
            if (!CheckRememberMe(stage)) {
                Parent root = FXMLLoader.load(getClass().getResource("/FXML/wel.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);

                stage.show();
                stage.setResizable(false);
            }
        } else {
            try {
                String UserName = benkh.LocalUserName;
                String PassWord = benkh.LocalUserPassword;
                User user = new User(UserName, PassWord);

                Command command = new GetLOGIN(user);
                networkOutput.writeObject(command);
                networkOutput.flush();

                Respone respone = (Respone) networkInput.readObject();

                if (respone.TypeRespone == ResponeType.DONE) {
                    List<CommonProject> list = GetMyProject();
                    CommonProject tmp = null;
                    for (CommonProject u : list) {
                        if (u.NameProject.equals(benkh.ProjectName)) {
                            tmp = u;
                            break;
                        }
                    }
                    FileBrowsersController fileBrowsersController = new FileBrowsersController(tmp, user);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/FileBrowsers.fxml"));

                    fxmlLoader.setController(fileBrowsersController);
                    AnchorPane root = null;
                    try {
                        root = (AnchorPane) fxmlLoader.load();
                    } catch (IOException ex) {
                        System.out.println("Error:::: " + ex.getMessage());
                    }
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.TRANSPARENT);

                    stage.show();
                    stage.setResizable(false);
                    //    System.out.println("Bug is initlize in FileBrowsersController: " + roopane + " : " + root);

                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private static List<CommonProject> GetMyProject() {

        try {
            networkOutput.writeObject(new GetMyProject());
            networkOutput.flush();

        } catch (IOException ex) {
            System.out.println("Error in function : GetMyProject  Class: MyProjectController  : " + ex.getMessage());
            return null;
        }
        try {
            Respone respone = (Respone) networkInput.readObject();
            if (respone.TypeRespone == ResponeType.DONE) {
                return ((SendMyProject) respone).getMylist();
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error3 in function : GetMyProject  Class: MyProjectController  : " + ex.getMessage());
        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(args[0]);
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println("\nHost ID not foun!");
            System.exit(1);
        }
        socket = new Socket(host, PORT);
        networkOutput = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        networkInput = new ObjectInputStream(socket.getInputStream());

//        if (args.length > 0) {
//            benkh = getBenkh(args[0]);
//            if (benkh != null) {
//                GUI = true;
//            }
//
//        }
        launch(args);
        //sendMessage();
    }

    boolean CheckRememberMe(Stage stage) {
        boolean bo = false;
        File f = new File("Data");
        if (!f.exists()) {
            f.mkdir();
        }
        File F = new File("Data\\temp.txt");
        if (F.exists()) {
            BufferedReader reader = null;
            try {

                reader = new BufferedReader(new FileReader(F));
                String UserName = reader.readLine();
                String PassWord = reader.readLine();

                User user = new User(UserName, PassWord);

                Command command = new GetLOGIN(user);
                networkOutput.writeObject(command);
                networkOutput.flush();
                Respone respone = (Respone) networkInput.readObject();

                if (respone.TypeRespone == ResponeType.DONE) {
                    GoToMainPage(new User(UserName, PassWord), stage);
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(LoginMainController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(LoginMainController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(LoginMainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            bo = true;
        }
        return bo;
    }

    private void GoToMainPage(User user, Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/PageMain.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            PageMainController mainPageController = fxmlLoader.getController();
            mainPageController.setOwner(user);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
            stage.setResizable(false);
        } catch (IOException ex) {
            System.err.println("error : " + ex.getMessage());
        }
    }

}
