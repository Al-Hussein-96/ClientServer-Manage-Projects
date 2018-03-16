package Controller;

import static client.Project.networkInput;
import static client.Project.networkOutput;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
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

public class LoginController implements Initializable {

    @FXML
    private AnchorPane cardpanal;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton login;
    @FXML
    private JFXButton back;
    @FXML
    private Button close;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        JFXDepthManager.setDepth(cardpanal, 2);
    }

    @FXML
    public void closeth(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    private void login_c(MouseEvent event) {
        String response;
        try {
            networkOutput.println("LOGIN");
            String UserName = username.getText();
            String PassWord = password.getText();
            networkOutput.println(UserName);
            networkOutput.println(PassWord);
            response = networkInput.readLine();
            if (response.equals("Login Done Correct")) {
                //go to the next page              
            }
            System.out.println("\nServer : " + response);
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setContentText(response);
//            alert.show();

        } catch (IOException ex) {
            System.out.println("gfdgdfgdfgdfgdfgdfgdfg");
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

}
