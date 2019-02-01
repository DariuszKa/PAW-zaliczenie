package com.ebd.login.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import com.ebd.login.beans.Log;
import com.ebd.login.util.DataConnect;
import static com.ebd.signup.dao.SignupDAO.loginMatches;
import static com.ebd.signup.dao.SignupDAO.passwordMatches;
import static com.ebd.signup.dao.SignupDAO.loginPasswordMatches;

public class LoginDAO {
	private static Log log = new Log();

	public static boolean validate(String user, String password) {
		user = user.toLowerCase();
		log.info(user, "LoginDAO: validate: started. user='" + user + "'");
		Connection con;
		PreparedStatement ps;
		try {
			Long waitTime = getRandom(280L, 2800L);
			//if(loginPasswordMatches(user, password)) {
			if(loginMatches(user) && passwordMatches(password)) {
				log.info(user, "LoginDAO: validate: OK. user='" + user + "'. watitTime=" + waitTime);
				Thread.sleep(waitTime);
				con = DataConnect.getConnection();
				String query = "SELECT user_name, password, first_name FROM User WHERE user_name = ? AND password = sha2(?, 224)";
				ps = con.prepareStatement(query);
				ps.setString(1, user);
				ps.setString(2, "*6" + user + password);
                /*String query = "SELECT user_name, password, first_name FROM User WHERE user_name = '" + user + "' AND password = sha2('" + password + "', 224)";
                log.info(user, "LoginDAO: query=>" + query + "<");
                ps = con.prepareStatement(query);*/
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					return true;
				} else{
                    log.warning(user, "LoginDAO: matches: no results! user='" + user + "'. watitTime=" + waitTime);
                    //log.warning(user, "LoginDAO: query=>" + query + "<");
                    //return false;
                }
			} else {
				log.warning(user, "LoginDAO: matches: ERROR. user='" + user + "'. watitTime=" + waitTime);
				Thread.sleep(waitTime*10);
				throw new IllegalArgumentException("user='" + user + "'. password='" + hidePassword(password) + "'");
			}
			//log.info(user,"LoginDAO: validate: Login was OK");
		} catch (SQLException ex) {
			log.severe(user,"LoginDAO: validate: Login SQLException --> " + ex.getMessage());
			//return false;
		} catch (Exception e) {
			log.severe(user,"LoginDAO: validate: Login Exception --> " + e.getMessage());
		}
		return false;
	}

	public static long getRandom(long min, long max) {
		Random r = new Random();
		return min +((long)(r.nextDouble()*(max-min)));
	}

	public static String hidePassword(String password) {
		return password.replaceAll(".","*");
	}
}