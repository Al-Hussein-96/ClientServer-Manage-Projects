/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Text;

/**
 *
 * @author Al-Hussein
 */
public class LineCode {
    
    StringProperty OldRow;
    StringProperty NewRow;
    StringProperty state;
   // TextFieldProperty
    StringProperty text;

    public LineCode(String OldRow,String NewRow, String state, String text) {
        this.OldRow = new SimpleStringProperty(OldRow);
        this.NewRow = new SimpleStringProperty(NewRow);
        this.state = new SimpleStringProperty(state);
        this.text = new SimpleStringProperty(text);
      //  this.text.setStyle("-fx-fill: yellow;");
    }

    public String getNewRow() {
        return NewRow.get();
    }

    public void setNewRow(SimpleStringProperty line) {
        this.NewRow = line;
    }
    
    public String getOldRow() {
        return OldRow.get();
    }

    public void setOldRow(SimpleStringProperty line) {
        this.OldRow = line;
    }

    public String getState() {
        return state.get();
    }

    public void setState(SimpleStringProperty state) {
        this.state = state;
    }

    public String getText() {
        return text.get();
    }

    public void setText(SimpleStringProperty text) {
        this.text = text;
    }
    
    
    
    
    
}
