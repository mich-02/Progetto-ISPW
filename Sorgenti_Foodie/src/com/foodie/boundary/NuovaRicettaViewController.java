package com.foodie.boundary;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.foodie.controller.PubblicaRicettaController;
import com.foodie.bean.AlimentoBean;
import com.foodie.bean.RicettaBean;
import com.foodie.bean.UtenteBean;
import com.foodie.boundary.components.ViewInfo;
import com.foodie.boundary.components.ViewLoader;
import com.foodie.controller.LoginController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;

public class NuovaRicettaViewController {
	
	private PubblicaRicettaController pubblicaRicettacontroller = new PubblicaRicettaController();
	private LoginController loginController = new LoginController();
	
	@FXML
	private Button gestisciRicetteButton;
	@FXML
	private RadioButton facile;
	@FXML
	private RadioButton medio;
	@FXML
	private RadioButton difficile;
	@FXML
	private TextField nome;
	@FXML
	private TextArea descrizione;
	@FXML
	private Button pubblica;
	
	private RicettaBean ricettaBean = new RicettaBean();
	
	public void setRicettaBean(RicettaBean ricettaBean) {
		this.ricettaBean = ricettaBean;
		aggiornaView();
	}
	
	private void aggiornaView() {  // aggiorno i vari campi
		if (ricettaBean == null) {
	        return;
	    }
		nome.setText(ricettaBean.getNome());
		descrizione.setText(ricettaBean.getDescrizione());
		switch(ricettaBean.getDifficolta()) {
		case 1:
				facile.setSelected(true);
				disabilitaPulsanti(null);
				break;
		case 2:
				medio.setSelected(true);
				disabilitaPulsanti(null);
				break;
		case 3:
				difficile.setSelected(true);
				disabilitaPulsanti(null);
				break;
		default:
			disabilitaPulsanti(null);
		}
	}
	
	@FXML
    private void initialize() {
        // Listener per aggiornare il bean in tempo reale
        nome.textProperty().addListener((obs, oldText, newText) -> ricettaBean.setNome(newText));
        descrizione.textProperty().addListener((obs, oldText, newText) -> ricettaBean.setDescrizione(newText));

        facile.selectedProperty().addListener((obs, oldVal, newVal) -> { if (newVal) ricettaBean.setDifficolta(1); });
        medio.selectedProperty().addListener((obs, oldVal, newVal) -> { if (newVal) ricettaBean.setDifficolta(2); });
        difficile.selectedProperty().addListener((obs, oldVal, newVal) -> { if (newVal) ricettaBean.setDifficolta(3); });
    }
	
	@FXML
	private void caricaViewIngredienti(ActionEvent event) {
		FXMLLoader loader = ViewLoader.caricaView(ViewInfo.INSERISCI_INGR);
	        InserisciIngredienteViewController controller = loader.getController();
	        controller.setRicettaBean(this.ricettaBean); // Passo il ricettaBean 
	    
	}
	
	@FXML
    private void compilaRicetta(ActionEvent event) {
        // Verifica campi vuoti
        boolean valid = true;

        if (nome.getText().trim().isEmpty()) {
            showTemporaryPrompt(nome, "INSERISCI NOME");
            valid = false;
        }

        if (descrizione.getText().trim().isEmpty()) {
            showTemporaryPrompt(descrizione, "INSERISCI DESCRIZIONE");
            valid = false;
        }

        if (!facile.isSelected() && !medio.isSelected() && !difficile.isSelected()) {
            showTemporaryButtonText(pubblica, "DIFFICOLTA?");
            valid = false;
        }

        if (!valid) return;

        // Imposta autore
        UtenteBean utenteBean = loginController.getUtente();
        ricettaBean.setAutore(utenteBean.getUsername());

        // Imposta ingredienti
        List<AlimentoBean> alimentiBean = pubblicaRicettacontroller.mostraIngredientiRicetta();
        if (!alimentiBean.isEmpty()) {
            ricettaBean.setIngredienti(alimentiBean);
            pubblicaRicettacontroller.compilaRicetta(ricettaBean);
            gestisciRicetteButton.fire(); // simula click per tornare all'area personale
        } else {
            showTemporaryButtonText(pubblica, "INGREDIENTI?");
        }
    }

    private void showTemporaryPrompt(TextInputControl field, String text) {
        field.setPromptText(text);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> Platform.runLater(() -> field.setPromptText(field.getId().equals("nome") ? "Nome Ricetta" : "Descrizione")), 2, TimeUnit.SECONDS);
        scheduler.shutdown();
    }

    private void showTemporaryButtonText(Button button, String text) {
        String original = button.getText();
        button.setText(text);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> Platform.runLater(() -> button.setText(original)), 2, TimeUnit.SECONDS);
        scheduler.shutdown();
    }
	
	@FXML
	private void disabilitaPulsanti(ActionEvent event) {  //per cliccare solo una difficoltà alla volta 
		if (facile.isSelected()) {
		    medio.setDisable(true);
		    difficile.setDisable(true);
		} else if (medio.isSelected()) {
		    facile.setDisable(true);
		    difficile.setDisable(true);
		} else if (difficile.isSelected()) {
		    medio.setDisable(true);
		    facile.setDisable(true);
		} else {
		    medio.setDisable(false);
		    difficile.setDisable(false);
		    facile.setDisable(false);
		}
	}
	
	@FXML
    private void caricaViewGestisciRicette(ActionEvent event) { 
		ViewLoader.caricaView(ViewInfo.GESTISCI_RICETTE1);
    }
	
	@FXML
	private void tornaAlLogin(MouseEvent event) {  
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
	}

}