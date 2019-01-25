package com.ebd.news.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.ebd.login.beans.Log;
import com.ebd.news.ejb.NewsBean;
import com.ebd.news.jpa.News;


@ManagedBean(eager = true)
@ApplicationScoped
public class NewsLookupDatabaseBean implements NewsLookupService, Serializable {

    private static final long serialVersionUID = -5442331602203781978L;
    private static Log log = new Log();
    //protected Logger logger = Logger.getLogger("PawNewsExample");

    @EJB(name = "NewsBean")
    private NewsBean newsBean;

    public NewsLookupDatabaseBean() {
        //System.out.println("NewsLookupDatabaseBean bean created");
        log.info("NewsLookupDatabaseBean bean created");
    }

    /*public NewsBean getNewsBean() {
        return newsBean;
    }

    public void setNewsBean(NewsBean newsBean) {
        this.newsBean = newsBean;
    }*/

    @Override
    public List<News> getAllNews() {
        return newsBean.getLatestNews(0);
    }

    @Override
    public News getNews(int id) {
        // TODO Auto-generated method stub
        //System.out.println("NewsLookupDatabaseBean: getNews started. id=" + id);
        log.info("NewsLookupDatabaseBean: getNews started. id=" + id);
        News n = newsBean.getNews(id);
        //System.out.println("NewsLookupDatabaseBean: getNews: n==null = " + (n==null));
        log.info("NewsLookupDatabaseBean: getNews: n==null = " + (n==null));
        if (n == null)
            log.info("NewsLookupDatabaseBean returning null for id=" + id);
        else
<<<<<<< HEAD
            log.info("News " + id + "title: " + n.getChapter());
=======
            log.info("News " + id + "title: " + n.getTitle());
>>>>>>> 43b8fa438986623911b051a97c796ec2f2be194f
        //log.info("NewsLookupDatabaseBean: getNews finished. id=" + id);
        return n;
    }

    @Override
    public boolean merge(News news) {
        return newsBean.merge(news);
    }

    @Override
    public boolean persist(News news) {
        return newsBean.persist(news);
    }

}
