package com.foodie.boundary.components;

public enum ViewInfo {
	CONFIGURAZIONE("/com/foodie/applicazione/ConfigurazioneView.fxml"),
	LOGIN_VIEW("/com/foodie/applicazione/LoginView.fxml"),
	REGISTRAZIONE_VIEW("/com/foodie/applicazione/RegistratiView.fxml"),
	AREA_CHEF1("/com/foodie/boundary/AreaPersonaleView.fxml"),
	AREA_CHEF2("/com/foodie/boundary2/AreaPersonaleView2.fxml"),
	DISPENSA_UTENTE("/com/foodie/boundary/DispensaUtenteView.fxml"),
	DISPENSA2("/com/foodie/boundary2/DispensaView2.fxml"),
	AGGIUNGI_ALIMENTO("/com/foodie/boundary2/AggiungiAlimentoView2.fxml"),
	MOD_VIEW1("/com/foodie/boundary/ModeratoreView.fxml"),
	MOD_VIEW2("/com/foodie/boundary2/ModeratoreView2.fxml"),
	GESTISCI_RICETTE1("/com/foodie/boundary/GestisciRicetteView.fxml"),
	GESTISCI_RICETTE2("/com/foodie/boundary2/GestisciRicetteView2.fxml"),
	NUOVA_RICETTA1("/com/foodie/boundary/NuovaRicettaView.fxml"),
	NUOVA_RICETTA2("/com/foodie/boundary2/NuovaRicettaView2.fxml"),
	CONTENUTO_RIC_CHEF("/com/foodie/boundary/ContenutoRicettaChef.fxml"),
	CONTENUTO_RIC_UT("/com/foodie/boundary/ContenutoRicettaView.fxml"),
	INSERISCI_INGR("/com/foodie/boundary/InserisciIngredienteView.fxml"),
	TROVA_RICETTE("/com/foodie/boundary/TrovaRicetteView.fxml"),
	TROVA_RICETTE2("/com/foodie/boundary2/TrovaRicettaView2.fxml");
	
	
	private final String fxmlPath;
	
	ViewInfo(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }
	
	public String getFxmlPath() {
        return fxmlPath;
    }
	
	// metodo per ottenere il ViewInfo in base al percorso FXML
    public static ViewInfo fromFxmlPath(String fxmlPath) {
        for (ViewInfo viewInfo : values()) {
            if (viewInfo.getFxmlPath().equalsIgnoreCase(fxmlPath)) {
                return viewInfo;
            }
        }
        throw new IllegalArgumentException("No ViewInfo found for FXML path: " + fxmlPath);
    }
}
