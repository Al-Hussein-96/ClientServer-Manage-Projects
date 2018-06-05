package Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;
import severbenkh.ClientHandler;
import static severbenkh.SeverBENKH.PORT;
import static severbenkh.SeverBENKH.serverSocket;

public class TimerController implements Initializable {

    Timeline timeline;
    Thread thread;
    private int max;
    private int seconds;
    @FXML
    private Label sc1;

    @FXML
    private Label sc2;

    @FXML
    private Label mi1;

    @FXML
    private Label mi2;

    @FXML
    private Label ho1;

    @FXML
    private Label ho2;

    @FXML
    void start(ActionEvent event) {
        timeline.play();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                } catch (IOException ex) {
                    System.out.println("Unable to Connect to Port");
                    System.exit(1);
                }
                do {
                    Socket client;
                    try {
                        client = serverSocket.accept();
                    } catch (IOException ex) {
                        return;
                    }
                    System.out.println("New Client Accepted");
                    ClientHandler handler = new ClientHandler(client);
                    handler.start();
                } while (true);
            }
        });
        thread.start();

    }

    @FXML
    void stop(ActionEvent event) {
        timeline.stop();
        sc1.setText("0");
        sc2.setText("0");
        mi1.setText("0");
        mi2.setText("0");
        ho1.setText("0");
        ho2.setText("0");

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(TimerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            seconds++;
            updateText();
        }));
    }

    private void updateText() {
        int s1 = Integer.valueOf(sc1.getText());
        int s2 = Integer.valueOf(sc2.getText());
        int m1 = Integer.valueOf(mi1.getText());
        int m2 = Integer.valueOf(mi2.getText());
        int h1 = Integer.valueOf(ho1.getText());
        int h2 = Integer.valueOf(ho2.getText());
        s1++;
        if (s1 >= 10) {
            s1 = 0;
            s2++;
        }
        if (s2 >= 6) {
            s1 = s2 = 0;
            m1++;
        }

        if (m1 >= 10) {
            m1 = 0;
            m2++;
        }
        if (m2 >= 6) {
            m1 = m2 = 0;
            h1++;
        }

        if (h1 >= 10) {
            h1 = 0;
            h2++;
        }
        sc1.setText(String.valueOf(s1));
        sc2.setText(String.valueOf(s2));
        mi1.setText(String.valueOf(m1));
        mi2.setText(String.valueOf(m2));
        ho1.setText(String.valueOf(h1));
        ho2.setText(String.valueOf(h2));
    }

}
