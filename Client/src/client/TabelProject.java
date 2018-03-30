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
        System.out.println(this.NameProject.get() + " : " + CP.NameProject);
        System.out.println(this.DateCreate.get() + " : " + CP.DateCreate);
        System.out.println(this.Author.get() + " : " + CP.Author);
        System.out.println(this.NumberOfContributors.get() + " : " + CP.Contributors.size());
        System.out.println(this.NumberOfCommits.get() + " : " + CP.way.size());

        if (this.NameProject.get().equals(String.valueOf(CP.NameProject))
                && this.Author.get().equals(String.valueOf(CP.Author)) && this.NumberOfContributors.get().equals(String.valueOf(CP.Contributors.size()))
                && this.NumberOfCommits.get().equals(String.valueOf(CP.way.size()))) {
            return true;
        }
        return false;
    }
}
