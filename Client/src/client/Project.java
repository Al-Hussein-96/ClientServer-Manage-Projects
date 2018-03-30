package client;

import Controller.LoginMainController;
import Controller.PageMainController;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

public class Project extends Application {

    public static InetAddress host;
    public static final int PORT = 4321;
    public static final int PORT1 = 4322;
    public static Socket socket;
    public static DataInputStream networkInput;
    public static DataOutputStream networkOutput;
//    public static BufferedReader networkInput;
//    public static PrintWriter networkOutput;

    @Override
    public void start(Stage stage) throws Exception {
       if (!CheckRememberMe(stage))
        {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/WindowsSelect.fxml"));
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
        networkInput = new DataInputStream(socket.getInputStream());
        networkOutput = new DataOutputStream(socket.getOutputStream());

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
                networkOutput.writeUTF("LOGIN");

                reader = new BufferedReader(new FileReader(F));
                String UserName = reader.readLine();
                String PassWord = reader.readLine();
                System.out.println(UserName + "  " + PassWord);

                networkOutput.writeUTF(UserName);
                networkOutput.writeUTF(PassWord);
                String response = networkInput.readUTF();
                System.out.println(response);
                if (response.equals("Login Done Correct")) {
                    GoToMainPage(new User(UserName, PassWord), stage);
                }
                System.out.println("\nServer : " + response);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LoginMainController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(LoginMainController.class.getName()).log(Level.SEVERE, null, ex);
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
