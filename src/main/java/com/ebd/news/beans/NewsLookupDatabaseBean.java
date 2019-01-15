package com.ebd.news.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.ebd.news.ejb.NewsBean;
import com.ebd.news.jpa.News;


@ManagedBean(eager = true)
@ApplicationScoped
//public class NewsLookupDatabaseBean implements NewsLookupService, Serializable {
public class NewsLookupDatabaseBean implements NewsLookupService, Serializable {

    private static final long serialVersionUID = -5442331602203781978L;

    protected Logger logger = Logger.getLogger("PawNewsExample");

    @EJB(name = "NewsBean")
    private NewsBean newsBean;

    public NewsLookupDatabaseBean() {
        System.out.println("NewsLookupDatabaseBean bean created");
        logger.info("NewsLookupDatabaseBean bean created");
    }

    public NewsBean getNewsBean() {
        return newsBean;
    }

    public void setNewsBean(NewsBean newsBean) {
        this.newsBean = newsBean;
    }

    @Override
    public List<News> getAllNews() {
        //return newsBean.getLatesvalue = tNews(0);
        return newsBean.getLatestNews(0);
    }

    @Override
    public News getNews(int id) {
        // TODO Auto-generated method stub
        System.out.println("NewsLookupDatabaseBean: getNews started. id=" + id);
        logger.info("NewsLookupDatabaseBean: getNews started. id=" + id);
        News n = newsBean.getNews(id);
        System.out.println("NewsLookupDatabaseBean: getNews: n==null = " + (n==null));
        logger.info("NewsLookupDatabaseBean: getNews: n==null = " + (n==null));
        if (n == null)
            logger.info("NewsLookupDatabaseBean returning null for id=" + id);
        else
            logger.info("News " + id + "title: " + n.getTitle());
        logger.info("NewsLookupDatabaseBean: getNews finished. id=" + id);
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

    @Override
    public boolean remove(int id) {
        return newsBean.remove(id);
    }

}
