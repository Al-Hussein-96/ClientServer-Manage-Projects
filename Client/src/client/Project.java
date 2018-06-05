package client;

import CommonClass.User;
import CommonCommand.Command;
import CommonCommand.GetLOGIN;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import Controller.LoginMainController;
import Controller.PageMainController;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.Notifications;

public class Project extends Application {

    public static InetAddress host;
    public static final int PORT = 4321;
    public static final int PORT1 = 4322;
    public static Socket socket;
    public static ObjectInputStream networkInput;
    public static ObjectOutputStream networkOutput;

    @Override
    public void start(Stage stage) throws Exception {
        if (!CheckRememberMe(stage)) {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/wel.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);

            stage.show();
            stage.setResizable(false);
        }
    }

    public static void main(String[] args) throws IOException {

        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println("\nHost ID not foun!");
            System.exit(1);
        }
        socket = new Socket(host, PORT);
        networkOutput = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        networkInput = new ObjectInputStream(socket.getInputStream());

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
