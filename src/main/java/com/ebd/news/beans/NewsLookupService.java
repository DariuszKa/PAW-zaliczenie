package com.ebd.news.beans;


import java.util.List;

import com.ebd.news.jpa.News;


public interface NewsLookupService {
    public News getNews(int id);
    public List<News> getAllNews();
    public boolean merge(News news);
    public boolean persist(News news);
}
