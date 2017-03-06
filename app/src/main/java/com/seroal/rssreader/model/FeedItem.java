package com.seroal.rssreader.model;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Attribute;

@Root(name = "entry", strict = false)
public class FeedItem implements Serializable {

    @Element(name = "published")
    private String pubDate;

    @Element(name = "author")
    private Author author;

    @Element(name = "title")
    private String title;

    @ElementList(entry = "link", inline = true)
    private List<FeedLink> link;

    @Element(name = "updated")
    private String updateDate;

    @ElementList(entry = "category", inline = true)
    private List<FeedTag> tags;


    public FeedItem() {
    }

    public FeedItem(String title, String updateDate, String pubDate) {
        this.updateDate = updateDate;

        this.title = title;
        this.pubDate = pubDate;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getTitle() {
        return title;
    }

    public List<FeedLink> getLink() {
        return link;
    }

    public List<FeedTag> getTags() { return tags; }

    public String getUpdateDate() {
        return updateDate;
    }

    public Author getAuthor() {
        return author;
    }

    @Root
    public static class Author{
        @Element(name = "name")
        private String name;

        @Element(name = "uri")
        private String uri;

        @Element(name = "nsid")
        private String nsid;

        @Element(name="buddyicon")
        private String icon;

        public String getName() {
            return name;
        }

        public String getIcon() {
            return icon;
        }
    }

    @Root
    public static class FeedLink {
        @Attribute
        private String rel;

        @Attribute
        private String type;

        @Attribute
        private String href;

        public String getRel() {
            return rel;
        }

        public String getType() {
            return type;
        }

        public String getHref() {
            return href;
        }
    }

    @Root
    public static class FeedTag{
        @Attribute
        private String term;

        @Attribute
        private String scheme;

        public String getTerm(){
            return term;
        }

        public String getScheme() {
            return scheme;
        }

        public String toString(){
            return "#"+ getTerm();
        }
    }

}
