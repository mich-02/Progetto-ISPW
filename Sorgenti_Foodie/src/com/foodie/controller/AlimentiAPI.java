package com.foodie.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodie.bean.AlimentoBean;
import com.foodie.exception.NessunAlimentoTrovatoException;
import com.foodie.model.Alimento;

public class AlimentiAPI{ 
	
	private static final String API_URL = "https://trackapi.nutritionix.com/v2/search/instant";
	private static final String APP_ID = "103947a7";
	private static final String API_CHIAVE = "726892d58f85bf82f8f8ab4171671c42";
	private static final Logger logger = Logger.getLogger(AlimentiAPI.class.getName());
	
	private ArrayList<Alimento> estraiFoodName(String stringa) { //INIZIALIZZO I VARI ALIMENTI DAL CAMPO FOOD NAME ESTRATTO DAL JSON OTTENUTO
        ObjectMapper mapper = new ObjectMapper(); 
        try {
            JsonNode nodo = mapper.readTree(stringa).get("common"); //prendo ramo common del vettore json di elementi
            return mapper.readValue(
                nodo.toString(),
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, Alimento.class)
            );
        } catch (JsonMappingException e) {
            logger.severe("PROBLEMA LIBRERIA JSON MAPPING: " + e.getMessage());
            return new ArrayList<>();
        } catch (JsonProcessingException e) {
            logger.severe("PROBLEMA LIBRERIA JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<AlimentoBean> trovaAlimenti(String nome) throws NessunAlimentoTrovatoException {
        try {
            ArrayList<AlimentoBean> alimentiBeanTrovati = new ArrayList<>();
            String nomeCodificato = URLEncoder.encode(nome, "UTF-8"); //da stringa codificato per l'url
            String urlQuery = API_URL + "?query=" + nomeCodificato; //url per fare query all'api di nutrixionix
            URI url = new URI(urlQuery); //creo l'URI associato a quell'url
            HttpURLConnection connessione = (HttpURLConnection) url.toURL().openConnection(); //converto l'URI ad URl e apro una connessione http
            // imposto il modo di richiedere come definito nella documentazione dell'API di Nutrixionix
            connessione.setRequestMethod("GET");
            connessione.setRequestProperty("x-app-id", APP_ID);
            connessione.setRequestProperty("x-app-key", API_CHIAVE);

            int codiceDiRisposta = connessione.getResponseCode(); //ottengo codice di risposta di http
            if (codiceDiRisposta == HttpURLConnection.HTTP_OK) { //controllo se == 2 00 per capire se la connessione http ha ottenuto esito positivo
                BufferedReader lettore = new BufferedReader(new InputStreamReader(connessione.getInputStream()));
                StringBuilder risposta = new StringBuilder();
                String stringa; //converto i dati ottenuti in una stringa
                while ((stringa = lettore.readLine()) != null) {
                    risposta.append(stringa);
                }
                lettore.close();

                ArrayList<Alimento> alimenti = estraiFoodName(risposta.toString()); // applico il metodo che deserializza la stringa json ottenuta per ottenere i nomi degli alimenti
                connessione.disconnect();

                if (!alimenti.isEmpty()) {
                    // 
                    for (Alimento a : alimenti) {
                    	
                        alimentiBeanTrovati.add(new AlimentoBean(a.getNome()));
                    }
                    return alimentiBeanTrovati;
                } else {
                    throw new NessunAlimentoTrovatoException(nome);
                }
            } else {
                String warning = "Errore: codice di risposta " + codiceDiRisposta;
                logger.warning(warning);
                connessione.disconnect();
                throw new NessunAlimentoTrovatoException(nome);
            }
        } catch (Exception e) {
            logger.severe("Errore generico di connessione con l'API Nutrixionix: " + e.getMessage());
            throw new NessunAlimentoTrovatoException(nome);
        }
    }

}