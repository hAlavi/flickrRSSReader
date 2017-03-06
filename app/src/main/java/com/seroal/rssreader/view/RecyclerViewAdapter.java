package com.seroal.rssreader.view;

/**
 * Created by rouhalavi on 04/03/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seroal.rssreader.R;

import com.seroal.rssreader.model.FeedItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<FeedViewHolder> implements View.OnClickListener {

    private List<FeedItem> items;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(List<FeedItem> items) {
        this.items = items;
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


        //holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(final View v) {
        onItemClickListener.onItemClick(v, (FeedViewHolder) v.getTag());
    }



    public interface OnItemClickListener {

        void onItemClick(View view, FeedViewHolder feedViewHolder);

    }
}
