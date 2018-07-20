package Controller;

import CommonClass.User;
import CommonCommand.Command;
import CommonCommand.GetSIGNUP;
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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
//import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

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

    @FXML
    private JFXTextField email;

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
        try {
            String UserName = username.getText();
            String PassWord = password.getText();
            String Password_Confirmation = password_confirmation.getText();
            String Email = email.getText();
            if (!PassWord.equals(Password_Confirmation)) {
                System.out.println("Mismatch");
                ShowNotifications("Sign Up", "Mismatch");
                return;
            }
            if (!isValidUserName(UserName) && false) {
                ShowNotifications("Sign Up", "Invalid UserName");
                return;
            }
            if (!isValidPassword(PassWord) && false) {
                ShowNotifications("Sign Up", "Invalid Password");
                return;
            }
            

            User user = new User(UserName, PassWord);
            user.setEmail(Email);
            user.setDateCreate(new Date());
            Command command = new GetSIGNUP(user);
            networkOutput.writeObject(command);
            networkOutput.flush();

            Respone response = (Respone) networkInput.readObject();
            if (response.TypeRespone == ResponeType.DONE) {
                GoToMainPage();
                CheckRememberMe(UserName, PassWord);
            }
        } catch (IOException ex) {
            System.out.println("Error SIGN UP");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
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
            PageMainController mainPageController = fxmlLoader.getController();
            mainPageController.setOwner(new User(username.getText(), password.getText()));
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
            File F = new File("Data\\temp");
            ObjectOutputStream output = null;
            User u = new User(UserName, PassWord);
            try {
                output = new ObjectOutputStream(new FileOutputStream(F));
                output.writeObject(u);
                output.flush();
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(LoginMainController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            File F = new File("Data\\temp");
            F.delete();
        }
    }
    
    // check if UserName Valid :
    // Have at least 3 character
    // First one is char
    public boolean isValidUserName(String UserName) {
        Pattern ptn = Pattern.compile("^[a-zA-Z][a-zA-Z0-9\\-._]{2,}$");
        Matcher match = ptn.matcher(UserName);
        return match.find();
    }

    //    (?=.*[0-9]) a digit must occur at least once
    //    (?=.*[a-z]) a lower case letter must occur at least once
    //    (?=.*[A-Z]) an upper case letter must occur at least once
    //    (?=.*[@#$%^&+=]) a special character must occur at least once
    //    (?=\\S+$) no whitespace allowed in the entire string
    //    .{8,} at least 8 characters
    public boolean isValidPassword(String Password) {
        //  Pattern ptn = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        Pattern ptn = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{5,}$");
        Matcher match = ptn.matcher(Password);
        return match.find();
    }

    public void ShowNotifications(String title, String text) {
        Notifications notification = Notifications.create()
                .title(title)
                .text(text)
                .graphic(null)
                .hideAfter(Duration.seconds(2))
                .position(Pos.CENTER);
        notification.showConfirm();
    }

}
