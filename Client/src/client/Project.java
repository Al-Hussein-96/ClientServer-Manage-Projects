package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Project extends Application {

    private static InetAddress host;
    private static final int PORT = 4321;
    public static Socket socket;
    public static BufferedReader networkInput;
    public static PrintWriter networkOutput;

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/FXML/WindowsSelect.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);

        stage.show();

        stage.setResizable(false);
    }

    public static void main(String[] args) throws IOException {
//        try {
//            host = InetAddress.getLocalHost();
//        } catch (UnknownHostException ex) {
//            System.out.println("\nHost ID not foun!");
//            System.exit(1);
//        }
//        socket = new Socket(host, PORT);
//        networkInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        networkOutput = new PrintWriter(socket.getOutputStream(), true);

        launch(args);

        //sendMessage();
    }

}
