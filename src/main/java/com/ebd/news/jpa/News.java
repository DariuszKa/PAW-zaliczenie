package com.ebd.news.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@Entity
@Table(name = "news")
@XmlRootElement
@XmlType( namespace = "http://paw.agh.edu.pl/types/", name = "News",
        propOrder = { "id", "chapter", "content", "createdAtTimestamp" })
public class News {
    protected String volume;
    protected int id;
    protected int chapter;
    protected String content;
    protected String wasRead;
    protected Date createdAt;
    protected Date updatedAt;

    public News() {
    }

    public News(String volume, int id, int title, String content, String wasRead) {
        //super();
        this.volume = volume;
        this.id = id;
        this.chapter = title;
        this.content = content;
        createdAt = new Date();
        updatedAt = new Date();
        this.wasRead = wasRead;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable=false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Column(name = "title", nullable = false)
    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    @Column(name = "content", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "was_read", nullable = false)
    public String getWasRead() {
        return wasRead;
    }

    public void setWasRead(String wasRead) {
        this.wasRead = wasRead;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, length = 29)
    @XmlTransient
    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false, length = 29)
    @XmlTransient
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Transient
    @XmlElement(name = "createdAt")
    protected long getCreatedAtTimestamp() {
        return createdAt.getTime();
    }

    @Transient
    protected void setCreatedAtTimestamp(long createdAtTimestamp) {
        createdAt.setTime(createdAtTimestamp);
    }

    @Transient
    @XmlTransient
    protected long getUpdatedAtTimestamp() {
        return updatedAt.getTime();
    }

    @Transient
    protected void setUpdatedAtTimestamp(long updatedAtTimestamp) {
        updatedAt.setTime(updatedAtTimestamp);
    }

    public void setUpdatedAt(java.util.Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @XmlTransient
    @Transient
    public String getTitleAbbr() {
        return String.valueOf(chapter);
    }

    @XmlTransient
    @Transient
    public String getContentAbbr() {
        int maxLength = 150;
        if (content != null)
            if (content.length() > maxLength)
                return content.substring(0, maxLength) + "...";
            else
                return content;
        return null;
    }

    @XmlTransient
    @Transient
    public String getCreatedAtAbbr() {
        String ret = "" + createdAt;
        return ret.substring(0, 16);
    }

    @XmlTransient
    @Transient
    public String getUpdatedAtAbbr() {
        String ret = "" + updatedAt;
        return ret.substring(0, 16);
    }

    @Override
    public String toString() {
        return "News [id=" + id + ", title=" + chapter + ", content=" + content + ", createdAt="
                + createdAt + ", updatedAt=" + updatedAt + "]";
    }

}
