package Controller;

import CommonClass.User;
import CommonCommand.Command;
import CommonCommand.LOGIN;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import static client.Project.networkInput;
import static client.Project.networkOutput;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginMainController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton login;

    @FXML
    private AnchorPane cardpanal;
    @FXML
    private JFXCheckBox RememberMe;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //     CheckRememberMe();
    }

    @FXML
    void Close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void login_c(MouseEvent event) {
        try {
            String UserName = username.getText();
            String PassWord = password.getText();

            User user = new User(UserName, PassWord);

            Command command = new LOGIN(user);
            networkOutput.writeObject(command);
            networkOutput.flush();
            Respone respone = (Respone) networkInput.readObject();

            if (respone.TypeRespone == ResponeType.DONE) {
                GoToMainPage();
                CheckRememberMe(UserName, PassWord);
            }

        } catch (IOException ex) {
            System.out.println("Error LOGIN");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void Back(MouseEvent event) {

        try {
            login.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/WindowsSelect.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(WindowsSelectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void GoToMainPage() {
        try {
            username.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/PageMain.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            PageMainController mainPageController = fxmlLoader.getController();
            mainPageController.setOwner(new User(username.getText(), password.getText()));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException ex) {
            System.err.println("error : " + ex.getMessage());
        }
    }

    void CheckRememberMe(String UserName, String PassWord) {
        File f = new File("Data");
        if (!f.exists()) {
            f.mkdir();
        }
        if (RememberMe.isSelected()) {
            File F = new File("Data\\temp.txt");
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new FileWriter(F));
                writer.write(UserName);
                writer.newLine();
                writer.write(PassWord);
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(LoginMainController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            File F = new File("Data\\temp.txt");
            F.delete();
        }
    }

}
