/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Al-Hussein
 */
public class TabelUsers {
    public StringProperty UserName;

    public TabelUsers(String UserName) {
        this.UserName = new SimpleStringProperty(UserName);
    }

    public String getUserName() {
        return UserName.get();
    }
    
    
    
}
