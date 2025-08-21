package com.foodie.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CatalogoRicetteImplementazione2Dao implements CatalogoRicetteChefDao{  //DAO CON FILE SYSTEM
	
	private static CatalogoRicetteImplementazione2Dao istanza;    //SINGLETON
	private static Path currentPath = Paths.get("");
	private static Path projectPath = currentPath.toAbsolutePath().normalize();
	private static Path otherDirectory = projectPath.resolve("CatalogoRicette");
	private static Path filePath = otherDirectory.resolve("CatalogoRicette.txt");
	private static final String PATH = filePath.toString();
	private static final String MESSAGGIO= "ERRORE NELL'APERTURA DEL CATALOGO DELLE RICETTE (FILE)";
	private static final Logger logger = Logger.getLogger(CatalogoRicetteImplementazione2Dao.class.getName());
	
	private CatalogoRicetteImplementazione2Dao(){
	}
	
	public static synchronized CatalogoRicetteImplementazione2Dao ottieniIstanza() { //METODO PER OTTENERE L'ISTANZA
		if(istanza==null) {
			istanza=new CatalogoRicetteImplementazione2Dao();
		}
		return istanza;
	}
	
	@Override
	public ArrayList<Ricetta> trovaRicette(Dispensa dispensa, int difficolta, String autore) throws SQLException,ClassNotFoundException { //TROVA LE RICETTE NEL FILE O PER ALIMENTI-DIFFICOLTA' O PER AUTORE
	    ArrayList<String> linee = new ArrayList<>();
	    ArrayList<Ricetta> ricetteTrovate=new ArrayList<>();
	    if(dispensa!=null && dispensa.getAlimenti().isEmpty()) { //CONTROLLO SE LA DISPENSA è VUOTA SE GLIELA FORNISCO
			logger.info("Dispensa vuota!!! Riempila prima");
			return new ArrayList<>();
		}
	    try(BufferedReader lettore = new BufferedReader(new FileReader(PATH))) {//MI CARICO TUTTE LE RIGHE DEL FILE SI CHIUDE IN AUTOMATICO IL FILE
	        String linea;
	        while ((linea = lettore.readLine()) != null) {
	            linee.add(linea);
	        }
	        if (!linee.isEmpty()) {
	        	analizzaCampi(linee,dispensa,ricetteTrovate,difficolta,autore);  //ANALIZZO I CAMPI
		        if(!ricetteTrovate.isEmpty()) {
		        	logger.info("Ricette Trovate");
		        	return ricetteTrovate;
		        }
		    }
	        logger.info("Nessuna ricetta trovata");
	        return new ArrayList<>();
	    } catch (IOException e) {
	        e.printStackTrace();
	        logger.severe(MESSAGGIO);
	        return new ArrayList<>();
	    }
	}
	
	private void analizzaCampi(ArrayList<String> linee,Dispensa dispensa,ArrayList<Ricetta> ricetteTrovate, int difficolta, String autore) {
		for (String s : linee) {
            String[] campi = s.split(";");
            if(dispensa!=null) { //SE FORNISCO LA DISPENSA SIGNIFICA CHE VOGLIO FARE LA QUERY PER ALIMENTI
				List<Alimento> alimentiDispensa= dispensa.getAlimenti();
            	String[] alimenti= campi[4].split(",");
				if(controllaIngredienti(alimentiDispensa,alimenti) && Integer.parseInt(campi[3])==difficolta) {
					Ricetta ricetta=costruisciRicetta(campi);
					ricetteTrovate.add(ricetta);
				}
			}
			else { //SE NON LA FORNISCO E DO UN AUTORE SIGNIFICA CHE VOGLIO FARE LA QUERY PER AUTORE
				if(campi[1].equals(autore)) {
					Ricetta ricetta= costruisciRicetta(campi);
					ricetteTrovate.add(ricetta);
				}
			}
        }
	}
	
	private boolean controllaIngredienti(List<Alimento> alimentiDispensa,String[] alimenti) {//METODO PRIVATO CHE MI CONSENTE DI VEDERE SE LA DISPENSA CONTIENE GLI INGREDIENTI NECESSARI ALLA RICETTA
		for(String s:alimenti) {  //INGREDIENTI RICETTA
			for(int i=0;i<alimentiDispensa.size();i++) {  //ALIMENTI DISPENSA
				Alimento alimento= alimentiDispensa.get(i);
				if(alimento.getNome().equals(s)) {
					break;
				}
				else if(i==alimentiDispensa.size()-1) {
					return false;
				}
			}
		}
		return true;
	}

	private Ricetta costruisciRicetta(String[] campi) {  //METODO PER LA COSTRUZIONE DELLA RICETTA DALLA STRINGA OTTENUTA DAL FILE
		Ricetta ricetta = new Ricetta(campi[0], campi[2],Integer.parseInt(campi[3]), new ArrayList<>(), campi[1], new ArrayList<>());
		String[] alimenti= campi[4].split(",");
    	String[] quantita= campi[5].split(",");
    	for(int i=0;i<alimenti.length;i++) {
    		Alimento alimento=new Alimento(alimenti[i]);
    		ricetta.aggiungiIngrediente(alimento, quantita[i]);
    	}
    	return ricetta;
	}
	
	@Override
	public void aggiungiRicetta(Ricetta ricetta) throws SQLException,ClassNotFoundException,RicettaDuplicataException { //METODO PER AGGIUNGERE LA RICETTA SUL FILE SE NON PRESENTE
		if(controllaSeEsistente(ricetta.getNome(),ricetta.getAutore())) {
			RicettaDuplicataException eccezione= new RicettaDuplicataException("Ricetta già esistente nel file!");
			eccezione.suggerimento();
			throw eccezione;
		}
		try(BufferedWriter scrittore= new BufferedWriter(new FileWriter(PATH,true))) {//FORMATTAZIONE NOME;AUTORE;DESCRIZIONE;DIFFICOLTA;ALIMENTO1,ALIMENTO2;QUANTITA'1,QUANTITA'2.
			String nome=ricetta.getNome();
			String descrizione=ricetta.getDescrizione();
			String autore=ricetta.getAutore();
			int difficolta=ricetta.getDifficolta();
			StringBuilder alimentiBuilder = new StringBuilder();
			StringBuilder quantitaBuilder = new StringBuilder();
			for (int i = 0; i < ricetta.getIngredienti().size(); i++) {
			    Alimento alimento = ricetta.getIngredienti().get(i);
			    alimentiBuilder.append(alimento.getNome());
			    quantitaBuilder.append(ricetta.getQuantita().get(i));
			    if (i != ricetta.getIngredienti().size() - 1) {
			        alimentiBuilder.append(",");
			        quantitaBuilder.append(",");
			    }
			}
			String alimenti = alimentiBuilder.toString();
			String quantita = quantitaBuilder.toString();
			scrittore.write(nome+";"+autore+";"+descrizione+";"+Integer.toString(difficolta)+";"+alimenti+";"+quantita);
			scrittore.newLine();
			logger.info("Ricetta aggiunta al database");
		}catch(IOException e) {
			e.printStackTrace();
			logger.severe("ERRORE NELLA SCRITTURA NEL CATALOGO DELLE RICETTE (FILE)");
			logger.info("Ricetta non aggiunta al database");
		}
	}

	private boolean controllaSeEsistente(String nome,String autore) {  //METODO PER VERIFICARE SE LA RICETTA è GIA' PRESENTE NEL FILE
		ArrayList<String> linee = new ArrayList<>();
		try(BufferedReader lettore = new BufferedReader(new FileReader(PATH))) {
	        String linea;
	        while ((linea = lettore.readLine()) != null) {
	            linee.add(linea);
	        }
	        if(!linee.isEmpty()) {
	        	for (String s : linee) {
		            String[] campi = s.split(";");
		            if(campi[0].equals(nome) && campi[1].equals(autore)) {
		            	return true;
		            }
		        }
	        	return false;
	        }
	        else {
	        	return false;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        logger.severe(MESSAGGIO);
	        return false;
	    }
	}

	@Override
	public void eliminaRicetta(String nome, String autore) throws SQLException,ClassNotFoundException {  //METODO PER ELIMINARE LA RICETTA DAL FILE
	    ArrayList<String> lineeVecchie = new ArrayList<>();
        ArrayList<String> lineeNuove = new ArrayList<>();
	    try(BufferedReader lettore = new BufferedReader(new FileReader(PATH))) {
	        String linea;
	        while ((linea = lettore.readLine()) != null) {
	            lineeVecchie.add(linea);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        logger.severe(MESSAGGIO);
	        logger.info("Ricetta eliminata dal database");
	        return;
	    }
	    if (!lineeVecchie.isEmpty()) {
	        for (String s : lineeVecchie) {
	            String[] campi = s.split(";");
	            if (!campi[0].equals(nome) || !campi[1].equals(autore)) {
	                lineeNuove.add(s);
	            }
	        }
	    }
	    try(BufferedWriter scrittore = new BufferedWriter(new FileWriter(PATH))) {
	        if (!lineeNuove.isEmpty()) {
	            for (String s : lineeNuove) {
	                scrittore.write(s);
	                scrittore.newLine();
	            }
	            logger.info("Ricetta eliminata dal database");
	            return;
	        }
	        logger.info("Ricetta non presente nel database");  //LEVO 16° if per codesmell
	    } catch (IOException e) {
	        e.printStackTrace();
	        logger.severe("ERRORE NELLA SCRITTURA NEL CATALOGO DELLE RICETTE (FILE)");
	        logger.info("Ricetta non eliminata dal database");
	    }
	}

	@Override
	public Ricetta ottieniDatiRicetta(String nome, String autore) throws SQLException,ClassNotFoundException {  //METODO PER OTTENERE I DATI DELLA RICETTA SPECIFICA
	    ArrayList<String> linee = new ArrayList<>();
	    try(BufferedReader lettore = new BufferedReader(new FileReader(PATH));) {//MI CARICO TUTTE LE RIGHE DEL FILE  
	        String linea;
	        while ((linea = lettore.readLine()) != null) {
	            linee.add(linea);
	        }
	        if (!linee.isEmpty()) {
		        for (String s : linee) {
		            String[] campi = s.split(";");
					if(campi[0].equals(nome) && campi[1].equals(autore)) {
						return costruisciRicetta(campi);
					}
		        }
		        return null;
		    }
	        else {
	        	return null;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        logger.severe(MESSAGGIO);
	        return null;
	    }
	}

}