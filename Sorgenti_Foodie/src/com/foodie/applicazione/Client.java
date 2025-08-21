package com.foodie.applicazione;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Client extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader= new FXMLLoader(getClass().getResource("LoginView.fxml"));
		LoginViewController loginViewController=LoginViewController.ottieniIstanza();
		loader.setController(loginViewController);
		Parent root= loader.load();
		loginViewController.setPrimaryStage(primaryStage);
		primaryStage.setResizable(false);  //NON ZOOMABILE
		Scene scene= new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();  
	}

	public static void main(String[] args) {
		launch(args);
	}
}
