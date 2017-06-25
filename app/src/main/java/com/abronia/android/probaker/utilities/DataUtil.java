package com.abronia.android.probaker.utilities;

import android.content.ContentValues;
import android.database.Cursor;

import com.abronia.android.probaker.data.models.Ingredient;
import com.abronia.android.probaker.data.models.Recipe;
import com.abronia.android.probaker.data.models.Step;
import com.abronia.android.probaker.data.provider.ProBakerDbContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adaobifrank on 6/22/17.
 */

public class DataUtil {

    public static List<Recipe> CursorToRecipesConverter(Cursor cursor){

        List<Recipe> recipes = new ArrayList<Recipe>();

        while (cursor.moveToNext()) {

            int recipeId = cursor.getInt(cursor.getColumnIndex(ProBakerDbContract.RecipeEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(ProBakerDbContract.RecipeEntry.NAME));
            int servings = cursor.getInt(cursor.getColumnIndex(ProBakerDbContract.RecipeEntry.SERVINGS));
            String image = cursor.getString(cursor.getColumnIndex(ProBakerDbContract.RecipeEntry.IMAGE));

            Recipe recipe = new Recipe(recipeId, name, servings, image);
            recipes.add(recipe);
        }
        return recipes;
    }

    public static List<Step> CursorToStepsConverter(Cursor cursor){

        List<Step> steps = new ArrayList<Step>();

        while (cursor.moveToNext()) {

            int stepId = cursor.getInt(cursor.getColumnIndex(ProBakerDbContract.StepEntry._ID));
            String shortDescription = cursor.getString(cursor.getColumnIndex(ProBakerDbContract.StepEntry.SHORT_DESCRIPTION));
            String description = cursor.getString(cursor.getColumnIndex(ProBakerDbContract.StepEntry.DESCRIPTION));
            String videoURL = cursor.getString(cursor.getColumnIndex(ProBakerDbContract.StepEntry.VIDEO_URL));
            String thumbnailURL = cursor.getString(cursor.getColumnIndex(ProBakerDbContract.StepEntry.THUMBNAIL_URL));

            Step step = new Step(stepId, shortDescription, description, videoURL, thumbnailURL);
            steps.add(step);
        }
        return steps;
    }

    public static List<Ingredient> CursorToIngredientsConverter(Cursor cursor){

        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        while (cursor.moveToNext()) {

            int ingredientId = cursor.getInt(cursor.getColumnIndex(ProBakerDbContract.IngredientEntry._ID));
            float quantity = cursor.getFloat(cursor.getColumnIndex(ProBakerDbContract.IngredientEntry.QUANTITY));
            String measure = cursor.getString(cursor.getColumnIndex(ProBakerDbContract.IngredientEntry.MEASURE));
            String ingredientString = cursor.getString(cursor.getColumnIndex(ProBakerDbContract.IngredientEntry.INGREDIENT));

            Ingredient ingredient = new Ingredient(ingredientId, quantity, measure, ingredientString);
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    public static ContentValues[] getIngredientsContentValues(List<Ingredient> ingredients, long recipeId){

        ContentValues[] ingredientValues = new ContentValues[ingredients.size()];
        for (int i = 0; i < ingredients.size(); i++) {

            ContentValues contentValues = new ContentValues();

            contentValues.put(ProBakerDbContract.IngredientEntry.RECIPE_ID, recipeId);
            contentValues.put(ProBakerDbContract.IngredientEntry.QUANTITY, ingredients.get(i).getQuantity());
            contentValues.put(ProBakerDbContract.IngredientEntry.MEASURE, ingredients.get(i).getMeasure());
            contentValues.put(ProBakerDbContract.IngredientEntry.INGREDIENT, ingredients.get(i).getIngredient());

            ingredientValues[i] = contentValues;
        }
        return ingredientValues;
    }

    public static ContentValues[] getStepsContentValues(List<Step> steps, long recipeId){

        ContentValues[] stepValues = new ContentValues[steps.size()];
        for (int i = 0; i < steps.size(); i++) {

            ContentValues contentValues = new ContentValues();

            contentValues.put(ProBakerDbContract.StepEntry.RECIPE_ID, recipeId);
            contentValues.put(ProBakerDbContract.StepEntry.DESCRIPTION, steps.get(i).getDescription());
            contentValues.put(ProBakerDbContract.StepEntry.SHORT_DESCRIPTION, steps.get(i).getShortDescription());
            contentValues.put(ProBakerDbContract.StepEntry.THUMBNAIL_URL, steps.get(i).getThumbnailURL());
            contentValues.put(ProBakerDbContract.StepEntry.VIDEO_URL, steps.get(i).getVideoURL());

            stepValues[i] = contentValues;
        }
        return stepValues;
    }

}
