package client;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class TabelBrowsers {

    public int id;
    public boolean Type;
    public ImageView ImageIcon;
    public SimpleStringProperty Name;
    public SimpleStringProperty DataModified;
    public SimpleStringProperty Size;
    public SimpleStringProperty Description;
    public String DiectoryServer;

    public TabelBrowsers(String Name, boolean type, int id) {
        this.Name = new SimpleStringProperty(Name);
        this.Type = type;
        this.id = id;
        this.DataModified = new SimpleStringProperty("");
        this.Size = new SimpleStringProperty("");
        this.Description = new SimpleStringProperty("");
        this.DiectoryServer = new String("");

        //     this.Description = new SimpleStringProperty(Description);
    }

    public String getName() {
        return Name.get();
    }

    public String getDataModified() {
        return DataModified.get();
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

}
