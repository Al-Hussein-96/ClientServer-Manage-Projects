package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController implements Initializable {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton signup;

    @FXML
    private JFXButton login;

    @FXML
    private JFXCheckBox remember;

    @FXML
    private JFXButton forgot;

    @FXML
    private ImageView loading;

    @FXML
    void ClickLogin(ActionEvent event) {
        loading.setVisible(true);

        PauseTransition pt = new PauseTransition(Duration.seconds(3));
        pt.setOnFinished(e -> {
            loading.setVisible(false);
            
            
            /// just code if username is not found in Database and alert Box
            if ("m".equals(username.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setHeaderText(null);
                alert.setContentText("Username Or Password Is Not Correct");
                alert.show();
                loading.setVisible(false);
            }

        });

        pt.play();

    }

    @FXML
    void ClickSignUp(ActionEvent event) {
        login.getScene().getWindow().hide();

        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/SignUp.fxml"));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);

        } catch (IOException ex) {

        }
    }

    @FXML
    void Hello(ActionEvent event) {

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        

        loading.setVisible(false);
        username.setStyle("-fx-text-inner-color: #a0a2ab;");
        password.setStyle("-fx-text-inner-color: #a0a2ab;");

    }

}
