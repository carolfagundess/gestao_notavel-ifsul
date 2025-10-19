package br.com.gestaonotavel.ifsul.util;

import javafx.scene.control.Alert;

public class AlertUtil {

    private AlertUtil() {
    }

    public static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
