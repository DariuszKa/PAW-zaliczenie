package com.ebd.signup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.ebd.login.beans.Log;
import com.ebd.login.util.DataConnect;
import com.ebd.signup.jpa.User;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import static com.ebd.login.dao.LoginDAO.getRandom;
import static com.ebd.login.dao.LoginDAO.hidePassword;

public class SignupDAO {
    private static List<String> userBlackList = Arrays.asList("zzz", "root", "admin", "su", "adm", "@", "_");
    private static List<String> sqlBlackList = Arrays.asList("'", "\"", ";", ",", "*", "%", "/", "(", ")", "=", "--", "where", "from", "or", "drop", "delete", "select", "insert", "alter", "update", "join", "union", "null", "key", "order", "count", "avg", "sum", "group", "case", "having", "database", "table", "use", "show", "describe", "grant", "privil", "pass");
    private static Log log = new Log();

    public SignupDAO() {
        log.info("SignupDAO started");
    }

    public static boolean registerUser(User u) {
        String user = u.getLogin().toLowerCase();
        String password = u.getPassword();
        String nickName = u.getNickName();
        String email = u.getEmail();
        log.info(user, "SignupDAO: registerUser: loginMatches(user)=" + loginMatches(user) + ". passwordMatches(password)=" + passwordMatches(password));
        Connection con;
        PreparedStatement ps;
        try {
            Long waitTime = getRandom(400L, 4000L);
            //if (loginPasswordMatches(user, password) && !user.toLowerCase().contains((CharSequence) Arrays.asList(userBlackList))) {
            if (loginMatches(user) && passwordMatches(password) && !(userBlackList.stream().anyMatch(s -> user.contains(s.toLowerCase()))) && loginMatches(nickName) && emailMatches(email)) {
                log.info(user, "SignupDAO: registerUser: validation OK. user='" + user + "'. watitTime=" + waitTime);
                Thread.sleep(waitTime);
                con = DataConnect.getConnection();
                ResultSet rs;

                ps = con.prepareStatement("SELECT user_name FROM User WHERE user_name = ?");
                ps.setString(1, user);
                rs = ps.executeQuery();
                if (rs.next()) {
                    log.warning(user, "SignupDAO: registerUser: login juz istnieje!!!");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Wybierz inny login!", ""));
                    return false;
                }

                ps = con.prepareStatement("SELECT first_name FROM User WHERE first_name = ?");
                ps.setString(1, nickName);
                rs = ps.executeQuery();
                if (rs.next()) {
                    log.warning(user, "SignupDAO: registerUser: nick już istnieje");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Wybierz inny nick!", ""));
                    return false;
                }

                ps = con.prepareStatement("INSERT INTO user(user_type, user_name, first_name, email, password) values(?, ?, ?, ?, sha2(?, 224))");
                ps.setString(1, "requester");
                ps.setString(2, user);
                ps.setString(3, nickName);
                ps.setString(4, email);
                ps.setString(5, "*6" + user + password);
                log.info(user, "SignupDAO: registerUser: ps=>" + ps.toString() + "<");
                ps.execute();
                ps = con.prepareStatement("SELECT user_name, password, first_name FROM User WHERE user_name = ? AND password = sha2(?, 224)");
                ps.setString(1, user);
                ps.setString(2, "*6" + user + password);
                rs = ps.executeQuery();
                if (rs.next()) {
                    log.info(user, "SignupDAO: registerUser: rs OK, Registration was OK");
                    return true;
                } else {
                    log.warning(user, "SignupDAO: registerUser: rs WRONG, Registration was NOT ok");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Zmień parametry rejestracji", ""));
                    return false;
                }
                /*if (rs.next()) {
                    return true;
                }*/
            } else {
                log.warning(user, "SignupDAO: registerUser: matches: ERROR. user='" + user + "'. watitTime=" + waitTime);
                Thread.sleep(waitTime * 3);
                throw new IllegalArgumentException("user='" + user + "'. password=...");
            }
            //log.info(user,"SignupDAO: registerUser: Registration was OK");
        } catch (SQLException ex) {
            log.severe(user, "SignupDAO: registerUser: SQLException --> " + ex.getMessage());
            return false;
        } catch (Exception e) {
            log.severe(user, "SignupDAO: registerUser: Exception --> " + e.getMessage());
            //} finally {
            //	DataConnect.close(con);
        }
        return false;
    }

