package Controller;

import com.jfoenix.controls.JFXProgressBar;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ProgressBarController implements Initializable {

    @FXML
    private JFXProgressBar progressbar;

    @FXML
    private Label label;

    Task copyWorker;
    
    Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        progressbar.setProgress(0.0);

        copyWorker = createWorker();
        progressbar.progressProperty().unbind();
        progressbar.progressProperty().bind(copyWorker.progressProperty());

        copyWorker.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                label.setText(newValue);
            }
        });
        new Thread(copyWorker).start();
        
        copyWorker.setOnSucceeded(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                stage.hide();
            }
        });

    }

    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(300);
                    updateMessage("Transfer Completed : " + (i+1) * 10 + "%");
                    updateProgress(i + 1, 10);
                }
                
                return true;
            }
        };

    }

}
