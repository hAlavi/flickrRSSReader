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

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {

    private List<FeedItem> items;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(List<FeedItem> items) {
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FeedItem item = items.get(position);
        holder.text.setText(item.getTitle());
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
        onItemClickListener.onItemClick(v, (ViewHolder) v.getTag());
    }



    public interface OnItemClickListener {

        void onItemClick(View view, ViewHolder viewHolder);

    }
}
