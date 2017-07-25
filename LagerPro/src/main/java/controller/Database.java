package controller;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.apache.logging.log4j.Level;

public class Database {

	private static String Url = "jdbc:mysql://localhost:3306/wevotest";
	
	private static String IpAddress = "localhost";
	
	private static String User = "root";
	
	private static String Pass = "";
	
	private static Integer Port = 3306;
	
	
	
	public static boolean DatabaseCheck() { 
	    try (Socket s = new Socket(IpAddress, Port)) {
	        return true;
	    } catch (IOException ex) {
	        
	    }
	    return false;
	}

}

