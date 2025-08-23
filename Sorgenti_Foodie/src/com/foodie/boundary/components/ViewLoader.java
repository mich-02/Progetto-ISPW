package com.foodie.boundary.components;

import java.io.IOException;
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

	/*
	public static Object caricaView(ViewInfo viewInfo) {

		try {
			FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(viewInfo.getFxmlPath()));
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			//stage.setTitle(viewInfo.getTitle());
			stage.show();
		} catch (IOException e) {
			logger.severe("Errore durante il caricamento della view: " + e.getMessage());
			return null;
		}
	}
	*/
	
	public static FXMLLoader caricaView(ViewInfo viewInfo) {

		try {
			FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(viewInfo.getFxmlPath()));
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			//stage.setTitle(viewInfo.getTitle());
			stage.show();
			return loader;
		} catch (IOException e) {
			logger.severe("Errore durante il caricamento della view: " + e.getMessage());
			return null;
		}
	}
}
