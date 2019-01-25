package com.ebd.news.ejb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
//import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.ebd.login.beans.Log;
import com.ebd.login.util.DataConnect;
import com.ebd.news.beans.*;
import com.ebd.news.jpa.*;

@ManagedBean
@SessionScoped
public class NewsManagedBean implements Serializable {
    private static final long serialVersionUID = -7962404280558108365L;
    private static Log log = new Log();
    //protected Logger logger = Logger.getLogger("PawNewsExample");

    protected News news;
    protected int id;
    protected int title;
    protected String content;
    protected String description;
    protected String volume;

    @ManagedProperty(value="#{newsLookupDatabaseBean}")
    private NewsLookupService newsLookupService;

    public NewsManagedBean() {
        //super();
        ///System.out.println("NewsManagedBean bean created...");
        ///logger.info("NewsManagedBean bean created...");
        log.fine("NewsManagedBean bean created...");
        news = new News();
    }

    public NewsLookupService getNewsLookupService() {
        return newsLookupService;
    }

    public void setNewsLookupService(NewsLookupService newsLookupService) {
        this.newsLookupService = newsLookupService;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        log.fine("NewsManagedBean: setId(int id) started. id=" + id);
        this.id = id;
        log.fine("NewsManagedBean: setId(int id): 1");
        if(id==0) {
            log.fine("NewsManagedBean: setId(int id): 2");
            this.news = null;
            this.title = 0;
            this.content = "";
            this.description = "";
        }
        if (this.newsLookupService != null && id != 0) {
            log.fine("NewsManagedBean: setId(int id): 6");
            this.news = getNews(id);
            log.fine("NewsManagedBean: setId(int id): 7");
            if (this.news != null) {
                log.fine("NewsManagedBean: setId(int id): 8");
<<<<<<< HEAD
                this.title = this.news.getChapter();
=======
                this.title = this.news.getTitle();
>>>>>>> 43b8fa438986623911b051a97c796ec2f2be194f
                this.content = this.news.getContent();
                log.fine("NewsManagedBean: setId(int id): 10");
            }
        } else log.fine("NewsManagedBean: setId(int id): 11");
        log.fine("NewsManagedBean: setId(int id) finished. id=" + id);
    }

    public News getNews(int id) {
        log.fine("NewsManagedBean: getNews started. id=" + id);
        Connection con = DataConnect.getConnection();
        if(con==null) {
            //System.out.println("NewsManagedBean: getNews: connection is null!");
            log.warning("NewsManagedBean: getNews: connection is null!");
        }
        else {
            //System.out.println("NewsManagedBean: getNews: connection is NOT null.");
            log.fine("NewsManagedBean: getNews: connection is NOT null.");
            try {
                Statement stmt = con.createStatement();
<<<<<<< HEAD
                if(volume.matches("[0-9]?[A-Z][a-z]{0,2}")) {
                    String query = "SELECT * FROM _" + volume + " WHERE id=" + id;
=======
                if(volume.matches("[0-9]?[A-Z]{1}[a-z]{0,2}")) {
                    String query = "SELECT * FROM " + volume + " WHERE id=" + id;
>>>>>>> 43b8fa438986623911b051a97c796ec2f2be194f
                    log.fine("NewsManagedBean: getNews: query = >" + query + "<");
                    ResultSet rs = stmt.executeQuery(query);
                    log.fine("NewsManagedBean: getNews: Columns in the table: " + rs.getMetaData().getTableName(1));
                    int columnCount = rs.getMetaData().getColumnCount();
                    log.fine("NewsManagedBean: getNews: columnCount=" + columnCount);
                    for (int i = 1; i <= columnCount; i++) {
                        log.fine(rs.getMetaData().getColumnName(i) + " ");
                    }
                    //System.out.println();
                    while (rs.next()) {
                        /*for (int i = 1; i <= 2; i++)
                            System.out.print(rs.getInt(i) + "\t");
                        for (int i = 3; i <= 4; i++)
                            System.out.print(rs.getString(i) + "\t");
                        for (int i = 5; i <= 6; i++)
                            System.out.print(rs.getTimestamp(i) + "\t");
                        /*for (int i = 5; i < 6; i++)
                            System.out.print(rs.getTimestamp(i) + "\t");*/
                        //System.out.print(getRes(rs, i) + "\t");
                            /*String string;
                            if(rs.wasNull()) string = "NULL";
                            else string = rs.getString(i);
                            System.out.print(string + "\t");
                            System.out.println();*/
                        News news = new News(volume, rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4));
                        return news;
                    }
                }else {
                    throw new IllegalArgumentException(" volume='" + volume + "'");
                }
            } catch (Exception e) {
                log.warning("NewsManagedBean: getNews: rs error: " + e.getMessage());
                //System.out.println("NewsManagedBean: getNews: " + e.getMessage());
                log.warning("NewsManagedBean: getNews: " + e);
                //System.out.println("NewsManagedBean: getNews: " + e);
<<<<<<< HEAD
            //} finally {
            //    DataConnect.close(con);
=======
            } finally {
                DataConnect.close(con);
>>>>>>> 43b8fa438986623911b051a97c796ec2f2be194f
            }
        }
        return null;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String update() {
        Connection con = DataConnect.getConnection();
        if(con==null) {
            log.warning("NewsManagedBean: update: connection is null!");
        }
        else {
            log.fine("NewsManagedBean: update: connection is NOT null.");
            try {
                Statement stmt = con.createStatement();
                if (! volume.matches("[0-9]?[A-Z]{1}[a-z]{0,2}")) throw new IllegalArgumentException("volume='" + volume + "'");
<<<<<<< HEAD
                    String update = "UPDATE _" + volume + " SET content = '" + content + "' WHERE id=" + id;
                    //log.info("NewsManagedBean: update: update = >" + update + "<");
                    System.out.println("NewsManagedBean: update: update = >" + update + "<");
=======
                    String update = "UPDATE " + volume + " SET content = '" + content + "' WHERE id=" + id;
                    log.info("NewsManagedBean: update: update = >" + update + "<");
>>>>>>> 43b8fa438986623911b051a97c796ec2f2be194f
                    int result = stmt.executeUpdate(update);
                    log.info("NewsManagedBean: getNews: result=" + result);
            } catch (Exception e) {
                log.warning("NewsManagedBean: getNews: rs error: " + e.getMessage());
                //System.out.println("NewsManagedBean: getNews: " + e.getMessage());
                log.warning("NewsManagedBean: getNews: " + e);
                //System.out.println("NewsManagedBean: getNews: " + e);
<<<<<<< HEAD
            //} finally {
            //    DataConnect.close(con);
=======
            } finally {
                DataConnect.close(con);
>>>>>>> 43b8fa438986623911b051a97c796ec2f2be194f
            }
        }
        return null;
    }

    /*public String add() {
        FacesContext context = FacesContext.getCurrentInstance();
        this.news = new News();
        this.news.setUpdatedAt(new Date());
        this.news.setCreatedAt(new Date());
        this.news.setContent(content);
        this.news.setTitle(title);
        if (this.newsLookupService.persist(this.news)) {
            context.addMessage(null, new FacesMessage("Dodano nowego newsa."));
        }
        else {
            context.addMessage(null, new FacesMessage("Błąd podczas dodania newsa."));
            return null;
        }
        return "list";//"edit.xhtml?id=" + news.getId();
    }*/

}
