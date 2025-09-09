package com.foodie.applicazione;

import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);  // non ridimensionabile
		ViewLoader.setStage(primaryStage);
		ViewLoader.caricaView(ViewInfo.CONFIGURAZIONE);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
