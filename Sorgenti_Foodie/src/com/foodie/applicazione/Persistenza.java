package com.foodie.applicazione;

public enum Persistenza {
	MEMORIA("In Memoria"),
    DATABASE("Database");

    private final String descrizione;

    Persistenza(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return descrizione;
    }
}
