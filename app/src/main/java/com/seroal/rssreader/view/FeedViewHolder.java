package com.seroal.rssreader.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seroal.rssreader.R;

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



    public FeedViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.ivFeedCenter);
        imUser = (ImageView) itemView.findViewById(R.id.ivUserProfile);
        tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
        tvPubDate = (TextView) itemView.findViewById(R.id.tvPubDate);
        text = (TextView) itemView.findViewById(R.id.tvFeedDetails);
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
}
