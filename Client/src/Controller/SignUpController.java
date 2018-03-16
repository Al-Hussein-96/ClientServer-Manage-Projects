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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
//import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class SignUpController implements Initializable {

    @FXML
    private AnchorPane cardpanal;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXPasswordField password_confirmation;

    @FXML
    private JFXButton signup;

    @FXML
    void btnSignUp(ActionEvent event) {
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(cardpanal, 2);
    }

    public void closeth(MouseEvent event) {
        Platform.exit();
    }

}
