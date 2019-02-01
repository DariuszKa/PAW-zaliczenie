package com.ebd.login.beans;

import java.io.Serializable;
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
    private static final long serialVersionUID = 1094801825228386168L;
    private static Log log = new Log();
    private String pwd;
    private String msg;
    private String user;

    public String getPwd() {
        //return pwd;
        return "";
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


    public boolean getIsAdmin() {
        log.fine("LoginBean: getIsAdmin(): user = '" + user + "'. isAdmin = " + (user.contains("admin")));
        if(user.contains("admin")) return true;
        return false;
    }

    public String validateUsernamePassword() {
        log.info(user,"LoginBean: validate: Authorization started.");
        boolean valid = LoginDAO.validate(user, pwd);
        if(valid) {
            log.info(getUser(),"Authorization is valid for '" + getUser() + "'");
            HttpSession session = SessionUtils.getSession();
            //session = SessionUtils.getSession();
            session.setAttribute("username", user);
            return "list";
        } else {
            log.warning(getUser(),"Authorization was NOT valid for '" + getUser() + "'");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Zły login lub hasło",
                            "Podaj prawidłowy login oraz hasło"));
            return "index";
        }
    }

    public String logout() {
        HttpSession session = SessionUtils.getSession();
        log.info(getUser(),"logout for '" + getUser() + "' '" + session.getAttribute("username") + "'");
        session.invalidate();
        return "index";
    }

}
