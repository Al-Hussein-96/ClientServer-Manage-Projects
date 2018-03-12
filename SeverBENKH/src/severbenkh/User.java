package severbenkh;

import java.io.Serializable;

public class User implements Serializable {

    private String Name;
    private String Password;
    private int Id;

    public User(String Name, String Password) {
        this.Name = Name;
        this.Password = Password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

}
