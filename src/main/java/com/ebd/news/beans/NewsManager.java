package com.ebd.news.beans;

import com.ebd.login.beans.Log;
import com.ebd.login.dao.LoginDAO;
import com.ebd.login.util.DataConnect;
import com.ebd.news.jpa.News;
import com.ebd.login.beans.LoginBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@ManagedBean
//@RequestScoped
@SessionScoped
public class NewsManager implements Serializable {
    private static final long serialVersionUID = 3754037223174977077L;
    private static Log log = new Log();
    private static final int resultPerPage = 10;

    private List<News> allNewsList = new ArrayList<>();
    private List<News> newsList = new ArrayList<>();

    Map<String,String> bibleMap = new LinkedHashMap<String,String>(23);

    private String volume;
    private String volumeLong;
    private String user;

    private String wasRead;

    //private Logger logger = Logger.getLogger("PGE-WEB");

    @ManagedProperty(value="#{newsLookupDatabaseBean}")
    private NewsLookupDatabaseBean newsLookupService;

    //atrybuty potrzebne do wyświetlania listy newsów
    private int page = 1, nextPage = 1, lastPage = 1, firstPage = 1, previousPage = 1;
    private int allPages = 0;
    private long recordsCount = 0;
    private String tableListCaption = "Brak rekordów do wyświetlenia";

