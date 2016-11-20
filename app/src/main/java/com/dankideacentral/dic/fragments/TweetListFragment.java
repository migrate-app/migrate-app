package com.dankideacentral.dic.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dankideacentral.dic.view.DividerItemDecoration;
import com.dankideacentral.dic.R;
import com.dankideacentral.dic.activities.TweetFeedActivity;
import com.dankideacentral.dic.adapters.MyItemRecyclerViewAdapter;
import com.dankideacentral.dic.model.TweetNode;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TweetListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyItemRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<TweetNode> tweetNodes = new <TweetNode> ArrayList();
    private LatLng location;
    private Toolbar toolbar = null;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TweetListFragment() {}

//    public TweetListFragment(ArrayList tweetNodes) {
//        this.tweetNodes = tweetNodes;
//        location = (tweetNodes.size() > 0) ? this.tweetNodes.get(0).getPosition(): new LatLng(45.383082, -75.698312);
//    }

    public boolean insert (TweetNode item) {
        return recyclerViewAdapter.insert(item);
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TweetListFragment newInstance(int columnCount) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tweetNodes = getArguments().getParcelableArrayList("TWEETS");

            if (tweetNodes.get(0) instanceof TweetNode) {
                TweetNode firstNode = (TweetNode) tweetNodes.get(0);
                location = firstNode.getPosition();
            }


            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tweet_list, container, false);
        final Activity mActivity = getActivity();
        toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        toolbar.hasExpandedActionView();

        // done nav separately so it appears on the far left side
        // TODO: Case where this sets the nav icon to the arrow, when tweetlistfragment already dismissed
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.getMenu().clear();
                if (mActivity != null)
                    mActivity.onBackPressed();
            }
        });
        // TODO: This will inflate everytime a clusteritem or cluster is clicked
        toolbar.inflateMenu(R.menu.tweet_list_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_directions:
                        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW,
                                getDirectionsUri());
                        startActivity(mapIntent);
                        return true;

                    case R.id.share:
                        // implement the share info
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "" + getDirectionsUri().toString();
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_message));
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    default:
                        return false;

                }
            }
        });


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getResources().getDrawable(R.drawable.divider));
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.addItemDecoration(dividerItemDecoration);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(recyclerViewAdapter = new MyItemRecyclerViewAdapter(tweetNodes, mListener));
        }
        return view;
    }

    private Uri getDirectionsUri(){
        if (location != null) {
            double destLat  = location.latitude;
            double destLong = location.longitude;
            String directionsFromCurrentLocation =  "http://maps.google.com/maps?daddr= %f,%f";
            //String directionsFromDifferentAddress = "http://maps.google.com/maps?saddr=%f,%f&daddr= %f,%f";
            String uri = String.format(directionsFromCurrentLocation, destLat, destLong);
            return Uri.parse(uri);
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((TweetFeedActivity)getActivity()).setToolbar();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(TweetNode item);
    }
}
