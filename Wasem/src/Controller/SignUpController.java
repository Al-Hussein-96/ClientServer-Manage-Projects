package Controller;

import static client.Project.networkInput;
import static client.Project.networkOutput;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
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
    private JFXButton back;
    @FXML
    private Button close;

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
            networkOutput.println("SIGNUP");
            String UserName = username.getText();
            String PassWord = password.getText();
            String Password_Confirmation = password_confirmation.getText();
            if (!PassWord.equals(Password_Confirmation)) {
                System.out.println("Mismatch");
                return;
            }
            networkOutput.println(UserName);
            networkOutput.println(PassWord);
            response = networkInput.readLine();
            if (response.equals("Server Agree on username")) {
                GoToMainPage();
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Wind_select.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Wind_selectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void GoToMainPage() {
        try {
            back.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/MainPage.fxml"));
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
