package Controller;

import static client.Project.networkInput;
import static client.Project.networkOutput;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
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
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
//import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SignUpController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXPasswordField password_confirmation;
    @FXML
    private JFXButton done;
    @FXML
    private ImageView back;
    @FXML
    private JFXButton close;
    @FXML
    private JFXCheckBox RememberMe;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //       JFXDepthManager.setDepth(cardpanal, 2);
    }

    @FXML
    void Close(ActionEvent event) {
        Platform.exit();

    }

    @FXML
    private void create_clc(MouseEvent event) {
        String response;
        try {
            networkOutput.writeUTF("SIGNUP");
            networkOutput.flush();
            String UserName = username.getText();
            String PassWord = password.getText();
            String Password_Confirmation = password_confirmation.getText();
            if (!PassWord.equals(Password_Confirmation)) {
                System.out.println("Mismatch");
                return;
            }
            networkOutput.writeUTF(UserName);
            networkOutput.flush();
            networkOutput.writeUTF(PassWord);
            networkOutput.flush();
            response = networkInput.readUTF();
            if (response.equals("Server Agree on username")) {
                GoToMainPage();
                CheckRememberMe(UserName, PassWord);
            }
            System.out.println("\nServer : " + response);
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setContentText(response);
//            alert.show();
        } catch (IOException ex) {
            System.out.println("Error SIGN UP");
        }
    }

    @FXML
    private void Back(MouseEvent event) {
        try {
            back.getScene().getWindow().hide();
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
            back.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/PageMain.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginMainController.class.getName()).log(Level.SEVERE, null, ex);
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
