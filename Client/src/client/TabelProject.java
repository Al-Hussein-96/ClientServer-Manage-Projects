package client;

import CommonClass.CommonBranch;
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
        int numCommit = 0;
        for (CommonBranch t : CP.BranchNames) {
            numCommit += t.way.size();
        }

        return this.NameProject.get().equals(String.valueOf(CP.NameProject))
                && this.Author.get().equals(String.valueOf(CP.Author)) && this.NumberOfContributors.get().equals(String.valueOf(CP.Contributors.size()))
                && this.NumberOfCommits.get().equals(String.valueOf(numCommit));
    }
}
