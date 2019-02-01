package com.ebd.signup.beans;

import java.io.Serializable;
import com.ebd.login.beans.Log;
import com.ebd.signup.dao.SignupDAO;
import com.ebd.signup.jpa.User;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class SignupBean implements Serializable {
    private static final long serialVersionUID = 901420472876671027L;
    private static Log log = new Log();
    private String login;
    private String password;
    private String nickName;
    private String email;
    private String displayLogin = "block";
    private String displaySignup = "none";

    public SignupBean() {
        Log.info("SignupBean started");
    }

    public String getDisplayLogin() {
        return displayLogin;
    }

    public void setDisplayLogin(String displayLogin) {
        this.displayLogin = displayLogin;
    }

    public String getDisplaySignup() {
        return displaySignup;
    }

    public void setDisplaySignup(String displaySignup) {
        this.displaySignup = displaySignup;
    }

    public void toggle() {
        log.info(login, "SignupBean: toggle started.");
        String buffer = displayLogin;
        displayLogin = displaySignup;
        displaySignup = buffer;
    }

    public String doSignup() {
        log.info("SignupBean: wywolanie metody: doSignup");
        displayLogin = "none";
        displaySignup = "block";
        return "index";
    }

    public String doLogin() {
        log.info("SignupBean: wywolanie metody: doLogin");
        displayLogin = "block";
        displaySignup = "none";
        return "index";
    }

    public String getLogin() {
        if(login==null) return "";
        else return login.toLowerCase();
    }

    public void setLogin(String login) {
        log.info("SignupBean: wywolanie metody: setLogin");
        this.login = login;
    }

    public String getPassword() {
        //return password;
        return "";
    }

    public void setPassword(String password) {
        log.info("SignupBean: wywolanie metody: setPassword");
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        log.info("SignupBean: wywolanie metody: setNickName");
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        log.info("SignupBean: wywolanie metody: setEmail");
        this.email = email;
    }

    /*public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }*/

    @Override
    public String toString() {
        log.info("SignupBean: wywolanie metody: toString");
        String output = "SignupBean: toString:{" +
                "nickName='" + login + "'" +
                ", lastName='" + nickName + "'" +
                ", password='" + password + "'" +
                ", email='" + email + "'" +
                '}';
        log.info("SignupBean: wywolanie metody: toString");
        return output;
    }

    public String register() {
        log.info(login,"SignupBean: register: Registration started.");
        User u = new User();
        u.setLogin(this.login);
        u.setFirstName(this.nickName);
        u.setPassword(this.password);
        u.setEmail(this.email);
        boolean valid = SignupDAO.registerUser(u);
        if(valid) {
            log.info(u.getLogin(),"SignupBean: register: Registration is valid for '" + u.getLogin() + "'");
            log.info(u.getLogin(),"SignupBean: register: user = " + u);
            /*HttpSession session = SessionUtils.getSession();
            session.setAttribute("username", user);*/
            displayLogin = "block";
            displaySignup = "none";
            return "signup-success";
            //return "";
        } else {
            log.warning(u.getLogin(),"SignupBean: register: Registration was NOT valid for '" + u.getLogin() + "'");
            log.warning(u.getLogin(),"SignupBean: register: user = " + u);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Zarejestrowanie użytkownika jest niemożliwe", ""));
            //return "signup-fail";
            //return "signup";
            //return "signup";
            return "index";
        }

    }

}