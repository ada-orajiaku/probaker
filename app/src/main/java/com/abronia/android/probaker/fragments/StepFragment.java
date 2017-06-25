package com.abronia.android.probaker.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abronia.android.probaker.R;
import com.abronia.android.probaker.RecipeDetailActivity;
import com.abronia.android.probaker.StepDetailActivity;
import com.abronia.android.probaker.adapters.MyStepRecyclerViewAdapter;
import com.abronia.android.probaker.data.models.Step;
import com.abronia.android.probaker.data.provider.ProBakerDbContract;
import com.abronia.android.probaker.utilities.DataUtil;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class StepFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipeId";
    private static final String ARG_IS_TWO_PANE = "isTwoPane";
    private int recipeId;
    private Boolean mTwoPane;
    public OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepFragment() {
    }

    public static StepFragment newInstance(int recipeId, Boolean mTwoPane) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        args.putBoolean(ARG_IS_TWO_PANE, mTwoPane);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            recipeId = getArguments().getInt(ARG_RECIPE_ID);
            mTwoPane = getArguments().getBoolean(ARG_IS_TWO_PANE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.steps_recycler_view);

        Uri uri = ProBakerDbContract.StepEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(recipeId)).build();
        Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);

        List<Step> stepList = DataUtil.CursorToStepsConverter(cursor);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyStepRecyclerViewAdapter(stepList, mListener));

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (this.getActivity() instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(this.getActivity().toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Step item);
    }
}
