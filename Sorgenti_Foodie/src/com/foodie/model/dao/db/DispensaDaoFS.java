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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
}