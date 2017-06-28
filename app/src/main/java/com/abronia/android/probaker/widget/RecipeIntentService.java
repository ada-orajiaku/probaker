package com.abronia.android.probaker.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.abronia.android.probaker.R;
import com.abronia.android.probaker.data.provider.ProBakerDbContract;

/**
 * Created by adaobifrank on 6/28/17.
 */

public class RecipeIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public static final String ACTION_UPDATE_RECIPE =
            "com.abronia.android.probaker.action.view_recipe";
    public static final String SERVICE_NAME = "RecipeIntentService";

    public RecipeIntentService() {
        super(SERVICE_NAME);
    }

    public static void startActionUpdateRecipe(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_UPDATE_RECIPE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE.equals(action)) {
                handleActionViewRecipe();
            }
        }
    }

    private void handleActionViewRecipe() {
        String recipeName = "";
        int recipeId = 0;

        Uri uri = ProBakerDbContract.RecipeEntry.CONTENT_URI.buildUpon()
                .appendPath(ProBakerDbContract.PATH_ONE_RANDOM_RECIPE)
                .build();
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);

        if (cursor.moveToNext()) {
            recipeId = cursor.getInt(cursor.getColumnIndex(ProBakerDbContract.RecipeEntry.RECIPE_ID));
            recipeName = cursor.getString(cursor.getColumnIndex(ProBakerDbContract.RecipeEntry.NAME));
        }

        int imageResource = R.drawable.cupcake;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        RecipeWidgetProvider.updateRecipeWidget(this, appWidgetManager, appWidgetIds, imageResource, recipeName,recipeId);
    }
}
