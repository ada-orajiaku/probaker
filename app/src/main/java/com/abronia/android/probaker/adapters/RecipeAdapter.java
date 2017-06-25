package com.abronia.android.probaker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abronia.android.probaker.R;
import com.abronia.android.probaker.data.models.Recipe;
import com.abronia.android.probaker.data.provider.ProBakerDbContract;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by adaobifrank on 6/20/17.
 */

public class RecipeAdapter extends  RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context context;
    private Cursor mData;

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
        notifyItemRangeChanged(0,recipes.size());
    }

    private List<Recipe> recipes;
   // private OnItemClickListener onItemClickListener;


    public void swapCursor(Cursor newCursor) {
        mData = newCursor;
        notifyDataSetChanged();
    }

    public RecipeAdapter(Context context,Cursor data) {
        this.context = context;
        this.mData = data;
    }

    public OnRecipeSelectedListener onItemSelectedListener;
    public interface OnRecipeSelectedListener {
        void onRecipeSelected(int recipeId);
    }

    public OnRecipeSelectedListener getOnItemClickListener() {
        return onItemSelectedListener;
    }

    public void setOnItemClickListener(OnRecipeSelectedListener onItemClickListener) {
        this.onItemSelectedListener = onItemClickListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);

        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: ");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        mData.moveToPosition(position);

        if(mData.getCount() > 0){

            String recipeName = mData.getString(mData.getColumnIndex(ProBakerDbContract.RecipeEntry.NAME));
            String imageUrl = mData.getString(mData.getColumnIndex(ProBakerDbContract.RecipeEntry.IMAGE));
            final int recipeId = mData.getInt(mData.getColumnIndex(ProBakerDbContract.RecipeEntry.RECIPE_ID));

            holder.recipeTextView.setText(recipeName);

            if(imageUrl != null && !imageUrl.isEmpty()){
                Picasso.with(context).load(imageUrl).into(holder.recipeImage);
            }

            final View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String index = ProBakerDbContract.RecipeEntry.RECIPE_ID;
                    //int columnIndex = mData.getColumnIndex(ProBakerDbContract.RecipeEntry.RECIPE_ID);
                   // int recipeId = mData.getInt(mData.getColumnIndex(ProBakerDbContract.RecipeEntry.RECIPE_ID));
                    onItemSelectedListener.onRecipeSelected(recipeId);
                }
            };
            holder.recipeCardView.setOnClickListener(listener);
        }

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.getCount();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder{
        public Recipe recipe;

        public @BindView(R.id.recipe_card_view)
        CardView recipeCardView;
        public @BindView(R.id.recipe_name)
        TextView recipeTextView;
        public @BindView(R.id.recipe_image)
        ImageView recipeImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }
}