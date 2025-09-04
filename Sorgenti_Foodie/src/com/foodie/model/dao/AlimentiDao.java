package com.foodie.model.dao;

import java.util.List;
import com.foodie.exception.NessunAlimentoTrovatoException;
import com.foodie.model.Alimento;

public interface AlimentiDao {  //DAO PER GLI ALIMENTI
	
	public List<Alimento> trovaAlimenti(String nome) throws NessunAlimentoTrovatoException;
	
}