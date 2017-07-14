package com.abronia.android.probaker.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.abronia.android.probaker.R;
import com.abronia.android.probaker.RecipeDetailActivity;
import com.abronia.android.probaker.data.models.Ingredient;
import com.abronia.android.probaker.data.provider.ProBakerDbContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adaobifrank on 7/14/17.
 */

public class IngredientListRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ListViewRemoteViewsFactory(this.getApplicationContext(),intent);
    }
}

class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String ARG_RECIPE_ID = "recipe_id";
    public static final String ARG_RECIPE_NAME = "recipe_name";
    public static final String ARG_RECIPE_INGREDIENTS = "recipe_ingredients";

    Context mContext;
    Cursor mCursor;
    ArrayList<Ingredient> ingredients;

    int recipeId;
    String recipeName;

    public ListViewRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        recipeId = intent.getIntExtra(ARG_RECIPE_ID,0);
        recipeName = intent.getStringExtra(ARG_RECIPE_NAME);
      //  ingredients = intent.getParcelableArrayListExtra(ARG_RECIPE_INGREDIENTS);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        Uri uri = ProBakerDbContract.IngredientEntry.CONTENT_URI.buildUpon()
                .appendPath(ProBakerDbContract.PATH_INGREDIENT_BY_RECIPE)
                .appendPath(String.valueOf(recipeId))
                .build();

        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(uri,null,null,null,null);

    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
//        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;

        mCursor.moveToPosition(position);

        float quantity = mCursor.getFloat(mCursor.getColumnIndex(ProBakerDbContract.IngredientEntry.QUANTITY));
        String measure = mCursor.getString(mCursor.getColumnIndex(ProBakerDbContract.IngredientEntry.MEASURE));
        String ingredientString = mCursor.getString(mCursor.getColumnIndex(ProBakerDbContract.IngredientEntry.INGREDIENT));

      //  Ingredient currentIngredient = ingredients.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_ingredient_list_item);

//        views.setTextViewText(R.id.appwidget_ingredient_name, currentIngredient.getIngredient());
//        views.setTextViewText(R.id.appwidget_ingredient_measure, currentIngredient.getMeasure());
//        views.setTextViewText(R.id.appwidget_ingredient_quantity, Float.toString(currentIngredient.getQuantity()));

        views.setTextViewText(R.id.appwidget_ingredient_name, ingredientString);
        views.setTextViewText(R.id.appwidget_ingredient_measure, measure);
        views.setTextViewText(R.id.appwidget_ingredient_quantity, Float.toString(quantity));

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        Bundle extras = new Bundle();
        extras.putLong(RecipeDetailActivity.ARGS_RECIPE_ID, recipeId);
        extras.putString(RecipeDetailActivity.ARGS_RECIPE_NAME, recipeName);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        views.setOnClickFillInIntent(R.id.recipe_widget_ingredient_list_item, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}