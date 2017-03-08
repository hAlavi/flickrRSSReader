package com.seroal.rssreader.view;

import java.util.List;

import com.seroal.rssreader.model.FeedItem;

/**
 * Created by rouhalavi on 08/03/2017.
 */

public interface ViewAdapter {
    void putData(List<FeedItem> feeds);
    void refresh();
}
