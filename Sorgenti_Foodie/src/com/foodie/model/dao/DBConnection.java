package com.foodie.model.dao;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DBConnection { //NOSONAR
	private static DBConnection istanza;  
    private Connection connection;
    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());

    // Costruttore privato: impedisce la creazione con new
    private DBConnection() {
        try (InputStream input = new FileInputStream("resources/db.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            String connectionUrl = properties.getProperty("CONNECTION_URL");
            String user = properties.getProperty("LOGIN_USER");
            String pass = properties.getProperty("LOGIN_PASS");

            connection = DriverManager.getConnection(connectionUrl, user, pass);

        } catch (IOException | SQLException e) {
            logger.severe("Si è verificato un errore: " + e.getMessage());
        }
    }

    // Metodo per ottenere l'unica istanza
    public static DBConnection ottieniIstanza() {
        if (istanza == null) {
            istanza = new DBConnection();  
        }
        return istanza;
    }

    public Connection getConnection() {
        return connection;
    }

    // Metodo per chiudere la connessione, se necessario
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Errore durante la chiusura della connessione: " + e.getMessage());
        }
    }
}
