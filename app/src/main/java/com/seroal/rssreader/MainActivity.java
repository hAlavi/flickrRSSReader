package com.seroal.rssreader;

import java.util.ArrayList;
import java.util.List;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import com.seroal.rssreader.model.RssAdapter;
import com.seroal.rssreader.model.RssFeed;
import com.seroal.rssreader.model.FeedItem;
import com.seroal.rssreader.view.RecyclerViewAdapter;
import com.seroal.rssreader.view.ViewModel;

public class MainActivity extends AppCompatActivity {


    RecyclerView rvFeed;
    SwipeRefreshLayout swLayout;

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
        buildRetrofit();


    }


    private void buildRetrofit(){

        OkHttpClient okClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.flickr.com")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(okClient)
                .build();

        RssAdapter rssAdapter = retrofit.create(RssAdapter.class);

        Call<RssFeed> call = rssAdapter.getItems();
        call.enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {


                Toast.makeText(getApplicationContext(),"CallBack"+ " response is " + response.body().getFeedItems().get(2).getAuthor().getName() ,Toast.LENGTH_LONG).show();
                swLayout.setRefreshing(false);
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

        final MenuItem searchItem = menu.findItem(R.id.action_search);


        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


      /*  // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemsVisibility(menu, searchItem, false);
            }
        });
        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setItemsVisibility(menu, searchItem, true);
                return false;
            }
        });*/

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here


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

        List<ViewModel> items = new ArrayList<>();

        items.add(new ViewModel("x","y","z","r"));
        items.add(new ViewModel("a","b","c","d"));


        RecyclerViewAdapter feedAdapter = new RecyclerViewAdapter(items);

        feedAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, ViewModel viewModel){
                Toast.makeText(getApplicationContext(),viewModel.getTitle(),Toast.LENGTH_LONG).show();
            }
        });
        rvFeed.setAdapter(feedAdapter);




        rvFeed.setAdapter(feedAdapter);




    }


    private void SwipeInitialize(){

        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buildRetrofit();
            }
        });
    }

}
