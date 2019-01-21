package com.ebd.login.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.ebd.login.util.DataConnect;

public class LoginDAO {

	public static boolean validate(String user, String password) {
		Connection con = null;
		PreparedStatement ps;
		try {
			//Long waitTime = new Random(2000).nextLong() + new Random(1000).nextLong() + new Random(500).nextLong() + 100L;
			Long waitTime = getRandom(280L, 2800L);
			if( user.matches("[0-9A-Za-z]{5,15}") && password.matches("[0-9A-Za-z\\.,\\-_!?]{5,15}") && !user.toLowerCase().equals(password.toLowerCase()) ) {
				System.out.println("LoginDAO: validate: OK. user='" + user + "'. watitTime=" + waitTime);
				Thread.sleep(waitTime);
				con = DataConnect.getConnection();
				ps = con.prepareStatement("SELECT user_name, password, first_name FROM User WHERE user_name = ? AND password = ?");
				ps.setString(1, user);
				ps.setString(2, password);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					///return rs.getString(3);
					return true;
				}
			} else {
				System.out.println("LoginDAO: validate: ERROR. user='" + user + "'. watitTime=" + waitTime);
				Thread.sleep(waitTime*10);
				throw new IllegalArgumentException("user='" + user + "'. password='" + password + "'");
			}
			System.out.println("LoginDAO: Login was OK");
		} catch (SQLException ex) {
			System.out.println("LoginDAO: Login error -->" + ex.getMessage());
			return false;
			///return null;
		} catch (Exception e) {
			System.out.println("LoginDAO: Login error -->" + e.getMessage());
		} finally {
			DataConnect.close(con);
		}
		return false;
		///return null;
	}

	private static long getRandom(long min, long max) {
		Random r = new Random();
		return min +((long)(r.nextDouble()*(max-min)));
	}
}