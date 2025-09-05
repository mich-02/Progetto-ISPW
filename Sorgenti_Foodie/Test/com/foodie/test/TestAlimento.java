package com.foodie.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.foodie.model.Alimento;

public class TestAlimento { //BIRU MICHELE FRANCESCO 
	
	private static final String NOME1 = "Arancia";
	private static final String NOME2 = "Mela";
	
	@Test
    public void testGetNomeUguale() {
        Alimento alimento = new Alimento(NOME1);
        assertEquals(NOME1, alimento.getNome()); //verifica che il metodo restituisca il nome corretto
	}
	
	@Test
	public void testEqualsStessoNome() {
		Alimento alimento1 = new Alimento(NOME1);
        Alimento alimento2 = new Alimento(NOME1);
        assertTrue(alimento1.equals(alimento2)); //verifica che restituisca true con due istanze con lo stesso nome
	}
	
	 @Test
	 public void testHashCodeDiverso() {
	    Alimento alimento1 = new Alimento(NOME1);
	    Alimento alimento2 = new Alimento(NOME2);
	    assertNotEquals(alimento1.hashCode(), alimento2.hashCode()); //verifica che il loro hash code sia diverso
	 }
}
