package com.ebd.login.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ebd.login.beans.LoginBean;
import com.ebd.login.util.DataConnect;

public class LoginDAO {

	public static boolean validate(String user, String password) {
	///public static String validate(String user, String password) {
		Connection con = null;
		//PreparedStatement ps = null;
		PreparedStatement ps;
		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement("SELECT user_name, password, first_name FROM User WHERE user_name = ? AND password = ?");
			ps.setString(1, user);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {

				///return rs.getString(3);
				return true;
			}
			System.out.println("LoginDAO: Login was OK");
		} catch (SQLException ex) {
			System.out.println("LoginDAO: Login error -->" + 	ex.getMessage());
			return false;
			///return null;
		} finally {
			DataConnect.close(con);
		}
		return false;
		///return null;
	}
}