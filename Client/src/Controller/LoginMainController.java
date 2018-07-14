package Controller;

import CommonClass.User;
import CommonCommand.Command;
import CommonCommand.GetLOGIN;
import CommonRespone.Respone;
import CommonRespone.ResponeType;
import CommonRespone.SendStatus;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

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

            if (!isValidUserName(UserName)) {
                ShowNotifications("Login", "Invalid UserName");
                return;
            }
            if (!isValidPassword(PassWord)) {
                ShowNotifications("Login", "Invalid Password");
                return;
            }

            User user = new User(UserName, PassWord);

            Command command = new GetLOGIN(user);
            networkOutput.writeObject(command);
            networkOutput.flush();

            Respone respone = (SendStatus) networkInput.readObject();
            if (respone.TypeRespone == ResponeType.DONE) {
                GoToMainPage();
                CheckRememberMe(UserName, PassWord);
                /**
                 * *
                 */
                ShowNotifications("Login", "Done Login");
            } else {
                ////
                ShowNotifications("Login", "Can't Login");
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
