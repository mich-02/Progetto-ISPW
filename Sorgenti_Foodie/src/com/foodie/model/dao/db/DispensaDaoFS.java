package com.foodie.model.dao.db;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.foodie.exception.DaoException;
import com.foodie.model.Alimento;
import com.foodie.model.AlimentoSerializzabile;
import com.foodie.model.Dispensa;
import com.foodie.model.dao.DispensaDao;

public class DispensaDaoFS implements DispensaDao{ 

	private static final Path filePath = Paths.get("ClassiSerializzate/dispensa_data.ser").toAbsolutePath();
    private static final String PATH = filePath.toString();
	
	@SuppressWarnings("unchecked")
	@Override
	public void salvaDispensa(String username) throws DaoException {
	    Map<String, ArrayList<AlimentoSerializzabile>> dispensaMap = new HashMap<>();

	    // Provo a caricare la mappa completa dal file
	    try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(PATH))) {
	        dispensaMap = (Map<String, ArrayList<AlimentoSerializzabile>>) objectInputStream.readObject();
	    } catch (FileNotFoundException e) {
	        // Se il file non esiste ancora, la mappa rimane vuota
	    } catch (ClassNotFoundException e) {
	        throw new DaoException("salvaDispensa - Errore nella deserializzazione della dispensa: " + e.getMessage());
	    } catch (IOException e) {
	        throw new DaoException("salvaDispensa - Errore di I/O durante il caricamento della dispensa: " + e.getMessage());
	    }

	    // Prepara la lista degli alimenti dell'utente
	    ArrayList<AlimentoSerializzabile> alimentiSerializzabili = new ArrayList<>();
	    Dispensa dispensa = Dispensa.ottieniIstanza();
	    if (dispensa.getAlimenti() != null && !dispensa.getAlimenti().isEmpty()) {
	        for (Alimento a : dispensa.getAlimenti()) {
	            AlimentoSerializzabile alimento = new AlimentoSerializzabile();
	            alimento.setNome(a.getNome());
	            alimentiSerializzabili.add(alimento);
	        }
	    }

	    // Aggiorna la mappa con la dispensa dell'utente
	    dispensaMap.remove(username); // rimuove eventuale vecchia dispensa
	    if (!alimentiSerializzabili.isEmpty()) {
	        dispensaMap.put(username, alimentiSerializzabili);
	    }

	    // Salva la mappa aggiornata sul file
	    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(PATH))) {
	        objectOutputStream.writeObject(dispensaMap);
	    } catch (IOException e) {
	        throw new DaoException("salvaDispensa - Errore di I/O durante il salvataggio della dispensa: " + e.getMessage());
	    }
	}

	/*
	public void salvaDispensa(String username) throws DaoException {
	    Map<String, ArrayList<AlimentoSerializzabile>> dispensaMap = new HashMap<>();

	    // Provo a caricare la mappa completa dal file
	    try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(PATH))) {
	        dispensaMap = (Map<String, ArrayList<AlimentoSerializzabile>>) objectInputStream.readObject();
	    } catch (FileNotFoundException e) {
	        // Se il file non esiste ancora, la mappa rimane vuota
	    } catch (ClassNotFoundException e) {
	        throw new SQLException("Errore nella deserializzazione della dispensa", e);
	    } catch (IOException e) {
	        throw new SQLException("Errore di I/O durante il caricamento della dispensa", e);
	    }

	    // Prepara la lista degli alimenti dell'utente
	    ArrayList<AlimentoSerializzabile> alimentiSerializzabili = new ArrayList<>();
	    Dispensa dispensa = Dispensa.ottieniIstanza();
	    if (dispensa.getAlimenti() != null && !dispensa.getAlimenti().isEmpty()) {
	        for (Alimento a : dispensa.getAlimenti()) {
	            AlimentoSerializzabile alimento = new AlimentoSerializzabile();
	            alimento.setNome(a.getNome());
	            alimentiSerializzabili.add(alimento);
	        }
	    }

	    // Aggiorna la mappa con la dispensa dell'utente
	    dispensaMap.remove(username); // rimuove eventuale vecchia dispensa
	    if (!alimentiSerializzabili.isEmpty()) {
	        dispensaMap.put(username, alimentiSerializzabili);
	    }

	    // Salva la mappa aggiornata sul file
	    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(PATH))) {
	        objectOutputStream.writeObject(dispensaMap);
	    } catch (IOException e) {
	        throw new SQLException("Errore di I/O durante il salvataggio della dispensa", e);
	    }
	}
	*/


	@SuppressWarnings({ "unchecked", "resource" })  //RIMUOVO I WARNING
	@Override
	public List<Alimento> caricaDispensa(String username) throws DaoException {
	    Map<String, ArrayList<AlimentoSerializzabile>> dispensaMap = new HashMap<>();

	    // Provo a caricare la mappa dal file
	    try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(PATH))) {
	        dispensaMap = (Map<String, ArrayList<AlimentoSerializzabile>>) objectInputStream.readObject();
	    } catch (FileNotFoundException e) {
	        // Se il file non esiste, ritorno lista vuota
	        return new ArrayList<>();
	    } catch (EOFException e) {
	        // File esistente ma vuoto
	        return new ArrayList<>();
	    } catch (ClassNotFoundException e) {
	        throw new DaoException("caricaDispensa - Errore nella deserializzazione della dispensa: " + e.getMessage());
	    } catch (IOException e) {
	        throw new DaoException("caricaDispensa - Errore di I/O durante il caricamento della dispensa: " + e.getMessage());
	    }

	    // Prende la dispensa legata allo username
	    ArrayList<AlimentoSerializzabile> alimentiSerializzabili = dispensaMap.get(username);
	    if (alimentiSerializzabili == null || alimentiSerializzabili.isEmpty()) {
	        return new ArrayList<>(); // Nessuna dispensa per questo utente
	    }

	    // Converte AlimentoSerializzabile -> Alimento
	    List<Alimento> alimenti = new ArrayList<>();
	    for (AlimentoSerializzabile a : alimentiSerializzabili) {
	        alimenti.add(new Alimento(a.getNome()));
	    }

	    return alimenti;
	}

	/*
	public List<Alimento> caricaDispensa(String username) throws DaoException{
	    try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(PATH))) {

	        // Legge la mappa salvata sul file
	        Map<String, ArrayList<AlimentoSerializzabile>> dispensaMap =
	                (Map<String, ArrayList<AlimentoSerializzabile>>) objectInputStream.readObject();

	        // Prende la dispensa legata allo username
	        ArrayList<AlimentoSerializzabile> alimentiSerializzabili = dispensaMap.get(username);

	        if (alimentiSerializzabili == null) {
	            return new ArrayList<>(); // Nessuna dispensa per questo utente
	        }

	        // Converte AlimentoSerializzabile -> Alimento
	        List<Alimento> alimenti = new ArrayList<>();
	        for (AlimentoSerializzabile a : alimentiSerializzabili) {
	            Alimento alimento = new Alimento(a.getNome()); // usa il costruttore JSON-ready
	            alimenti.add(alimento);
	        }
	        return alimenti;

	    } catch (ClassNotFoundException e) {
	        throw new SQLException("Errore nella deserializzazione della dispensa", e);
	    } catch (EOFException e) {
	        // File esistente ma vuoto
	        return new ArrayList<>();
	    } catch (IOException e) {
	        throw new SQLException("Errore di I/O durante il caricamento della dispensa", e);
	    }
	}
	*/
	
	/*
	public void salvaDispensa(String username) throws SQLException {  //SALVA SU FILE UNA HASHMAP USERNAME-ARRAYLIST DI ALIMENTI,OGNI VOLTA LA CARICA E AGGIUNGE NUOVI ELEMENTI
		Map<String, ArrayList<AlimentoSerializzabile>> dispensaMap;
		ArrayList<AlimentoSerializzabile> alimentiSerializzabili=new ArrayList<>();
		if((dispensaMap = caricaDispensa())==null) {  //FALSE SIGNIFICA CHE NON DEVE RICARICARE LA DISPENSA(EVITI LOOP)
			dispensaMap=new HashMap<>();
		}
		Dispensa dispensa = Dispensa.ottieniIstanza();
		if(dispensa.getAlimenti()!=null && !dispensa.getAlimenti().isEmpty()) {
			for(Alimento a: dispensa.getAlimenti()) {
				AlimentoSerializzabile alimento = new AlimentoSerializzabile();
				alimento.setNome(a.getNome());
				alimentiSerializzabili.add(alimento);
			}
		}
		dispensaMap.remove(username);
		if(!alimentiSerializzabili.isEmpty()) {
			dispensaMap.put(username, alimentiSerializzabili);
		}
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(PATH))) {
			objectOutputStream.writeObject(dispensaMap);
			logger.info("Dispensa salvata");
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("ERRORE NEL SALVATAGGIO SU FILE DELLA DISPENSA");
            logger.info("Problema con il file, riprova o controlla se è nella directory");
        }
	}
	*/
	
	/*
	public Map<String, ArrayList<AlimentoSerializzabile>> caricaDispense() {  //CARICA L'HASHMAP DA FILE
		ObjectInputStream objectInputStream=null;    //TRUE VIENE PASSATO QUANDO FAI IL LOGIN!CHE è NECESSARIO RICARICARE LA DISPENSA!
		try {
			objectInputStream = new ObjectInputStream(new FileInputStream(PATH));
			Map<String, ArrayList<AlimentoSerializzabile>> dispensaMap = (Map<String, ArrayList<AlimentoSerializzabile>>)objectInputStream.readObject();// lo è per forza
			objectInputStream.close();
			return dispensaMap;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return new HashMap<>();
		}catch (EOFException e) {
			logger.warning("NESSUNA DISPENSA SALVATA");
			return new HashMap<>();
        }catch (IOException e) {
			e.printStackTrace();
			logger.severe("ERRORE NEL CARICAMENTO DA FILE DELLA DISPENSA");
			logger.info("Problema con il file, riprova o controlla se è nella directory");
            return new HashMap<>();
		}
	}
	*/


}