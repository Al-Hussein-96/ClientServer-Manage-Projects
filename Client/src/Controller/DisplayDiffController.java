package Controller;

import Different.Changes;
import Different.Delete;
import Different.Diff;
import Different.Insert;
import Different.NoChange;
import client.LineCode;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;

public class DisplayDiffController implements Initializable {

    Diff Difference;
    FileBrowsersController Father;
    String BranchName;
    String ProjectName;

    @FXML
    private Label ProjectNameLabel;

    @FXML
    private Label BranchNameLabel;

    @FXML
    private TableView<LineCode> table;

    @FXML
    private TableColumn<LineCode, String> OldRow;

    @FXML
    private TableColumn<LineCode, String> NewRow;

    @FXML
    private TableColumn<LineCode, String> state;

    @FXML
    private TableColumn<LineCode, String> text;

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

        text.setCellFactory((param) -> new TableCell<LineCode, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                //  super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    String temp = item;
                    setText(temp);

                    int rowIndex = getTableRow().getIndex();
                    String valueInSecondaryCell = getTableView().getItems().get(rowIndex).getState();
                    if (valueInSecondaryCell.equals("+")) {
                        setStyle("-fx-background-color: #7FFFD4; -fx-border-color: #7FFFD4;"); //Set the style in the first cell based on the value of the second cell
                    } else if (valueInSecondaryCell.equals("-")) {
                        setStyle("-fx-background-color: #FFC0CB; -fx-border-color: #FFC0CB;"); //Set the style in the first cell based on the value of the second cell
                    } else if (valueInSecondaryCell.equals("o")) {
                        setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #FFFFFF;"); //Set the style in the first cell based on the value of the second cell
                    } else {
                        setStyle("-fx-background-color: #71dd71; -fx-border-color: black;");
                    }

                }
            }

        });

        BranchNameLabel.setText("Branch Name : " + BranchName);
        ProjectNameLabel.setText("Project Name : " + ProjectName);

    }

//D:\My Project\DisplayText\2.fxml
    @FXML
    @SuppressWarnings("NestedAssignment")
    public void showFileLines() throws InterruptedException, ExecutionException {

        int oldRow = 0, newRow = 0;
        ObservableList<LineCode> list;
        LineCode[] st = new LineCode[Difference.getChanges().size()];

        for (int i = 0; i < Difference.getChanges().size(); i++) {
            Changes change = Difference.getChanges().get(i);
            if (change instanceof Insert) {
                newRow++;
                st[i] = new LineCode("", String.valueOf(newRow), "+", change.getObject());
            }
            if (change instanceof Delete) {
                oldRow++;
                st[i] = new LineCode(String.valueOf(oldRow), "", "-", change.getObject());
            }
            if (change instanceof NoChange) {
                oldRow++;
                newRow++;
                st[i] = new LineCode(String.valueOf(oldRow), String.valueOf(newRow), "o", change.getObject());
            }
            if (!change.getObject().trim().equals("")) {
            }

        }
        OldRow.setStyle("-fx-alignment: CENTER");
        NewRow.setStyle("-fx-alignment: CENTER");
        state.setStyle("-fx-alignment: CENTER");

        //  text.setStyle("-fx-background-color: yellow;");
        // text.setStyle("-fx-background-color:  #111111;");
        list = FXCollections.observableArrayList(st);
        OldRow.setCellValueFactory(new PropertyValueFactory<>("OldRow"));
        NewRow.setCellValueFactory(new PropertyValueFactory<>("NewRow"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        text.setCellValueFactory(new PropertyValueFactory<>("text"));

        table.setItems(list);

    }

    public void setBranchName(String BranchName) {
        this.BranchName = BranchName;
    }

    public void setProjectName(String ProjectName) {
        this.ProjectName = ProjectName;
    }
}
