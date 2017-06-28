package com.abronia.android.probaker.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.abronia.android.probaker.R;
import com.abronia.android.probaker.adapters.MyIngredientRecyclerViewAdapter;
import com.abronia.android.probaker.adapters.MyStepRecyclerViewAdapter;
import com.abronia.android.probaker.data.models.Ingredient;
import com.abronia.android.probaker.data.models.Step;
import com.abronia.android.probaker.data.provider.ProBakerDbContract;
import com.abronia.android.probaker.utilities.DataUtil;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class IngredientFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipeId";
    public static final String ARG_TWO_PANE = "two_pane";
    private int recipeId;
    private Boolean mTwoPane;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientFragment() {
    }

    public static IngredientFragment newInstance(int recipeId, Boolean mTwoPane) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        args.putBoolean(ARG_TWO_PANE, mTwoPane);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            recipeId = getArguments().getInt(ARG_RECIPE_ID);
            mTwoPane = getArguments().getBoolean(ARG_TWO_PANE);
        }
        if(!mTwoPane)
            setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        if(!mTwoPane){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("INGREDIENTS");
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            RecyclerView recyclerView = (RecyclerView) view;

            Uri uri = ProBakerDbContract.IngredientEntry.CONTENT_URI.buildUpon()
                    .appendPath(ProBakerDbContract.PATH_INGREDIENT_BY_RECIPE)
                    .appendPath(String.valueOf(recipeId))
                    .build();
            Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);

            List<Ingredient> ingredients = DataUtil.CursorToIngredientsConverter(cursor);

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyIngredientRecyclerViewAdapter(ingredients, mListener));
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
               getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (this.getActivity() instanceof OnListFragmentInteractionListener) {
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Ingredient ingredient);
    }
}
