package client;

import CommonClass.CommonProject;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TabelProject extends RecursiveTreeObject<TabelProject> {

    public StringProperty NameProject;
    public StringProperty DateCreate;
    public StringProperty Author;
    public StringProperty NumberOfContributors;
    public StringProperty NumberOfCommits;

    public TabelProject(String NameProject, String DateCreate, String Author, String NumberOfContributors, String NumberOfCommits) {
        this.NameProject = new SimpleStringProperty(NameProject);
        this.DateCreate = new SimpleStringProperty(DateCreate);
        this.Author = new SimpleStringProperty(Author);
        this.NumberOfContributors = new SimpleStringProperty(NumberOfContributors);
        this.NumberOfCommits = new SimpleStringProperty(NumberOfCommits);
    }

    public boolean equal(CommonProject CP) {
        if (this.NameProject.equals(CP.NameProject) && this.DateCreate.equals(CP.DateCreate)
                && this.Author.equals(CP.Author) && this.NumberOfContributors.equals(CP.Contributors.size())
                && this.NumberOfCommits.equals(CP.way.size())) {
            return true;
        }
        return false;
    }
}
