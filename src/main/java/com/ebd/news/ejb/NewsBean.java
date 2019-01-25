package com.ebd.news.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import com.ebd.login.beans.Log;
import com.ebd.login.util.DataConnect;
import com.ebd.news.jpa.News;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

@Stateless(mappedName = "NewsBean")
public class NewsBean {
    private static Log log = new Log();
    public static final int DEFAULT_COUNT = 20;

    private String volume;

    //@PersistenceUnit(unitName = "PawSignup")
    EntityManagerFactory emf;

    public NewsBean() {
        //System.out.println("'NewsBean' created...");
        log.fine("'NewsBean' created...");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean persist(Object o) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            log.fine("Saving object: " + o);
            entityManager.persist(o);
            //log.info("Object saved: " + o);
            return true;
        } catch (Exception e) {
            log.severe("NewsBean::persist: Error writing to DB: " + e.getMessage());
            log.severe("" + e.fillInStackTrace());
            //System.out.println("NewsBean::persist: Error writing to DB: " + e);
            return false;
        }
    }

    /*@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean remove(int id) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            //String query = "DELETE FROM News n WHERE n.id = " + id;
            Query q = entityManager.createQuery("DELETE FROM News n WHERE n.id = " + id);
            //Query q = entityManager.createQuery(query);
            q.executeUpdate();
            logger.info("Object removed");
            return true;
        } catch (Exception e) {
            logger.severe("NewsBean::remove: " + e);
            logger.severe("" + e.fillInStackTrace());
            System.out.println("NewsBean::remove: " + e);
            return false;
        }
    }*/

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean merge(Object o) {
        log.info("NewsBean: merge" + o);
        EntityManager entityManager = emf.createEntityManager();
        try {
            log.fine("Updating object: " + o);
            entityManager.merge(o);
            log.info("Object updated: ");
            return true;
        } catch (Exception e) {
            log.severe("NewsBean::merge: Error writing to DB: " + e);
            log.severe("" + e.fillInStackTrace());
            //System.out.println("NewsBean::merge: Error writing to DB: " + e);
            return false;
        }
    }

    //@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<News> getLatestNews(int count) {
        System.out.println("NewsBean: getLatestNews: count=" + count);
        if (count<0)
            count = DEFAULT_COUNT;
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("FROM News n ORDER BY n.createdAt DESC", News.class);
            if (count>0)
                query.setMaxResults(count);
            return query.getResultList();
        } catch (Exception e) {
            log.warning("NewsBean: getLatestNews: error while executing query: " + e);
            //System.out.println("NewsBean: getLatestNews: error while executing query: " + e);
        }
        return null;
    }

    //@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public News getNews(int id) {
        System.out.println("NewsBean: getNews started. id=" + id);
        log.info("NewsBean: getNews started. id=" + id);
        News news = null;
        Connection con = null;
        try {
            log.fine("NewsBean: getNews: before getConnection");
            con = DataConnect.getConnection();
            if(con==null) {
                log.warning("NewsBean: getNews: con is null!");
                //System.out.println("NewsBean: getNews: con is null!");
            }
            else{
                if(volume.matches("[0-9]?[A-Z]{1}[a-z]{0,2}")) {
                    log.info("NewsBean: getNews: con is NOT null!");
                    //System.out.println("NewsBean: getNews: con is NOT null!");
                    //System.out.println("NewsBean: getNews: id=" + id);
                    /*EntityManager em = emf.createEntityManager();
                    return em.find(News.class, id);*/
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("Select * FROM " + volume.replace(" ", "") + " WHERE id=" + id);
                    log.info("NewsBean: Columns in the table: " + rs.getMetaData().getTableName(1));
                    news = new News(volume, rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4));

                } else {
                    throw new IllegalArgumentException(" volume='" + volume + "'");
                }
            }
            con.close();
        } catch (Exception e) {
            log.severe(" NewsBean: rs error: " + e.getMessage());
            log.severe(" NewsBean: rs error: " + e);
            //return null;
        //} finally {
        //    DataConnect.close(con);
        }
        return news;
    }

    /*@TransactionAttribute(TransactionAttributeType.REQUIRED)
    private List<News> getNewsFromDate(Date fromDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("FROM News n WHERE n.updatedAt > '" + sdf.format(fromDate) + "' ORDER BY n.createdAt DESC", News.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.warning("NewsBean: getNewsFromDate: error while executing query: " + e);
            System.out.println("NewsBean: getNewsFromDate: error while executing query: " + e);
        }
        return null;
    }*/

}

