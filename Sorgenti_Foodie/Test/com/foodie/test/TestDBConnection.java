package com.foodie.test;

import static org.junit.Assert.assertNotNull;
import java.sql.Connection;
import org.junit.Before;
import org.junit.Test;
import com.foodie.model.dao.DBConnection;

public class TestDBConnection { //VALERIO BALDAZZI
	
	private DBConnection DBConn;
	
	@Before
	public void setup() {
		DBConn = DBConnection.ottieniIstanza();
	}
	
	@Test
	public void testGetConnectionNotNull() {
		Connection conn = DBConn.getConnection(); //verifico che dopo aver creato l'istanza, la connessione viene stabilita
		assertNotNull(conn);
	}
}
