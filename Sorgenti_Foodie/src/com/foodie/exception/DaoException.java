package com.foodie.exception;

public class DaoException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public DaoException(String message) {
        super("Dao exception: " + message);
    }
		
}
