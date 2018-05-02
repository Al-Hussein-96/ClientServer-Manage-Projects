package CommonClass;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private String Name;
    private String Password;
    private int Id;
    String Email;
    Date DateCreate;

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

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setDateCreate(Date DateCreate) {
        this.DateCreate = DateCreate;
    }

    public String getEmail() {
        return Email;
    }

    public Date getDateCreate() {
        return DateCreate;
    }
    
    

}
