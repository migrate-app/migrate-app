package com.dankideacentral.dic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dankideacentral.dic.TweetListFragment.OnListFragmentInteractionListener;
import com.dankideacentral.dic.model.TweetNode;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TweetNode} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<TweetNode> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<TweetNode> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public boolean insert (TweetNode item) {
        boolean result = mValues.add(item);
        this.notifyDataSetChanged();
        return result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getStatus().getUser().getName());
        holder.mContentView.setText(mValues.get(position).getStatus().getText());
        holder.mImageView.setImageBitmap(mValues.get(position).getIcon());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public TweetNode mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mIdView = (TextView) view.findViewById(R.id.tweet_header).findViewById(R.id.tweet_handle);
            mContentView = (TextView) view.findViewById(R.id.tweet_view_content);
            mImageView = (ImageView) view.findViewById(R.id.tweet_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}
