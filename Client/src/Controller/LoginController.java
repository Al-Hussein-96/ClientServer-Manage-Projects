package Controller;

import com.jfoenix.effects.JFXDepthManager;
import com.sun.jndi.dns.DnsContextFactory;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Wasem
 */
public class LoginController implements Initializable {

    private Label label;
    @FXML
    private AnchorPane cardpanal;
    @FXML
    private MaterialDesignIconView closee;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(cardpanal, 2);
    }

    @FXML
    private void closeth(MouseEvent event) {
        Platform.exit();
    }

}
