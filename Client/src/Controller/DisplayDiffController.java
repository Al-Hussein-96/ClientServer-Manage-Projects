package Controller;

import Different.Changes;
import Different.Delete;
import Different.Diff;
import Different.Insert;
import Different.NoChange;
import client.LineCode;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DisplayDiffController implements Initializable {
    
    Diff Difference;
    FileBrowsersController Father;
    @FXML
    private TableView<LineCode> table;
    
    @FXML
    private TableColumn<LineCode, String> line;
    
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
                        setStyle("-fx-background-color: #bec8d6; -fx-border-color: black;"); //Set the style in the first cell based on the value of the second cell
                    } else {
                        setStyle("-fx-background-color: #71dd71; -fx-border-color: black;");
                    }
                    
                }
            }
            
        });
        
    }

//D:\My Project\DisplayText\2.fxml
    @FXML
    @SuppressWarnings("NestedAssignment")
    public void showFileLines() throws InterruptedException, ExecutionException {
        
        ObservableList<LineCode> list;
        LineCode[] st = new LineCode[Difference.getChanges().size()];
        
        for (int i = 0; i < Difference.getChanges().size(); i++) {
            Changes change = Difference.getChanges().get(i);
            if (change instanceof Insert) {
                st[i] = new LineCode(String.valueOf(i + 1), "+", change.getObject());

//                        System.out.print("Insert : ");
            }
            if (change instanceof Delete) {
                st[i] = new LineCode(String.valueOf(i + 1), "-", change.getObject());
//                        System.out.print("Delete : ");
            }
            if (change instanceof NoChange) {
//                        System.out.print("NoChange : ");
                st[i] = new LineCode(String.valueOf(i + 1), "o", change.getObject());
            }
            if (!change.getObject().trim().equals("")) {
            }
            
        }
        line.setStyle("-fx-alignment: CENTER");
        state.setStyle("-fx-alignment: CENTER");

        //  text.setStyle("-fx-background-color: yellow;");
        // text.setStyle("-fx-background-color:  #111111;");
        list = FXCollections.observableArrayList(st);
        line.setCellValueFactory(new PropertyValueFactory<>("line"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        text.setCellValueFactory(new PropertyValueFactory<>("text"));
        
        table.setItems(list);
        
    }
}
