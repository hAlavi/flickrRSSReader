package com.seroal.rssreader;

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

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import com.seroal.rssreader.model.FeedItem;
import com.seroal.rssreader.model.RssAdapter;
import com.seroal.rssreader.presenter.FlickrReedPresenter;
import com.seroal.rssreader.presenter.SearchField;
import com.seroal.rssreader.utils.StorageManager;
import com.seroal.rssreader.view.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    RecyclerView rvFeed;
    SwipeRefreshLayout swLayout;
    RecyclerViewAdapter rvAdapter;
    StorageManager storageManager = StorageManager.getInstance();

    Retrofit retrofit;
    OkHttpClient okClient;
    RssAdapter rssAdapter;

    FlickrReedPresenter frPresenter = FlickrReedPresenter.getInstance();

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
        List<FeedItem> feedItems = new ArrayList<>();
        rvAdapter = new RecyclerViewAdapter(getApplicationContext(),feedItems);
        rvFeed.setAdapter(rvAdapter);

        setupFeedLayout();
        initializeConnections();
        injectDependencies();

        runApp();
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

    private void injectDependencies(){
        rvAdapter.injectDevice(storageManager);

        frPresenter.inject(getApplicationContext());
        frPresenter.inject(rssAdapter);
        frPresenter.inject(rvAdapter);
        frPresenter.inject(swLayout);
    }


    private void runApp(){
        frPresenter.requestUpdate();
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
                frPresenter.setSearchField(SearchField.TITLE);
                menuItem.setChecked(true);
                return true;
            }
        });

        sortPubItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                frPresenter.setSearchField(SearchField.PUBLISHER);
                menuItem.setChecked(true);
                return true;
            }
        });

        sortPubDateItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                frPresenter.setSearchField(SearchField.PUB_DATE);
                menuItem.setChecked(true);
                return true;
            }
        });

        sortUpdateItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                frPresenter.setSearchField(SearchField.UP_DATE);
                menuItem.setChecked(true);
                return true;
            }
        });


        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                frPresenter.requestUpdate("","",query,"");
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


    private void setupFeedLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };


        rvFeed.setLayoutManager(linearLayoutManager);

    }



}
