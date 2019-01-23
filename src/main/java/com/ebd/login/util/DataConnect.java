package com.ebd.login.util;

import com.ebd.login.beans.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.*;

public class DataConnect {
	private static Log log = new Log();

	public static Connection getConnection() {
		String dbDriver = "oracle.jdbc.OracleDriver";
		String dbName = "darekdb";
		String dbUser = "darek";
		String dbPW = readFile("C:\\Tomcat\\HT");
		String dbHost = "//localhost";
		String dbPort = "3306";
		String dbUrl = "jdbc:mysql:" + dbHost + ":" + dbPort + "/" + dbName + "?user=" + dbUser + "&password=" + dbPW + "&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
		try {
			Class.forName(dbDriver);
			Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPW);
			DatabaseMetaData dbmd = connection.getMetaData();
			log.fine("DataConnect: DatabaseProductVersion: " + dbmd.getDatabaseProductVersion() );
			return connection;
		} catch (SQLException e1) {
			log.severe("JDBC driver SQLException e1 --> " + e1.getMessage());
		} catch (ClassNotFoundException e2) {
			log.severe("JDBC driver ClassNotFoundException e2 --> " + e2.getMessage());
		} catch (StringIndexOutOfBoundsException e3) {
			log.severe("JDBC driver StringIndexOutOfBoundsException e3 --> " + e3.getMessage());
		} catch (Exception e4) {
			log.severe("JDBC driver Exception e4 --> " + e4.getMessage());
		}
		return null;
	}

	public static void close(Connection con) {
		try {
			con.close();
		} catch (Exception e) {
			log.severe(e.getMessage());
		}
	}

	private static String readFile(String path) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException ex1) {
			log.severe("DataConnect: readFile: FileNotFoundException ex1. path=" + path + "\n" + ex1.getMessage());
		}
		String line = null;
		/*StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");*/
		try {
			/*while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}*/
			//return stringBuilder.toString();
			line = reader.readLine();
		} catch(IOException ex2) {
			log.severe("DataConnect: readFile: FileNotFoundException ex2. path=" + path + "\n" + ex2.getMessage());
		} finally {
			try {
				reader.close();
			} catch (IOException ex3) {
				log.severe("DataConnect: readFile: FileNotFoundException ex3. path=" + path + "\n" + ex3.getMessage());
			}
		}
		//log.fine("DataConnect: readFile: FileNotFoundException ex3. pwd = '" + line + "'");
		//return stringBuilder.toString();
		return line;
	}

}