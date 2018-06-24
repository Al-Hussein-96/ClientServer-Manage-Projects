package Controller;

import Different.Changes;
import Different.Delete;
import Different.Diff;
import Different.Insert;
import Different.NoChange;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.InlineCssTextArea;
import sun.security.pkcs11.wrapper.Constants;

public class DisplayDiffController implements Initializable {

    Diff Difference;
    FileBrowsersController Father;
    @FXML
    private TextField urlTextField;
    @FXML

    //  private TextArea linesTextArea;
    InlineCssTextArea linesTextArea = new InlineCssTextArea();

    private Future<List<String>> future;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TextFileReader reader = new TextFileReader();

    DisplayDiffController(FileBrowsersController aThis, Diff Difference) {
        this.Father = aThis;
        this.Difference = Difference;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showFileLines();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(DisplayDiffController.class.getName()).log(Level.SEVERE, null, ex);
        }

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

//        future = executorService.submit(new Callable<List<String>>() {
//            public List<String> call() throws Exception {
//                System.out.println("GET");
//                return reader.read(new File(DisplayDiffController.this.urlTextField.getText()));
//            }
//        });
//        List<String> lines = future.get();
//        executorService.shutdownNow();
        linesTextArea.clear();
        linesTextArea.setStyle("-fx-font-size: 1em;");

        for (int i = 0; i < Difference.getChanges().size(); i++) {
            Changes change = Difference.getChanges().get(i);
            if (change instanceof Insert) {
                linesTextArea.appendText(change.getObject() + " " + i);
                linesTextArea.setStyle(i, "-fx-fill: yellow;");
//                        System.out.print("Insert : ");
            }
            if (change instanceof Delete) {
                linesTextArea.appendText(change.getObject() + " " + i);
                linesTextArea.setStyle(i, "-fx-fill: red; -fx-background-color: yellow ;");
//                        System.out.print("Delete : ");
            }
            if (change instanceof NoChange) {
                linesTextArea.appendText(change.getObject() + " " + i);
//                        System.out.print("NoChange : ");
                linesTextArea.setStyle(i, "-fx-fill: black;");
            }
            if (!change.getObject().trim().equals("")) {
                linesTextArea.appendText("\n");
            }
//                    if ("".equals(change.getObject().trim())) {
//                        System.out.println("NEW LINE");
//                    } else {
//                        System.out.println(change.getObject());
//                    }
        }

//        for (String line : lines) {
//            linesTextArea.appendText(line);
//            if (!line.equals(Constants.NEWLINE)) {
//                linesTextArea.appendText("\n");
//            }
//
//        }
        System.out.println(linesTextArea.getText().split("\n").length);
//        linesTextArea.setStyle(5, "-fx-fill: yellow;");
//        linesTextArea.setStyle(6, "-fx-fill: yellow;");
//
//        linesTextArea.setStyle(7, "-fx-fill: yellow;");

    }
}
