package com.ebd.login.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

import com.ebd.login.beans.Log;
import com.ebd.login.util.DataConnect;

public class LoginDAO {
	private static Log log = new Log();
<<<<<<< HEAD
	private static String[] userNameBlackList = {"zzz", "root", "null", "\"", ";", "drop", "delete", "select", "insert", "alter", "update", "'"};
=======
>>>>>>> 43b8fa438986623911b051a97c796ec2f2be194f

	public static boolean validate(String user, String password) {
		Connection con = null;
		PreparedStatement ps;
		try {
			Long waitTime = getRandom(280L, 2800L);
<<<<<<< HEAD
			if(userMatches(user) && passwordMatches(password)) {
=======
			if( user.matches("[0-9A-Za-z]{5,15}") && password.matches("[0-9A-Za-z\\.,\\-_!?]{5,15}") && !user.toLowerCase().equals(password.toLowerCase()) ) {
>>>>>>> 43b8fa438986623911b051a97c796ec2f2be194f
				log.info(user, "LoginDAO: validate: OK. user='" + user + "'. watitTime=" + waitTime);
				Thread.sleep(waitTime);
				con = DataConnect.getConnection();
				ps = con.prepareStatement("SELECT user_name, password, first_name FROM User WHERE user_name = ? AND password = ?");
				ps.setString(1, user);
				ps.setString(2, password);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					return true;
				}
			} else {
<<<<<<< HEAD
				log.warning(user, "LoginDAO: matches: ERROR. user='" + user + "'. watitTime=" + waitTime);
=======
				log.warning(user, "LoginDAO: validate: ERROR. user='" + user + "'. watitTime=" + waitTime);
>>>>>>> 43b8fa438986623911b051a97c796ec2f2be194f
				Thread.sleep(waitTime*10);
				throw new IllegalArgumentException("user='" + user + "'. password='" + password + "'");
			}
			log.info(user,"LoginDAO: Login was OK");
		} catch (SQLException ex) {
<<<<<<< HEAD
			log.severe(user,"LoginDAO: Login SQLException -->" + ex.getMessage());
			return false;
		} catch (Exception e) {
			log.severe(user,"LoginDAO: Login Exception -->" + e.getMessage());
		//} finally {
		//	DataConnect.close(con);
=======
			log.severe(user,"LoginDAO: Login error -->" + ex.getMessage());
			return false;
		} catch (Exception e) {
			log.severe(user,"LoginDAO: Login error -->" + e.getMessage());
		} finally {
			DataConnect.close(con);
>>>>>>> 43b8fa438986623911b051a97c796ec2f2be194f
		}
		return false;
	}

	private static long getRandom(long min, long max) {
		Random r = new Random();
		return min +((long)(r.nextDouble()*(max-min)));
	}

	public static boolean userMatches(String user) {
		if (Arrays.asList(userNameBlackList).contains(user)) return false;
		return user.matches("[0-9A-Za-z]{5,15}");
	}

	public static boolean passwordMatches(String password) {
		return password.matches("[0-9A-Za-z\\.,\\-_!?]{5,15}");
	}

}