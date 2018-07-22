/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Al-Hussein
 */
public class Notification {

    public static void Notification(String title, String text) {
        Notifications notification = Notifications.create()
                .title(title)
                .text(text)
                .graphic(null)
                .hideAfter(Duration.seconds(2))
                .position(Pos.CENTER);
        notification.showConfirm();
    }
}
