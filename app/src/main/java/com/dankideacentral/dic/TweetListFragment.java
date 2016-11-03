package com.dankideacentral.dic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dankideacentral.dic.model.TweetNode;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;

import twitter4j.GeoLocation;

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
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TweetListFragment() {}

    public TweetListFragment(ArrayList tweetNodes) {
        this.tweetNodes = tweetNodes;
        location = (tweetNodes.size() > 0)? this.tweetNodes.get(0).getPosition(): new LatLng(0,0);
    }

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
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tweet_list, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);



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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.tweet_list_menu, menu);
    }


    public boolean onOptionItemIsSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_directions:
                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW,
                        getDirectionsUri());
                startActivity(mapIntent);
                return true;

            case R.id.back_to_map:
                Log.v("TweetListFragment --", "back");
                getActivity().getFragmentManager().popBackStack();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private Uri getDirectionsUri(){
        double destLat  = location.latitude;
        double destLong = location.longitude;
        String directionsFromCurrentLocation =  "http://maps.google.com/maps?daddr= %d,%d";
        //String directionsFromDifferentAddress = "http://maps.google.com/maps?saddr=%d,%d&daddr= %d,%d";
        String uri = String.format(directionsFromCurrentLocation, destLat, destLong);
        return Uri.parse(uri);
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