    public NewsManager() {
        log.info("NewsManager bean created.");

        Connection connection = DataConnect.getConnection();
        if(connection==null)
            log.warning("NewsManager: connection is null!");
        else {
            log.info("NewsManager: connection is NOT null");
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM bible");
                /*log.fine("NewsManager: Columns in the table: " + rs.getMetaData().getTableName(1));
                int columnCount = rs.getMetaData().getColumnCount();
                log.fine("NewsManager: columnCount=" + columnCount);*/
                log.info("NewsManager: There are " + rs.getMetaData().getColumnCount() + " columns in the table: " + rs.getMetaData().getTableName(1));
                /*for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getMetaData().getColumnName(i) + " ");
                }
                System.out.println(); */
                bibleMap.clear();
                while (rs.next()) {
                    /*for (int i = 1; i <= 1; i++)
                        System.out.print(rs.getInt(i) + "|");
                    for (int i = 2; i <= 3; i++)
                        System.out.print(rs.getString(i) + "|");*/
                    bibleMap.put(rs.getString(2), rs.getString(3));
                }
            } catch (Exception e) {
                log.warning(" NewsManager: rs error: " + e.getMessage());
                //System.out.println(" NewsManager: rs error: " + e.getMessage());
                log.warning(" NewsManager: rs error: " + e);
                //System.out.println(" NewsManager: error: " + e);
            }
            log.fine("NewsManager: bibleMap.size()=" + bibleMap.size());
        }
    }

    public void createList() {
        if(user==null || volume==null) log.severe("NewsManager: createList: user='" + user + "'. volume='" + volume + "'");
        else {
            log.info("NewsManager: createList: user='" + user + "'. volume='" + volume + "'");
            //allNewsList.clear();
            //newsList.clear();
            allNewsList = new ArrayList<>();
            newsList = new ArrayList<>();
            Connection connection = DataConnect.getConnection();
            if (connection == null)
                log.warning("NewsManager: createList: connection is null!");
            else {
                log.info("NewsManager: createList: connection is NOT null");
                try {
                    if (volume.matches("[0-9]?[A-Z]{1}[a-z]{0,2}")) {
                        Statement stmt = connection.createStatement();
                        //String query = "Select * From " + volume.replace(" ", "");
                        String query = "SELECT * FROM _" + volume;
                        log.info("NewsManager: createList: query = >" + query + "<");
                        ResultSet rs = stmt.executeQuery(query);
                        log.fine("NewsManager: createList: Columns in the table: " + rs.getMetaData().getTableName(1));
                        int columnCount = rs.getMetaData().getColumnCount();
                        log.fine("NewsManager: createList: columnCount=" + columnCount);
                        for (int i = 1; i <= columnCount; i++) {
                            log.fine(rs.getMetaData().getColumnName(i) + " ");
                        }
                        //System.out.println();
                        //allNewsList.clear();
                        while (rs.next()) {
                        /*for (int i = 1; i <= 2; i++)
                            System.out.print(rs.getInt(i) + "\t");
                        for (int i = 3; i <= 4; i++)
                            System.out.print(rs.getString(i) + "\t");
                        for (int i = 5; i <= 6; i++)
                            System.out.print(rs.getTimestamp(i) + "\t");
                        /*for (int i = 5; i < 6; i++)
                            System.out.print(rs.getTimestamp(i) + "\t");*/

                            int chapter = rs.getInt(2);
                            String queryUser = "SELECT was_read FROM zzzzur__" + user + " WHERE chapter=" + chapter;
                            log.info("NewsManager: createList: queryUser = >" + queryUser + "<");
                            Statement stmtUser = connection.createStatement();
                            ResultSet rsUser = stmtUser.executeQuery(queryUser);
                            String wasRead = rsUser.next() ? "Y" : "N";
                            //                  id     title/chapter             content       was_read
                            News news = new News(rs.getInt(1), chapter, rs.getString(3), wasRead);
                            allNewsList.add(news);
                        }
                        log.fine("NewsManager: createList: allNewsList.size()=" + allNewsList.size());
                        int size = Math.min(allNewsList.size(), resultPerPage);
                        log.fine("NewsManager: createList: size=" + size);
                        if (size > 0) {
                            newsList = allNewsList.subList(0, size);
                            calculatePages();
                            generateTableCaption();
                        } else
                            tableListCaption = "brak rekordów!";
                    } else {
                        log.warning(user,"NewsManager: createList: volume='" + volume + "'");
                        throw new IllegalArgumentException(" volume='" + volume + "'");
                    }
                } catch (Exception e) {
                    tableListCaption = "Brak danych dla księgi: " + volume;
                    log.severe(user,"NewsManager: createList: rs error: " + e.getMessage());
                    //log.warning(user,"NewsManager: createList: rs error: " + e);
                }
                log.info("NewsManager: createList: after newsList.size()=" + newsList.size());
            }
        }
    }

    public static String getRes(ResultSet rs, int i) {
        String result;
        try{
            result = rs.getTimestamp(i).toString();
        } catch(Exception e) {
            try {
                result = rs.getString(i);
            }
            catch(Exception ex) {
                result = "NULL";
            }
        }
        return result;
    }


    /*public NewsLookupDatabaseBean getNewsLookupService() {
        return newsLookupService;
    }

    public void setNewsLookupService(NewsLookupDatabaseBean newsLookupService) {
        logger.info("SetNewsBean() invoked. Trybing to receive message list.");
        this.newsLookupService = newsLookupService;
        if (this.newsLookupService != null) {
            allNewsList = this.newsLookupService.getAllNews();//pobieramy wszystkie newsy
            calculatePages();
        }
        else {
            logger.info("Lookup service is NULL. Injection failed.");
            System.out.println("Lookup service is NULL. Injection failed.");
        }
    }*/

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getVolumeLong() {
        Connection con = DataConnect.getConnection();
            if(con==null)
                log.warning("NewsManager: getVolumeLong: con is null!");
            else {
                log.fine("NewsManager: getVolumeLong: con is NOT null");
                try {
                    Statement stmt = con.createStatement();
                    String query = "SELECT long_name FROM bible WHERE short_name='" + volume + "'";
                    //System.out.println("NewsManager: getVolumeLong: query = >" + query + "<");
                    log.fine("NewsManager: getVolumeLong: query = >" + query + "<");
                    ResultSet rs = stmt.executeQuery(query);
                    log.fine("NewsManager: getVolumeLong: There are " + rs.getMetaData().getColumnCount() + " columns in the table: " + rs.getMetaData().getTableName(1));

                    if(rs.next()) volumeLong = rs.getString(1);
                    log.fine("NewsManager: getVolumeLong: volumeLong = '" + volumeLong + "'");
                    //log.info("NewsManager: getVolumeLong: volumeLong = '" + volumeLong + "'");

                } catch (Exception e) {
                    log.warning(" NewsManager: getVolumeLong: rs error: " + e.getMessage());
                    log.warning(" NewsManager: getVolumeLong: rs error: " + e);
                } finally {
                    DataConnect.close(con);
                }
        }
        return volumeLong;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
        createTableIfNeeded();
        createList();
    }

    private void createTableIfNeeded() {
        if(user==null || volume==null) log.severe("NewsManager: createTableIfNeeded: user=" + user + ". volume=" + volume);
        else {
            log.info("NewsManager: createTable started. user=" + user + ". volume=" + volume);
            if (volume.matches("[0-9]?[A-Z]{1}[a-z]{0,2}") && LoginDAO.userMatches(user)) {
                Connection con = null;
                PreparedStatement ps = null;
                String tableName = null;
                try {
                    con = DataConnect.getConnection();
                    tableName = "zzzzur__" + user;
                    ps = con.prepareStatement("SELECT id FROM " + tableName + "  limit 1");
                    //ResultSet rs = ps.executeQuery();
                    ps.executeQuery();
                } catch (SQLException ex) {
                    log.info("NewsManager: createTable: SQLException ex.getMessage() -->" + ex.getMessage());
                    String query = "CREATE TABLE " + tableName + " (\n" +
                            "\tid int AUTO_INCREMENT,\n" +
                            "\tchapter int NOT NULL,\n" +
                            "\tvolume_chapter_id int NOT NULL,\n" +
                            "\tshort_name varchar(10) NOT NULL,\n" +
                            "\twas_read char(1) default 'N',\n" +
                            "\tt_plan timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                            "\tt_read timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                            "\tprimary key (id)\n" +
                            ");";
                    log.info("NewsManager: createTable: query = " + query);
                    try {
                        ps.execute(query);
                    } catch (SQLException exc) {
                        log.severe("NewsManager: createTable: Exception exc.getMessage() -->" + exc.getMessage());
                    }
                } catch (Exception e) {
                    log.severe("NewsManager: createTable: Exception e.getMessage() -->" + e.getMessage());
                } finally {
                    DataConnect.close(con);
                }
            } else {
                log.severe("NewsManager: createTable: Wrong user='" + user + "' or volume ='" + volume + "'");
            }
        }
    }


    public String getWasRead() {
        return wasRead;
    }

    //public void setReadYesId(int readYesId) {
    public void setWasRead(String inputStr) {
        String[] inputArr = inputStr.split(";");
        int length = inputArr.length;
        log.info("NewsManager: setWasRead: inputStr=" + inputStr + ". length=" + length);
        //#{news.id};volume;title;true
        try{
            if(length!=5) throw new IllegalArgumentException("NewsManager: setWasRead: wrong inputStr=" + inputStr + ". length=" + length);
            int wasReadId = Integer.parseInt(inputArr[0]);
            //String volume = inputArr[1];
            //if(volume.length()<1)throw new IllegalArgumentException("NewsManager: setWasRead: wrong inputStr=" + inputStr + ". volume=" + volume);
            if(wasReadId<=0) throw new IllegalArgumentException("NewsManager: setWasRead: wrong inputStr=" + inputStr + ". wasReadId=" + wasReadId);
            int chapter = Integer.parseInt(inputArr[1]);
            if(chapter<=0) throw new IllegalArgumentException("NewsManager: setWasRead: wrong inputStr=" + inputStr + ". chapter=" + chapter);
            String setRead = inputArr[2];
            if(setRead!="true" && setRead!="false")throw new IllegalArgumentException("NewsManager: setWasRead: wrong inputStr=" + inputStr + ". setRead=" + setRead);
            //String user = inputArr[4];

            Connection connection = null;
            PreparedStatement ps;
            try {
                connection = DataConnect.getConnection();
                ps = connection.prepareStatement("SELECT id FROM zzzzur__" + user + "  limit 1");
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    String query = "CREATE TABLE zzzur__" + user + "\n" +
                            "\tid int NOT NULL AUTO_INCREMENT,\n" +
                            "\tchapter int NOT NULL,\n" +
                            "\tvolume_chapter_id int NOT NULL,\n" +
                            "\tshort_name varchar(10) NOT NULL,\n" +
                            "\twas_read char(1) default 'N',\n" +
                            "\tt_plan timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                            "\tt_read timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                            "\tprimary key (id)\n" +
                            ");";
                    log.info("NewsManager: setWasRead: query = " + query);
                    ps.executeQuery(query);









                }

            } catch (SQLException ex) {
                log.severe("NewsManager: Login error -->" + 	ex.getMessage());
            } finally {
                DataConnect.close(connection);
            }

        } catch(Exception e) {
            log.severe(e.getMessage());
            //log.severe(e.printStackTrace());
            //System.out.println(e);
        }
    }

    private void calculatePages() {
        if (allNewsList != null) {
            recordsCount = allNewsList.size();
            allPages = (int) Math.ceil((double) ( (double)recordsCount / NewsManager.resultPerPage));
        }
        firstPage = 1;
        if (recordsCount == 0 || recordsCount <= NewsManager.resultPerPage) {
            page = 1;
            nextPage = 1;
            lastPage = 1;
            previousPage = 1;
        }
        else {
            lastPage = allPages;
            if (page < lastPage)
                nextPage = page + 1;
            else
                nextPage = page;
            if (page > 1)
                previousPage = page - 1;
            else
                previousPage = page;
        }
        generateTableCaption();
        //wsadzamy do newwList te co trzeba
        int start = NewsManager.resultPerPage * (page-1);
        int end = start + NewsManager.resultPerPage;
        if (end > recordsCount)
            end = (int) recordsCount;
        newsList = allNewsList.subList(start, end);
    }

    private void generateTableCaption() {
        String caption = "";
        String str = recordsCount + "";
        String lastDigitStr = str.substring(str.length()-1);
        int lastDigit = new Integer(lastDigitStr).intValue();

        if (recordsCount == 0) {
            caption = "Brak rekordów do wyświetlenia";
        }
        if (recordsCount == 1) {
            caption = "1 wynik (strona 1 z 1)";
        }
        else if (recordsCount > 1 && (lastDigit == 2 || lastDigit == 3 || lastDigit == 3 || lastDigit == 4) ) {
            caption = recordsCount + " wyniki (strona " + page + " z " + allPages + ")";
        }
        else {
            caption = recordsCount + " wyników (strona " + page + " z " + allPages + ")";
        }
        this.tableListCaption = caption;
    }

    public String getTableListCaption() {
        return tableListCaption;
    }

    public void setTableListCaption(String tableListCaption) {
        this.tableListCaption = tableListCaption;
    }

    public List<News> getNewsList() {
        if (newsList != null) {
            log.fine(user,"NewsManager: getNewsList(): News list size: " + newsList.size() + ". volume=" + volume);
            //System.out.println("NewsManager: getNewsList(): News list size: " + newsList.size());
        }
        else {
            log.warning(user,"NewsManager: getNewsList(): News list is NULL! volume=" + volume);
            //System.out.println("NewsManager: getNewsList(): News list is NULL!");
        }
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public Map<String, String> getBibleMap() {
        if (bibleMap != null) {
            log.fine(user,"NewsManager: getBibleMap(): bibleMap.size()=" + bibleMap.size());
            //System.out.println("NewsManager: getBibleMap(): bibleMap.size()=" + bibleMap.size());
        } else {
            log.warning(user,"NewsManager: getBibleMap(): bibleMap is NULL!");
            //System.out.println("NewsManager: getBibleMap(): bibleMap is NULL!");
        }
        return bibleMap;
    }

    public void setBibleMap(Map<String, String> bibleMap) {
        this.bibleMap = bibleMap;
    }

    public int getPage() {
        return page;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
    }

    public void setPage(int page) {
        this.page = page;
        calculatePages();
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    /*public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public long getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(long recordsCount) {
        this.recordsCount = recordsCount;
    }*/

}
