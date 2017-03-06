package com.seroal.rssreader.model;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


/**
 * Created by rouhalavi on 05/03/2017.
 */

@Root(name = "feed", strict = false)
public class RssFeed implements Serializable {

    @ElementList(inline = true, name="entry")
    private List<FeedItem> feedItems;

    public List<FeedItem> getFeedItems() {
        return feedItems;
    }

    public RssFeed() {
    }

    public RssFeed(List<FeedItem> feedItems) {
        this.feedItems = feedItems;
    }
}