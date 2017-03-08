package com.seroal.rssreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import com.seroal.rssreader.model.QueryBuilder;
import com.seroal.rssreader.model.RssAdapter;
import com.seroal.rssreader.model.RssFeed;
import com.seroal.rssreader.model.FeedItem;
import com.seroal.rssreader.presenter.SearchField;
import com.seroal.rssreader.utils.StorageManager;
import com.seroal.rssreader.view.RecyclerViewAdapter;
import com.seroal.rssreader.view.FeedViewHolder;

public class MainActivity extends AppCompatActivity {


    RecyclerView rvFeed;
    SwipeRefreshLayout swLayout;

    Retrofit retrofit;
    OkHttpClient okClient;
    RssAdapter rssAdapter;
    SearchField sortId= SearchField.NONE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        ab.setHomeAsUpIndicator(R.drawable.ic_share_grey);
        ab.setDisplayHomeAsUpEnabled(false);

        swLayout = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        rvFeed = (RecyclerView) findViewById(R.id.rvFeed);

        setupFeed();
        initializeConnections();
        requestUpdate();


        swipeInitialize();
    }
    private void initializeConnections() {

        okClient = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.flickr.com")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(okClient)
                .build();

        rssAdapter = retrofit.create(RssAdapter.class);

    }

    private void requestUpdate(String... params){


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
                            if (sortId==SearchField.TITLE)
                                return lhs.getTitle().compareTo(rhs.getTitle());
                            if (sortId==SearchField.PUBLISHER)
                                return lhs.getAuthor().getName().compareTo(rhs.getAuthor().getName());
                            if (sortId==SearchField.PUB_DATE)
                                return lhs.getPubDate().compareTo(rhs.getPubDate());
                            if (sortId==SearchField.UP_DATE)
                                return lhs.getUpdateDate().compareTo(rhs.getUpdateDate());
                            return 0;
                        }
                    });
                    RecyclerViewAdapter feedAdapter = new RecyclerViewAdapter(lsFeeds);

                    feedAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, FeedViewHolder feedViewHolder) {

                            if (feedViewHolder != null)
                                if (feedViewHolder.image.getDrawable() != null) {
                                    BitmapDrawable drawable = (BitmapDrawable) feedViewHolder.image.getDrawable();
                                    Bitmap bitmap = drawable.getBitmap();
                                    StorageManager.storeImage(bitmap);
                                    Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_LONG).show();
                                }
                        }
                    });
                    rvFeed.setAdapter(feedAdapter);
                }
                else{
                    Toast.makeText(getApplicationContext(),"An Error occured!" + response.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RssFeed> call, Throwable t) {

                Log.d("CallBack", " Throwable is " +t);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        final MenuItem sortTitleItem = menu.findItem(R.id.sort_title_item);
        final MenuItem sortPubItem = menu.findItem(R.id.sort_publisher_item);
        final MenuItem sortPubDateItem = menu.findItem(R.id.sort_published_item);
        final MenuItem sortUpdateItem = menu.findItem(R.id.sort_update_item);


        sortTitleItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                sortId = SearchField.TITLE;
                menuItem.setChecked(true);
                Log.d("menu", "SortID:"+sortId+"/Item"+menuItem.getItemId());
                return true;
            }
        });

        sortPubItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                sortId = SearchField.PUBLISHER;
                menuItem.setChecked(true);
                Log.d("menu", "SortID:"+sortId+"/Item"+menuItem.getItemId());
                return true;
            }
        });

        sortPubDateItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                sortId = SearchField.PUB_DATE;
                menuItem.setChecked(true);
                Log.d("menu", "SortID:"+sortId+"/Item"+menuItem.getItemId());
                return true;
            }
        });

        sortUpdateItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                sortId = SearchField.UP_DATE;
                menuItem.setChecked(true);
                Log.d("menu", "SortID:"+sortId+"/Item"+menuItem.getItemId());
                return true;
            }
        });


        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                requestUpdate("","",query,"");
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });




        return super.onCreateOptionsMenu(menu);
    }


    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }




    private void setupFeed() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };


        rvFeed.setLayoutManager(linearLayoutManager);

    }


    private void swipeInitialize(){

        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestUpdate();
            }
        });
    }

    }
