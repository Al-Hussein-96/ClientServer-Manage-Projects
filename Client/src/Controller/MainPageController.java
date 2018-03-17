/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static client.Project.networkInput;
import static client.Project.networkOutput;
import client.User;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JFileChooser;

/**
 * FXML Controller class
 *
 * @author Moaz
 */
public class MainPageController implements Initializable {

    private User Owner;

    @FXML
    private AnchorPane cardpanal;
    @FXML
    private JFXButton StartProject;
    @FXML
    private JFXButton AllProjects;
    @FXML
    private JFXButton MyProjects;
    @FXML
    private JFXButton SignOut;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    public void setOwner(User Owner) {
        this.Owner = Owner;
    }

    @FXML
    private void Close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void Startproject(MouseEvent event) {
        String response;
        try {
            networkOutput.println("STARTPROJECT");
            response = networkInput.readLine();
            if (response.equals("Done")) {
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                f.showSaveDialog(null);
                String path = f.getSelectedFile().toString();
                System.out.println(path);
                networkOutput.println(this.Owner.getName());
                networkOutput.println("Mohammad"); /// this is not complete we need project name that Client choose it
                networkOutput.println(path);
            }
            System.out.println("\nServer : " + response);

        } catch (IOException ex) {
            System.out.println("Error START PROJECT");
        }
    }

    @FXML
    private void Allprojects(MouseEvent event) {
        String response;
        try {
            networkOutput.println("ALLPROJECTS");
            response = networkInput.readLine();
            if (response.equals("Done")) {

            }
            System.out.println("\nServer : " + response);
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setContentText(response);
//            alert.show();

        } catch (IOException ex) {
            System.out.println("Error IN GIT ALL PROJECTS");
        }
    }

    @FXML
    private void Myprojects(MouseEvent event) {
        String response;
        try {
            networkOutput.println("MYPROJECTS");
            response = networkInput.readLine();
            if (response.equals("Done")) {

            }
            System.out.println("\nServer : " + response);
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setContentText(response);
//            alert.show();

        } catch (IOException ex) {
            System.out.println("Error IN GIT MY PROJECTS");
        }
    }

    @FXML
    private void Signout(MouseEvent event) {
        try {
            SignOut.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Wind_select.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
