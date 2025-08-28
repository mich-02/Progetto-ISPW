package com.foodie.applicazione;

public class ConfiguratorePersistenza {
	private static Persistenza persistenzaCorrente;
	
	private ConfiguratorePersistenza() {
	}
	
	public static void configuraPersistenza(Persistenza persistenza) {
		persistenzaCorrente = persistenza;
	}
	
	public static Persistenza getPersistenzaCorrente() {
		return persistenzaCorrente;
	}
}
