package com.foodie.boundary.components;

import javafx.scene.control.Alert;

public class AlertUtils {
	private AlertUtils() {
        // Costruttore privato per impedire l'istanziazione
    }

    public static void showAlert(Alert.AlertType alertType, String titolo, String contenuto) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titolo);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }

}
