package com.foodie.boundary.components;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ViewLoader {
	private static final Logger logger = Logger.getLogger(ViewLoader.class.getName());
	private static Stage stage;

	private ViewLoader() {
	}

	public static void setStage(Stage primaryStage) {
		if (stage == null) {
			stage = primaryStage;
		} else {
			logger.warning("Stage già impostato");
		}
	}
	
	public static FXMLLoader caricaView(ViewInfo viewInfo) {

		try {
			FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(viewInfo.getFxmlPath()));
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.show();
			return loader;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Errore durante il caricamento della view", e);
			return null;
		}
	}
}
