package com.abronia.android.probaker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abronia.android.probaker.R;
import com.abronia.android.probaker.data.models.Ingredient;
import com.abronia.android.probaker.fragments.IngredientFragment.OnListFragmentInteractionListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Ingredient} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyIngredientRecyclerViewAdapter extends RecyclerView.Adapter<MyIngredientRecyclerViewAdapter.ViewHolder> {

    private final List<Ingredient> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyIngredientRecyclerViewAdapter(List<Ingredient> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.ingredient.setText(mValues.get(position).getIngredient());
        holder.quantity.setText(mValues.get(position).getQuantity().toString());
        holder.measure.setText(mValues.get(position).getMeasure());

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

        @BindView(R.id.ingredient)
        TextView ingredient;
        @BindView(R.id.quantity)
        TextView quantity;
        @BindView(R.id.measure)
        TextView measure;

        public Ingredient mItem;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            mView = view;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + ingredient.getText() + "'";
        }
    }
}
