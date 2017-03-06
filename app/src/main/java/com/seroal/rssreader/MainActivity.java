package com.seroal.rssreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.seroal.rssreader.model.RssAdapter;
import com.seroal.rssreader.model.RssFeed;
import com.seroal.rssreader.model.FeedItem;
import com.seroal.rssreader.view.RecyclerViewAdapter;
import com.seroal.rssreader.view.FeedViewHolder;

public class MainActivity extends AppCompatActivity {


    RecyclerView rvFeed;
    SwipeRefreshLayout swLayout;

    Retrofit retrofit;
    OkHttpClient okClient;
    RssAdapter rssAdapter;


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


        SwipeInitialize();
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

    private void requestUpdate(){


        Call<RssFeed> call = rssAdapter.getItems();
        call.enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {


                Toast.makeText(getApplicationContext(),"CallBack"+ " response is " + response.body().getFeedItems().get(2).getAuthor().getName() ,Toast.LENGTH_LONG).show();
                swLayout.setRefreshing(false);



                RecyclerViewAdapter feedAdapter = new RecyclerViewAdapter(response.body().getFeedItems());

                feedAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener(){
                    @Override
                    public void onItemClick(View view, FeedViewHolder feedViewHolder){
                        BitmapDrawable drawable = (BitmapDrawable) feedViewHolder.image.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        storeImage(bitmap);
                        Toast.makeText(getApplicationContext(), "Saved!",Toast.LENGTH_LONG).show();                   }
                });
                rvFeed.setAdapter(feedAdapter);


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

        List<FeedItem> items = new ArrayList<>();


        RecyclerViewAdapter feedAdapter = new RecyclerViewAdapter(items);

        feedAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, FeedViewHolder feedViewHolder){
                BitmapDrawable drawable = (BitmapDrawable) feedViewHolder.image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                storeImage(bitmap);
                Toast.makeText(getApplicationContext(), "Saved!",Toast.LENGTH_LONG).show();
            }
        });
        rvFeed.setAdapter(feedAdapter);




        rvFeed.setAdapter(feedAdapter);




    }


    private void SwipeInitialize(){

        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestUpdate();
            }
        });
    }

    private  File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");


        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }


    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {

            return;
        }
        try {

            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }
}
