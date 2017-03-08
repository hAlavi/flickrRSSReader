package com.seroal.rssreader.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.seroal.rssreader.model.FeedItem;
import com.seroal.rssreader.model.QueryBuilder;
import com.seroal.rssreader.model.RssAdapter;
import com.seroal.rssreader.model.RssFeed;
import com.seroal.rssreader.view.FeedViewHolder;
import com.seroal.rssreader.view.RecyclerViewAdapter;
import com.seroal.rssreader.view.ViewAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rouhalavi on 08/03/2017.
 */

public class FlickrReedPresenter {
    static private ViewAdapter viewAdapter;
    static private RssAdapter rssAdapter;
    static private SwipeRefreshLayout swLayout;
    static private Context context;
    static private SearchField searchField = SearchField.NONE;

    private static FlickrReedPresenter instance = new FlickrReedPresenter();
    //
    private FlickrReedPresenter(){

    }

    public static FlickrReedPresenter getInstance(){
        return instance;
    }

    public void inject(ViewAdapter  adapter){
        viewAdapter = adapter;
    }
    public void inject(RssAdapter adapter){
        rssAdapter = adapter;
    }
    public void inject(Context context){
        this.context = context;
    }
    public void inject(SwipeRefreshLayout swipeRefreshLayout){
        swLayout = swipeRefreshLayout;
        if (swLayout!=null){
            swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestUpdate();
                }
            });
        }
    }

    public void setSearchField(SearchField field){
        searchField = field;
        if (viewAdapter!=null)
            requestUpdate();
    }

    public void requestUpdate(String... params){


        swLayout.setRefreshing(true);
        Call<RssFeed> call;
        if (params.length!=4) {
            call = rssAdapter.getItems(QueryBuilder.buildQuery());
        }else{
            call = rssAdapter.getItems(QueryBuilder.buildQuery(params[0],params[1],params[2],params[3]));

        }
        call.enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {


                //Toast.makeText(getApplicationContext(),"CallBack"+ " response is " + response.body().getFeedItems().get(2).getAuthor().getName() ,Toast.LENGTH_LONG).show();
                swLayout.setRefreshing(false);


                if (response.body()!=null) {
                    List<FeedItem> lsFeeds = response.body().getFeedItems();
                    Collections.sort(lsFeeds, new Comparator<FeedItem>() {
                        @Override
                        public int compare(FeedItem lhs, FeedItem rhs) {
                            if (searchField==SearchField.TITLE)
                                return lhs.getTitle().compareTo(rhs.getTitle());
                            if (searchField==SearchField.PUBLISHER)
                                return lhs.getAuthor().getName().compareTo(rhs.getAuthor().getName());
                            if (searchField==SearchField.PUB_DATE)
                                return lhs.getPubDate().compareTo(rhs.getPubDate());
                            if (searchField==SearchField.UP_DATE)
                                return lhs.getUpdateDate().compareTo(rhs.getUpdateDate());
                            return 0;
                        }
                    });

                    viewAdapter.putData(lsFeeds);

                }
                else{
                    Toast.makeText(context,"An Error occured!" + response.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RssFeed> call, Throwable t) {

                Log.d("CallBack", " Throwable is " +t);
                swLayout.setRefreshing(false);
            }
        });


    }


}
