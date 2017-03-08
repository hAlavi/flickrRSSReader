package com.seroal.rssreader.view;

/**
 * Created by rouhalavi on 04/03/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seroal.rssreader.R;

import com.seroal.rssreader.model.FeedItem;
import com.seroal.rssreader.utils.FeedManageable;
import com.seroal.rssreader.utils.StorageManageable;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<FeedViewHolder>
        implements View.OnClickListener, FeedManageable {

    private List<FeedItem> items;
    private OnItemClickListener onItemClickListener;
    private Context context;

    private StorageManageable smDevice;


    public RecyclerViewAdapter(Context context, List<FeedItem> items) {

        this.items = items;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        v.setOnClickListener(this);
        return new FeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        FeedItem item = items.get(position);
        holder.text.setText(item.getTitle()+" tags :"+item.getTags().toString());
        holder.tvPubDate.setText(item.getPubDate());
        holder.tvUserName.setText(item.getAuthor().getName());

        holder.imUser.setImageBitmap(null);
        Picasso.with(holder.imUser.getContext())
                .load(item.getAuthor().getIcon())
                .into(holder.imUser);

        holder.image.setImageBitmap(null);
        Picasso.with(holder.image.getContext())
                .load(item.getLink().get(item.getLink().size()-1).getHref())
                .into(holder.image);

        holder.inject(this);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(final View v) {
        onItemClickListener.onItemClick(v, (FeedViewHolder) v.getTag());
    }

    public void injectDevice(StorageManageable sm){
        smDevice = sm;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, FeedViewHolder feedViewHolder);

    }

    public void openFeed(FeedViewHolder feedViewHolder){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse());
        context.startActivity(browserIntent);
    }
    public void storeFeed(FeedViewHolder feedViewHolder){
        if (feedViewHolder != null)
            if (feedViewHolder.image.getDrawable() != null) {
                BitmapDrawable drawable = (BitmapDrawable) feedViewHolder.image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                smDevice.storeImage(bitmap);
                Toast.makeText(context, "Saved!!!", Toast.LENGTH_LONG).show();
            }

    }

    public void mailFeed(FeedViewHolder feedViewHolder){

    }

}
