package com.ebd.login.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.ebd.login.util.SessionUtils;
import com.ebd.login.dao.LoginDAO;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1094801825228386363L;
    private String pwd;
    private String msg;
    private String user;
    //private HttpSession session;
    private Logger logger = Logger.getLogger("PGE-WEB");

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /*private HttpSession getSession() {
        return session;
    }

    private void setSession(HttpSession session) {
        this.session = session;
    }*/

    public boolean getIsAdmin() {
        System.out.println("LoginBean: getIsAdmin(): user = '" + user + "'. isAdmin = " + (user.contains("admin")));
        if(user.contains("admin")) return true;
        return false;
    }

    public String validateUsernamePassword() {
        boolean valid = LoginDAO.validate(user, pwd);
        if(valid) {
            logger.info("Authorization is valid for '" + getUser() + "'");
            HttpSession session = SessionUtils.getSession();
            //session = SessionUtils.getSession();
            session.setAttribute("username", user);
            return "list";
        } else {
            logger.warning("Authorization was NOT valid for " + getUser());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Zły login lub hasło",
                            "Podaj prawidłowy login oraz hasło"));
            return "index";
        }
    }

    public String logout() {
        HttpSession session = SessionUtils.getSession();
        logger.info("logout for '" + getUser() + "' '" + session.getAttribute("username") + "'");
        session.invalidate();
        return "index";
    }
}
