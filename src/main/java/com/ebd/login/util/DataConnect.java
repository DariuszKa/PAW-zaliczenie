package com.ebd.login.util;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;

import java.sql.*;
////import java.util.logging.Logger;

public class DataConnect {
	////private Logger logger = Logger.getLogger("PGE-WEB");

	public static Connection getConnection() {
		String dbDriver = "oracle.jdbc.OracleDriver";
		String dbName = "darekdb";
		String dbUser = "darek";
		String dbPW = "konopka";
		String dbHost = "//localhost";
		String dbPort = "3306";
		String dbUrl = "jdbc:mysql:" + dbHost + ":" + dbPort + "/" + dbName + "?user=" + dbUser + "&password=" + dbPW + "&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
		//System.out.println("\ndbUrl='" + dbUrl + "'");
		try {
			Class.forName(dbDriver);
			Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPW);
			DatabaseMetaData dbmd = connection.getMetaData();
			System.out.println("DataConnect: DatabaseProductVersion: " + dbmd.getDatabaseProductVersion() );
			////logger.info("DataConnect: DatabaseProductVersion: " + dbmd.getDatabaseProductVersion() );
			return connection;
		} catch (SQLException e1) {
			System.err.println("JDBC driver SQLException e1 --> " + e1.getMessage());
		} catch (ClassNotFoundException e2) {
			System.err.println("JDBC driver ClassNotFoundException e2 --> " + e2.getMessage());
		} catch (StringIndexOutOfBoundsException e3) {
			System.err.println("JDBC driver StringIndexOutOfBoundsException e3 --> " + e3.getMessage());
		} catch (Exception e4) {
			System.err.println("JDBC driver Exception e4 --> " + e4.getMessage());
		}
		return null;

		/*try{
			Class.forName(dbDriver);
			System.out.println("\n06: dbUrl = '" + dbUrl + "'");
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPW);
			System.out.println("=====  Database info =====");
			conn.setAutoCommit(false);
			DatabaseMetaData dbmd = conn.getMetaData();
			System.out.println("DatabaseProductName: " + dbmd.getDatabaseProductName() );
			System.out.println("DatabaseProductVersion: " + dbmd.getDatabaseProductVersion() );
			System.out.println("DatabaseMajorVersion: " + dbmd.getDatabaseMajorVersion() );
			System.out.println("DatabaseMinorVersion: " + dbmd.getDatabaseMinorVersion() );
			System.out.println("=====  Driver info =====");
			System.out.println("DriverName: " + dbmd.getDriverName() );
			System.out.println("DriverVersion: " + dbmd.getDriverVersion() );
			System.out.println("DriverMajorVersion: " + dbmd.getDriverMajorVersion() );
			System.out.println("DriverMinorVersion: " + dbmd.getDriverMinorVersion() );
			System.out.println("=====  JDBC/DB attributes =====");
			System.out.print("Supports getGeneratedKeys(): ");
			if (dbmd.supportsGetGeneratedKeys() ) System.out.println("true");
			else System.out.println("false");
			System.out.println("Success 06");
		} catch (SQLException e1) {
			System.err.println("JDBC driver SQLException e1 --> " + e1.getMessage());
		} catch (ClassNotFoundException e2) {
			System.err.println("JDBC driver ClassNotFoundException e2 --> " + e2.getMessage());
		} catch (StringIndexOutOfBoundsException e3) {
			System.err.println("JDBC driver StringIndexOutOfBoundsException e3 --> " + e3.getMessage());
		} catch (Exception e4) {
			System.err.println("JDBC driver Exception e4 --> " + e4.getMessage());
		}
		try{
			System.out.println("\n08: dbUrl = '" + dbUrl + "'");
			Class.forName(dbDriver);
			Connection con = DriverManager.getConnection( dbUrl,dbUser,dbPW);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("Select * From User");
			System.out.println("The columns in the table: " + rs.getMetaData().getTableName(1));
			int columnCount = rs.getMetaData().getColumnCount();
			for  (int i = 1; i<= columnCount; i++){
				System.out.print(rs.getMetaData().getColumnName(i) + " ");
			}
			System.out.println();
			while(rs.next()) {
				for(int i = 1; i<=2; i++)
					System.out.print(rs.getInt(i) + "\t");
				for (int i = 3; i<columnCount; i++){
					System.out.print(rs.getString(i) + "\t");
				}
				System.out.println();
			}
			System.out.println("Success 08");
			return con;
		}catch(Exception e){
			System.err.println(e.getMessage());
			System.err.println(e);
		}
		System.err.println("There is a problem!");
		return null;*/
	}

	public static void close(Connection con) {
		try {
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}