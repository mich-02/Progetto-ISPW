package com.foodie.boundary.components;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.foodie.bean.RicettaBean;
import com.foodie.boundary.InserisciIngredienteViewController;
import com.foodie.boundary.NuovaRicettaViewController;

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
	
	public static void caricaView(ViewInfo viewInfo, RicettaBean ricettaBean) {
	    try {
	        FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(viewInfo.getFxmlPath()));
	        Parent root = loader.load();

	        // Imposta il bean se il controller ha un metodo pubblico setRicettaBean
	        Object controller = loader.getController();
	        try {
	            Method method = controller.getClass().getMethod("setRicettaBean", RicettaBean.class);
	            method.invoke(controller, ricettaBean);
	        } catch (NoSuchMethodException ignored) {
	            // il controller non ha il metodo, va bene
	        }

	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException | IllegalAccessException | InvocationTargetException e) {
	        logger.log(Level.SEVERE, "Errore durante il caricamento della view", e);
	    }
	}
}
