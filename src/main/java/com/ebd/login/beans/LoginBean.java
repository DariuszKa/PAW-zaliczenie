package com.ebd.login.beans;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.ebd.login.util.SessionUtils;
import com.ebd.login.dao.LoginDAO;

@ManagedBean
//@SessionScoped
@ApplicationScoped
public class LoginBean implements Serializable {
    //private static final long serialVersionUID = 1094801825228386363L;
    private String pwd;
    private String msg;
    private String user;
    private HttpSession session;
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

    public HttpSession geSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public boolean getIsAdmin() {
        System.out.println("LoginBean: isAdmin(): user = '" + user + "'. isAdmin = " + (user.contains("admin")));
        if(user.contains("admin")) return true;
        return false;
    }

    public String validateUsernamePassword() {
        boolean valid = LoginDAO.validate(user, pwd);
        if(valid) {
            logger.info("Authorization is valid for '" + getUser() + "'");
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
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
        session.invalidate();
        return "index";
    }
}
