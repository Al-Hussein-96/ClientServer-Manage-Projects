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
    
    StringProperty line;
    StringProperty state;
   // TextFieldProperty
    StringProperty text;

    public LineCode(String line, String state, String text) {
        this.line = new SimpleStringProperty(line);
        this.state = new SimpleStringProperty(state);
        this.text = new SimpleStringProperty(text);
      //  this.text.setStyle("-fx-fill: yellow;");
    }

    public String getLine() {
        return line.get();
    }

    public void setLine(SimpleStringProperty line) {
        this.line = line;
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
