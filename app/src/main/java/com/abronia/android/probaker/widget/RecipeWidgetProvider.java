package com.abronia.android.probaker.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.abronia.android.probaker.MainActivity;
import com.abronia.android.probaker.R;
import com.abronia.android.probaker.RecipeDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int imageResource, String recipeName, int recipeId) {

        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.ARGS_RECIPE_ID, recipeId);
        intent.putExtra(RecipeDetailActivity.ARGS_RECIPE_NAME, recipeName);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        views.setTextViewText(R.id.appwidget_recipe_name, recipeName);
        views.setImageViewResource(R.id.appwidget_cake_image, imageResource);
        views.setOnClickPendingIntent(R.id.appwidget_cake_image, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeIntentService.startActionUpdateRecipe(context);
    }

    public static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds, int imageResource, String recipeName, int recipeId) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, imageResource, recipeName, recipeId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

