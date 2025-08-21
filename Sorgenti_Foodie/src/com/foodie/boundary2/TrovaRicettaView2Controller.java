package com.foodie.boundary2;



import java.util.List;
import com.foodie.controller.AdattatoreFactory;
import com.foodie.controller.ControllerAdapter;
import com.foodie.model.AlimentoBean;
import com.foodie.model.RicettaBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TrovaRicettaView2Controller {
	
	private static TrovaRicettaView2Controller istanza;
	private AdattatoreFactory factory= AdattatoreFactory.ottieniIstanza();
	private ControllerAdapter adattatoreTrovaRicettaController= factory.creaTrovaRicettaAdapter();
	private CaricaView2 caricaView2= CaricaView2.ottieniIstanza();
	private Stage primaryStage;
	private static final String FORMATO = "Arial";
	private static final String SFONDOGRIGIO = "-fx-background-color: rgba(217, 217, 217, 0.75);-fx-border-color: black;";
	@FXML
	private RadioButton facile;
	@FXML
	private RadioButton medio;
	@FXML
	private RadioButton difficile;
	@FXML
	private VBox contenitoreRicette;
	
	private TrovaRicettaView2Controller() {	
	}
	
	public static synchronized TrovaRicettaView2Controller ottieniIstanza() { //SINGLETON
		if(istanza == null) {
			istanza = new TrovaRicettaView2Controller();
		}
		return istanza;
	}
	
	public void setPrimaryStage(Stage primaryStage) {  //PASSO LO STAGE
		this.primaryStage= primaryStage;
	}
	
	@FXML
    private void tornaAlLogin(MouseEvent event) {  //CARICA VIEW LOGIN
        caricaView2.tornaAlLogin(primaryStage);
    }
	
	@FXML
	private void caricaViewDispensa(ActionEvent event) {  //CARICA VIEW DISPENSA
		caricaView2.caricaViewDispensa(primaryStage);
	}
	
	@FXML
	private void disabilitaPulsanti(ActionEvent event) {  //GESTISCE PULSANTI DIFFICOLTA'
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
    private void caricaViewAlimenti(ActionEvent event) {  //CARICA VIEW TROVA ALIMENTI
        caricaView2.caricaViewAlimenti(primaryStage);
    }
	
	@FXML
	private void trovaRicette(ActionEvent event) {  //METODO PER TROVARE RICETTE
		contenitoreRicette.getChildren().clear();
		List<RicettaBean> ricetteTrovate= null;
		String difficolta="";
		if (facile.isSelected()) {
			ricetteTrovate=adattatoreTrovaRicettaController.trovaLeRicette(1,null);
			difficolta="facile";
		} 
		else if (medio.isSelected()) {
			ricetteTrovate=adattatoreTrovaRicettaController.trovaLeRicette(2,null);
			difficolta="intermedia";
		} 
		else if (difficile.isSelected()) {
			ricetteTrovate=adattatoreTrovaRicettaController.trovaLeRicette(3,null);
			difficolta="difficile";
		}
		else {  //SE LA DIFFICOLTA' NON E' SELEZIONATA AVVERTIMENTO !
			Label label=new Label("SCEGLI DIFFICOLTA'");
			label.setStyle(SFONDOGRIGIO);
			label.setMaxWidth(Double.MAX_VALUE);
			label.setMinHeight(110);
			label.setWrapText(true);
			label.setFont(Font.font(FORMATO,50));
			label.setAlignment(Pos.CENTER);
			contenitoreRicette.getChildren().add(label);
			return;
		}
		if(!ricetteTrovate.isEmpty()) {
			contenitoreRicette.getChildren().clear();  //SE RICETTE TROVATE MOSTRA TUTTO IL CONTENUTO DELLE RICETTE
			for(RicettaBean r: ricetteTrovate) {      //CREA VBOX E LABEL UNA DI SEGUITO ALL'ALTRA
				VBox contenitoreRicetta = new VBox();
				contenitoreRicetta.setStyle(SFONDOGRIGIO);
				contenitoreRicetta.setSpacing(10);				
			    Label labelNome = new Label("Nome: "+r.getNome());
			    labelNome.setFont(Font.font(FORMATO, 20));
			    Label labelAutore = new Label("Autore: "+r.getAutore());
			    labelAutore.setFont(Font.font(FORMATO, 20));
			    Label labelDifficolta = new Label("Difficoltà: "+difficolta);
			    labelDifficolta.setFont(Font.font(FORMATO, 20));			 
			    Label labelDescrizione = new Label("Descrizione: "+r.getDescrizione());
			    labelDescrizione.setFont(Font.font(FORMATO, 20));
			    labelDescrizione.setWrapText(true);	    
			    VBox contenitoreIngredienti= new VBox();
			    Label inizio= new Label("Ingredienti:");
			    inizio.setFont(Font.font(FORMATO, 20));
			    contenitoreIngredienti.getChildren().add(inizio);
			    String ingredienti="";
			    for (int i = 0; i < r.getIngredienti().size(); i++) {
			        AlimentoBean alimentoBean = r.getIngredienti().get(i);
			        ingredienti = alimentoBean.getNome() + " : " + r.getQuantita().get(i);
			        Label labelIngredienti= new Label(ingredienti);
			        labelIngredienti.setFont(Font.font(FORMATO, 20));
			        contenitoreIngredienti.getChildren().add(labelIngredienti);
			    }
			    
			    contenitoreRicetta.getChildren().addAll(labelNome,labelAutore,labelDifficolta,labelDescrizione, contenitoreIngredienti);
			    contenitoreRicette.getChildren().add(contenitoreRicetta);
			}
		}
		else {  //NESSUNA RICETTA TROVATA
			Label label=new Label("NESSUN RISULTATO");
			label.setStyle(SFONDOGRIGIO);
			label.setMaxWidth(Double.MAX_VALUE);
			label.setMinHeight(110);
			label.setWrapText(true);
			label.setFont(Font.font(FORMATO,50));
			label.setAlignment(Pos.CENTER);
			contenitoreRicette.getChildren().add(label);
		}
	}
	
}