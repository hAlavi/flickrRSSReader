package com.seroal.rssreader.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.seroal.rssreader.R;
import com.seroal.rssreader.model.FeedItem;
import com.seroal.rssreader.utils.FeedManageable;
import com.seroal.rssreader.utils.StorageManageable;
import com.seroal.rssreader.utils.StorageManager;

/**
 * Created by rouhalavi on 04/03/2017.
 */

public class FeedViewHolder extends  RecyclerView.ViewHolder{
    private String title;
    private String publisher;
    public TextView text;
    public ImageView image;
    public TextView tvPubDate;
    public TextView tvUserName;
    public ImageView imUser;
    private ImageButton saveBtn;
    private ImageButton openBtn;
    private ImageButton emailBtn;
    private FeedItem feedItem;

    private StorageManageable smDevice;


    public FeedViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.ivFeedCenter);
        imUser = (ImageView) itemView.findViewById(R.id.ivUserProfile);
        tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
        tvPubDate = (TextView) itemView.findViewById(R.id.tvPubDate);
        text = (TextView) itemView.findViewById(R.id.tvFeedDetails);
        saveBtn = (ImageButton) itemView.findViewById(R.id.btnSave);
        emailBtn = (ImageButton) itemView.findViewById(R.id.btnEmail);
        openBtn = (ImageButton) itemView.findViewById(R.id.btnOpenBrowser);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedManageable!=null)
                    feedManageable.storeFeed(FeedViewHolder.this);
            }
        });

        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedManageable!=null)
                    feedManageable.openFeed(FeedViewHolder.this);
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedManageable!=null)
                    feedManageable.mailFeed(FeedViewHolder.this);
            }
        });

        itemView.setTag(this);
    }
    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public TextView getText() {
        return text;
    }

    public ImageView getImage() {
        return image;
    }

    public String getTitlePub() {
        return title + " by " + publisher;
    }

    public void setFeedItem(FeedItem fi){
        feedItem = fi;
    }

    private FeedManageable feedManageable;

    public void inject(FeedManageable fm){
        feedManageable = fm;
    }
}
