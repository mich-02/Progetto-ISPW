package com.foodie.applicazione;



import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Client extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//FXMLLoader loader= new FXMLLoader(getClass().getResource("LoginView.fxml"));
		//LoginViewController loginViewController= new LoginViewController();
		//loader.setController(loginViewController);
		//Parent root= loader.load();
		//loginViewController.setPrimaryStage(primaryStage);
		primaryStage.setResizable(false);  // non ridimensionabile
		//Scene scene= new Scene(root);
		//primaryStage.setScene(scene);
		//primaryStage.show(); 
		ViewLoader.setStage(primaryStage);
		ViewLoader.caricaView(ViewInfo.CONFIGURAZIONE);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
