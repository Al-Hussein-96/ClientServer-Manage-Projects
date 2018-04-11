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
 * @author Moaz
 */
public class TabelContributor {

    public StringProperty Name;
    public StringProperty NumberOfCommit;

    public TabelContributor(String Name, String NumberOfCommit) {
        this.Name = new SimpleStringProperty(Name);
        this.NumberOfCommit = new SimpleStringProperty(NumberOfCommit);
    }

    public String getName() {
        return Name.get();
    }

    public String getNumberOfCommit() {
        return NumberOfCommit.get();
    }
}
