package com.foodie.applicazione;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;
import com.foodie.bean.CredenzialiBean;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.LoginController;
import com.foodie.exception.UtenteEsistenteException;


public class RegistratiViewController {
	
	// Variabile per memorizzare il ruolo, 0 per l'utente base, 1 per lo chef, 2 per il moderatore
    private int ruolo;
    private LoginController controller = new LoginController();
	@FXML
    private RadioButton baseRadioButton;
    @FXML
    private RadioButton chefRadioButton;
    @FXML
    private RadioButton moderatoreRadioButton;
   
    private ToggleGroup userTypeToggleGroup;
    
    @FXML
    private Button indietroButton;
    @FXML
    private Button registratiButton;
    @FXML
    private TextField nomeTextField;
    @FXML
    private TextField cognomeTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private Label esitoRegistrazioneLabel;
    
    
    @FXML
    public void initialize() {  
      
    	userTypeToggleGroup = new ToggleGroup();
        
    	baseRadioButton.setToggleGroup(userTypeToggleGroup);
        chefRadioButton.setToggleGroup(userTypeToggleGroup);
        moderatoreRadioButton.setToggleGroup(userTypeToggleGroup);
        
        // Imposto primo bottone come quello selezionato di default
        userTypeToggleGroup.selectToggle(baseRadioButton);
        
        // Impostazione inizale del ruolo 
        ruolo = 0;    
    }

    private void handleRegistration() {  //imposta il ruolo
        RadioButton selectedRadioButton = (RadioButton) userTypeToggleGroup.getSelectedToggle();
        	
        	if(selectedRadioButton == baseRadioButton) {
        		ruolo = 0;
        	} else if (selectedRadioButton == chefRadioButton) {
        		ruolo = 1;
        	}
        	else {
        		ruolo = 2;
        	}
        }
    
    @FXML
    public void indietroButtonOnAction(ActionEvent event) { 
    	ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
   }
   
   @FXML
   public void registratiButtonOnAction(ActionEvent event) {  //chiama i metodi per registrarsi
    	handleRegistration();
    	registrazioneUser();	
   }
   
   private void registrazioneUser() {
	    // Prendo input utente e verifico che non sia vuoto
	    String nome = nomeTextField.getText().trim();
	    String cognome = cognomeTextField.getText().trim();
	    String username = usernameTextField.getText().trim();
	    String password = setPasswordField.getText().trim();
	    int role = ruolo;

	    CredenzialiBean credenzialiBean = new CredenzialiBean();

	    if (nome.isEmpty() || cognome.isEmpty() || username.isEmpty() || password.isEmpty()) {
	        // Uno o più campi sono vuoti
	        esitoRegistrazioneLabel.setText("Per favore, completa tutti i campi");
	        return; // Interrompi l'esecuzione se ci sono campi vuoti
	    }

	    credenzialiBean.setUsername(username);

	    try {
	        // Controllo se l'username è già presente nella base di dati
	        controller.controllaUsername(credenzialiBean);
	    } catch (UtenteEsistenteException e) {
	        esitoRegistrazioneLabel.setText("L'username è già in uso. Scegli un altro username.");
	        return; // Interrompi l'esecuzione se l'username è già in uso
	    }

	    credenzialiBean.setNome(nome);
	    credenzialiBean.setCognome(cognome);
	    credenzialiBean.setRuolo(role);
	    credenzialiBean.setPassword(password);
	    controller.registraUtente(credenzialiBean);

	    esitoRegistrazioneLabel.setText("Registrazione andata a buon fine.");
	    PauseTransition pause = new PauseTransition(Duration.seconds(2));
	    pause.setOnFinished(event -> {
	        ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
	    });
	    pause.play();
	}
}