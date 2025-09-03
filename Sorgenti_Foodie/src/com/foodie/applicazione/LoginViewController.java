package com.foodie.applicazione;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.foodie.bean.CredenzialiBean;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.LoginController;

public class LoginViewController {
	
	private int interfaccia;
	
	@FXML
	private Button registratiButton;
	@FXML
	private Button loginButton;
	@FXML
	private Label loginMessageLabel;
	@FXML 
	private TextField usernameTextField;
	@FXML
	private PasswordField enterPasswordField;
	@FXML
	private Label chiudiLabel;
	@FXML
    private RadioButton interfaccia1RadioButton;
    @FXML
    private RadioButton interfaccia2RadioButton;
    @FXML
    private ToggleGroup userTypeToggleGroup;
    
    private LoginController loginController = new LoginController();
    
	@FXML
    private void initialize() {  
        chiudiLabel.setOnMouseClicked(event -> closeApplication());
     
    	userTypeToggleGroup = new ToggleGroup();
        
    	interfaccia1RadioButton.setToggleGroup(userTypeToggleGroup);
    	interfaccia2RadioButton.setToggleGroup(userTypeToggleGroup);
        
        // Imposto primo bottone come quello selezionato di default
        userTypeToggleGroup.selectToggle(interfaccia1RadioButton);
        interfaccia = 1;
    }
	
    private void closeApplication() {  //chiusura applicazione
        Stage stage = (Stage)chiudiLabel.getScene().getWindow();
        stage.close();
    }
	
	@FXML
	public void loginButtonOnAction(ActionEvent event) { //attivo metodo quando premo il pulsante
		
		if(!usernameTextField.getText().isBlank() && !enterPasswordField.getText().isBlank()) {
			validazioneLogin();
		} else {
			loginMessageLabel.setText("Inserisci username e password");
		}
		
	}
	
	@FXML
	public void registratiButtonOnAction(ActionEvent event) {  //operazione invocata alla pressione del tasto Registrati
		ViewLoader.caricaView(ViewInfo.REGISTRAZIONE_VIEW); //carica la view per la pagina di registrazione
	}
	
	private void validazioneLogin() {  //metodo per il login e per settare il ruolo dell'utente 
		String username = usernameTextField.getText();
		String password = enterPasswordField.getText();
		CredenzialiBean credenzialiBean = new CredenzialiBean();
		credenzialiBean.setUsername(username);
		credenzialiBean.setPassword(password);
		int tipo = loginController.effettuaLogin(credenzialiBean); //ritorna il tipo dell'utenza 
		if(tipo != -1) {
		/*
		String ruolo = ""; 
		if(tipo != -1) {
			if(tipo == 0) {
				ruolo = "Standard";		
			}
			else if(tipo == 1){
				ruolo = "Chef";
			}
			else {
				ruolo = "Moderatore";
			}
			*/
		//loginController.setUtente(username.toLowerCase(), ruolo); 
			
			if(interfaccia1RadioButton.isSelected()) { // verifico la GUI da attivare 
				interfaccia = 1;
			} else {
				interfaccia = 2;
			}
			String viewIniziale = loginController.ottieniView(interfaccia);
			/*
			if(tipo == 0) {
				//Dispensa dispensa = Dispensa.ottieniIstanza();
            	//dispensa.svuotaDispensa();
            //	loginController.caricaDispense();
			}
			*/
			ViewLoader.caricaView(ViewInfo.fromFxmlPath(viewIniziale));
		} else {  	//se tipo == -1 errore nel login
			loginMessageLabel.setText("credenziali errate. Se non hai ancora un account, registrati.");
		}
	}	
}