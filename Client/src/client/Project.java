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
import java.awt.Component;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

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

    public static boolean isValidIP(String ipAddr) {
        Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher mtch = ptn.matcher(ipAddr);
        return mtch.find();
    }

    public static void main(String[] args) throws IOException {

//        String input;
//        while (true) {
//            input = JOptionPane.showInputDialog(new Component() {
//            }, "Enter IP : ", "IP Server", -1);
//            System.out.println(input);
//            if (isValidIP(input) || input.equals("1")) {
//                break;
//            }
//        }
//        if (input.equals("1")) {
//            try {
//                host = InetAddress.getLocalHost();
//            } catch (UnknownHostException ex) {
//                System.out.println("\nHost ID not foun!");
//                System.exit(1);
//            }
//        }
//        else host = InetAddress.getByName(input);
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println("\nHost ID not foun!");
            System.exit(1);
        }
        System.out.println(host);
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
        File F = new File("Data\\temp");
        if (F.exists()) {
            ObjectInputStream input = null;
            User u = null;
            try {
                input = new ObjectInputStream(new FileInputStream(F));
                u = (User) input.readObject();
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(LoginMainController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            }

            String UserName = u.getName();
            String PassWord = u.getPassword();

            User user = new User(UserName, PassWord);

            Command command = new GetLOGIN(user);
            try {
                networkOutput.writeObject(command);
                networkOutput.flush();
                Respone respone = (Respone) networkInput.readObject();

                if (respone.TypeRespone == ResponeType.DONE) {
                    GoToMainPage(new User(UserName, PassWord), stage);
                }
            } catch (IOException ex) {
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
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
