package com.abronia.android.probaker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abronia.android.probaker.R;
import com.abronia.android.probaker.models.Recipe;
import com.abronia.android.probaker.provider.RecipeColumns;
import com.squareup.picasso.Picasso;

import java.util.Date;
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

//    public void setRecipes(List<Recipe> recipes) {
//        this.recipes = recipes;
//        notifyDataSetChanged();
//        notifyItemRangeChanged(0, recipes.size());
//    }
    public void swapCursor(Cursor newCursor) {
        mData = newCursor;
        notifyDataSetChanged();
    }

//    private List<Recipe> recipes;
   // private OnItemClickListener onItemClickListener;

    public RecipeAdapter(Context context,Cursor data) {
        this.context = context;
        this.mData = data;
        // this.recipes = recipes;
    }

    public OnRecipeSelectedListener onItemSelectedListener;
    public interface OnRecipeSelectedListener {
        void onRecipeSelected(long recipeId);
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

        holder.recipeTextView.setText(mData.getColumnIndex(RecipeColumns.NAME));
        //Picasso.with(context).load(context.getString(R.string.image_base_url_500) +movieList.get(position).getPosterPath()).into(holder.movieImage);
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedListener.onRecipeSelected(mData.getColumnIndex(RecipeColumns.ID));
            }
        };
        holder.recipeCardView.setOnClickListener(listener);

    }


//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        View v = LayoutInflater.from(context).inflate(R.layout.recipe_list_item, parent, false);
//        v.setTag(new RecipeViewHolder(v));
//        return v;
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        ViewHolder = (ViewHolder) view.getTag();
//        holder.recipeTextView.setText(recipes.get(position).getName());
//        // Picasso.with(context).load(recipes.get(position).getImage()).into(holder.);
//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onItemClickListener.onItemClick(recipes.get(position));
//            }
//        };
//        holder.recipeCardView.setOnClickListener(listener);
//    }

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

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }
}