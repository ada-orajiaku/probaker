package com.abronia.android.probaker.utilities;

import android.content.ContentValues;
import android.database.Cursor;

import com.abronia.android.probaker.models.Ingredient;
import com.abronia.android.probaker.models.Recipe;
import com.abronia.android.probaker.models.Step;
import com.abronia.android.probaker.provider.IngredientColumns;
import com.abronia.android.probaker.provider.RecipeColumns;
import com.abronia.android.probaker.provider.StepColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adaobifrank on 6/22/17.
 */

public class DataUtil {

    public static List<Recipe> CursorToRecipesConverter(Cursor cursor){

        List<Recipe> recipes = new ArrayList<Recipe>();

        while (cursor.moveToNext()) {

            int recipeId = cursor.getInt(cursor.getColumnIndex(RecipeColumns.ID));
            String name = cursor.getString(cursor.getColumnIndex(RecipeColumns.NAME));
           // List<Ingredient> ingredients = CursorToIngredientsConverter(cursor. (RecipeColumns.INGREDIENTS))
            int servings = cursor.getInt(cursor.getColumnIndex(RecipeColumns.SERVINGS));
            String image = cursor.getString(cursor.getColumnIndex(RecipeColumns.IMAGE));

            Recipe recipe = new Recipe(recipeId, name, servings, image);
            recipes.add(recipe);
        }
        return recipes;
    }

    public List<Step> CursorToStepsConverter(Cursor cursor){

        List<Step> steps = new ArrayList<Step>();

        while (cursor.moveToNext()) {

            int stepId = cursor.getInt(cursor.getColumnIndex(StepColumns.ID));
            String shortDescription = cursor.getString(cursor.getColumnIndex(StepColumns.SHORT_DESCRIPTION));
            String description = cursor.getString(cursor.getColumnIndex(StepColumns.DESCRIPTION));
            String videoURL = cursor.getString(cursor.getColumnIndex(StepColumns.VIDEO_URL));
            String thumbnailURL = cursor.getString(cursor.getColumnIndex(StepColumns.THUMBNAIL_URL));

            Step step = new Step(stepId, shortDescription, description, videoURL, thumbnailURL);
            steps.add(step);
        }
        return steps;
    }

    public List<Ingredient> CursorToIngredientsConverter(Cursor cursor){

        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        while (cursor.moveToNext()) {

            int ingredientId = cursor.getInt(cursor.getColumnIndex(IngredientColumns.ID));
            float quantity = cursor.getFloat(cursor.getColumnIndex(IngredientColumns.QUANTITY));
            String measure = cursor.getString(cursor.getColumnIndex(IngredientColumns.MEASURE));
            String ingredientString = cursor.getString(cursor.getColumnIndex(IngredientColumns.INGREDIENT));

            Ingredient ingredient = new Ingredient(ingredientId, quantity, measure, ingredientString);
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    public static ContentValues[] getIngredientsContentValues(List<Ingredient> ingredients, long recipeId){

        ContentValues[] ingredientValues = new ContentValues[ingredients.size()];
        for (int i = 0; i < ingredients.size(); i++) {

            ContentValues contentValues = new ContentValues();

            contentValues.put(IngredientColumns.RECIPE_ID, recipeId);
            contentValues.put(IngredientColumns.QUANTITY, ingredients.get(i).getQuantity());
            contentValues.put(IngredientColumns.MEASURE, ingredients.get(i).getMeasure());
            contentValues.put(IngredientColumns.INGREDIENT, ingredients.get(i).getIngredient());

            ingredientValues[i] = contentValues;
        }
        return ingredientValues;
    }

    public static ContentValues[] getStepsContentValues(List<Step> steps, long recipeId){

        ContentValues[] stepValues = new ContentValues[steps.size()];
        for (int i = 0; i < steps.size(); i++) {

            ContentValues contentValues = new ContentValues();

            contentValues.put(StepColumns.RECIPE_ID, recipeId);
            contentValues.put(StepColumns.DESCRIPTION, steps.get(i).getDescription());
            contentValues.put(StepColumns.SHORT_DESCRIPTION, steps.get(i).getShortDescription());
            contentValues.put(StepColumns.THUMBNAIL_URL, steps.get(i).getThumbnailURL());
            contentValues.put(StepColumns.VIDEO_URL, steps.get(i).getVideoURL());

            stepValues[i] = contentValues;
        }
        return stepValues;
    }

}
