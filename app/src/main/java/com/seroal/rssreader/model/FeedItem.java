package com.seroal.rssreader.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

@Root(name = "entry", strict = false)
public class FeedItem implements Serializable {
    @Element(name = "published")
    private String pubDate;
    @Element(name = "title")
    private String title;
    @Element(name = "link", required = false)
    private String link;
    @Element(name = "updated")
    private String updateDate;




    public FeedItem() {
    }

    public FeedItem(String title, String updateDate, String pubDate, String link) {
        this.updateDate = updateDate;
        this.link = link;
        this.title = title;
        this.pubDate = pubDate;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getUpdateDate() {
        return updateDate;
    }


}