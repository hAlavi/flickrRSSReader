package com.seroal.rssreader.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rouhalavi on 05/03/2017.
 */

public interface RssAdapter{
    @GET("/services/feeds/photos_public.gne")
    Call<RssFeed> getItems();
}
