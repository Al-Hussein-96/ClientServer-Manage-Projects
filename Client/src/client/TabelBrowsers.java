package client;

import CommonClass.StateType;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class TabelBrowsers {

    public int id;
    public boolean Type;
    public ImageView ImageIcon;
    public SimpleStringProperty Name;
    public SimpleStringProperty DateModified;
    public SimpleStringProperty Size;
    public SimpleStringProperty Description;
    public String DiectoryServer;
    public StateType State;

    public TabelBrowsers(String Name, boolean type, int id,StateType State) {
        this.Name = new SimpleStringProperty(Name);
        this.Type = type;
        this.id = id;
        this.DateModified = new SimpleStringProperty("");
        this.Size = new SimpleStringProperty("");
        this.Description = new SimpleStringProperty("");
        this.DiectoryServer = new String("");
        this.State = State;

        //     this.Description = new SimpleStringProperty(Description);
    }

    public String getName() {
        return Name.get();
    }

    public String getDateModified() {
        return DateModified.get();
    }

    public String getSize() {
        return Size.get();
    }

    public String getDescription() {
        return Description.get();
    }

    public void setDiectoryServer(String DiectoryServer) {
        this.DiectoryServer = DiectoryServer;
    }

    public void setSize(String size) {
         this.Size = new SimpleStringProperty(size);
    }
    
    public void setDateModified(String dateModified) {
         this.DateModified = new SimpleStringProperty(dateModified);
    }

    public StateType getState() {
        return State;
    }
    
}
