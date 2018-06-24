
package Controller;

import client.TextFileReader;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.InlineCssTextArea;
import sun.security.pkcs11.wrapper.Constants;

public class DisplayDiffController implements Initializable {

    @FXML
    private TextField urlTextField;
    @FXML

    //  private TextArea linesTextArea;
    InlineCssTextArea linesTextArea = new InlineCssTextArea();

    private Future<List<String>> future;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TextFileReader reader = new TextFileReader();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void choose(ActionEvent event) {
        System.out.println("FOOOO");
        FileChooser dc = new FileChooser();
        dc.showOpenDialog(null);
        File selectedFile = dc.getInitialDirectory();

        if (selectedFile != null) {
            urlTextField.setText(selectedFile.getPath());
        }
    }

//D:\My Project\DisplayText\2.fxml
    @FXML
    @SuppressWarnings("NestedAssignment")
    public void showFileLines() throws InterruptedException, ExecutionException {

        future = executorService.submit(new Callable<List<String>>() {
            public List<String> call() throws Exception {
                System.out.println("GET");
                return reader.read(new File(DisplayDiffController.this.urlTextField.getText()));
            }
        });
        List<String> lines = future.get();
        executorService.shutdownNow();
        linesTextArea.clear();
        linesTextArea.setStyle("-fx-font-size: 2em;");
        for (String line : lines) {
            linesTextArea.appendText(line);
            if (!line.equals(Constants.NEWLINE)) {
                linesTextArea.appendText("\n");
            }

        }
        System.out.println(linesTextArea.getText().split("\n").length);
        linesTextArea.setStyle(5, "-fx-fill: yellow;");
        linesTextArea.setStyle(6, "-fx-fill: yellow;");

        linesTextArea.setStyle(7, "-fx-fill: yellow;");

    }
}