    public static boolean loginMatches(String str) {
        log.fine(str, "SignupDAO: loginMatches: started. str='" + str + "'");
        boolean check;
        //str = str.toLowerCase();
        if (sqlBlackList.stream().anyMatch(s -> str.toLowerCase().contains(s))) check = false;
            //else if( userBlackList.stream().anyMatch(s -> str.contains(s)) ) check = false;
        else check = str.toLowerCase().matches("[a-z][0-9a-z]{3,15}");
        log.info(str, "SignupDAO: loginMatches: finished. str='" + str + "'. check=" + check);
        return check;
    }

    /*public static boolean nickMatches(String str) {
        log.info(str,"SignupDAO: nickMatches: started. str='" + str + "'");
        boolean check;
        if( sqlBlackList.stream().anyMatch(s -> str.contains(s)) ) check = false;
        else if( userBlackList.stream().anyMatch(s -> str.contains(s)) ) check = false;
        else check = str.matches("[a-z][0-9a-z]{3,15}");
        log.info(str,"SignupDAO: nickMatches: finished. str='" + str + "'");
        return check;
    }*/

    public static boolean passwordMatches(String str) {
        boolean check;
        //if (str.toLowerCase().contains(Arrays.asList(sqlBlackList))) {
        if (sqlBlackList.stream().anyMatch(s -> str.contains(s))) check = false;
            //else check = (str.matches("[0-9A-Za-z\\.,_\\-!]{8,18}"));
            //else check = (str.matches("^[A-Za-z0-9][A-Za-z0-9\\._\\-]+@[A-Za-z0-9][A-Za-z0-9\\._\\-]+\\.[A-Za-z]{2,6}$"));
        else check = str.matches("[0-9A-Za-z\\.,_\\-!]{7,19}");
        log.info(str, "SignupDAO: passwordMatches: finished. str='" + hidePassword(str) + "'. check=" + check);
        return check;
    }

    public static boolean emailMatches(String str) {
        boolean check;
        //if (str.toLowerCase().contains(Arrays.asList(sqlBlackList))) {
        if (sqlBlackList.stream().anyMatch(s -> str.contains(s))) check = false;
            //else check = (str.matches("[0-9A-Za-z\\.,_\\-!]{8,18}"));
            //else check = (str.matches("^[A-Za-z0-9][A-Za-z0-9\\._\\-]+@[A-Za-z0-9][A-Za-z0-9\\._\\-]+\\.[A-Za-z]{2,6}$"));
            //else check = (str.matches("[A-Za-z][\\w._\\-]+@[\\w._\\-]+\\.[A-Za-z]{2,20}$"));
        else check = (str.matches("[A-Za-z][\\w._\\-]*@[\\w._\\-]+\\.[A-Za-z]{2,20}$"));
        if (!check) FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Popraw adres email", ""));
        return check;
    }

    public static boolean loginPasswordMatches(String login, String password) {
        //if(!loginMatches(login)) return false;
        //if(!passwordMatches(password)) return false;
        if (login.toLowerCase().equals(password.toLowerCase())) return false;
        if (compare(login, password, "\\W")) return false;
        if (compare(login, password, "\\D")) return false;
        if (compare(login, password, "\\w")) return false;
        if (compare(login, password, "\\d")) return false;
        /*String loginWord = login.toLowerCase().replaceAll("\\W", "");
        String passwordWord = password.toLowerCase().replaceAll("\\W", "");
        if(passwordWord.length()>1) if(loginWord.contains(passwordWord) || passwordWord.contains(loginWord)) return false;*/
        return true;
    }

    private static boolean compare(String login, String password, String removal) {
        String loginWord = login.toLowerCase().replaceAll(removal, "");
        String passwordWord = password.toLowerCase().replaceAll(removal, "");
        if (passwordWord.length() > 1)
            if (loginWord.contains(passwordWord) || passwordWord.contains(loginWord)) return false;
        return true;
    }
}