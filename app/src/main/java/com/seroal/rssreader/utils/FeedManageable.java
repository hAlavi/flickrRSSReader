package com.seroal.rssreader.utils;

import com.seroal.rssreader.view.FeedViewHolder;

/**
 * Created by rouhalavi on 08/03/2017.
 */

public interface FeedManageable {
    void openFeed(FeedViewHolder feedViewHolder);
    void storeFeed(FeedViewHolder feedViewHolder);
    void mailFeed(FeedViewHolder feedViewHolder);
}
