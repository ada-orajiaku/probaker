package com.abronia.android.probaker.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.MapColumns;
import net.simonvt.schematic.annotation.NotifyBulkInsert;
import net.simonvt.schematic.annotation.NotifyDelete;
import net.simonvt.schematic.annotation.NotifyInsert;
import net.simonvt.schematic.annotation.NotifyUpdate;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adaobifrank on 6/17/17.
 */

@ContentProvider(authority = RecipeProvider.AUTHORITY,
        database = RecipeDatabase.class,
        packageName = "com.abronia.android.probaker")

public class RecipeProvider {

    private RecipeProvider() {
    }

    public static final String AUTHORITY = "com.abronia.android.probaker";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String RECIPES = "recipes";
        String INGREDIENTS = "ingredients";
        String FROM_RECIPE = "fromRecipe";
        String STEPS = "steps";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = RecipeDatabase.Tables.RECIPES)
    public static class Recipes {

        @MapColumns
        public static Map<String, String> mapColumns() {
            Map<String, String> map = new HashMap<>();

            map.put(RecipeColumns.INGREDIENTS, INGREDIENT_COUNT);
            map.put(RecipeColumns.STEPS, STEP_COUNT);

            return map;
        }

        @ContentUri(
                path = Path.RECIPES,
                type = "vnd.android.cursor.dir/recipe",
                defaultSort = RecipeColumns.NAME + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.RECIPES);

        @InexactContentUri(
                path = Path.RECIPES + "/#",
                name = "RECIPE_ID",
                type = "vnd.android.cursor.item/recipe",
                whereColumn = RecipeColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.RECIPES, String.valueOf(id));
        }

        static final String INGREDIENT_COUNT = "(SELECT COUNT(*) FROM "
                + RecipeDatabase.Tables.INGREDIENTS
                + " WHERE "
                + RecipeDatabase.Tables.INGREDIENTS
                + "."
                + IngredientColumns.RECIPE_ID
                + "="
                + RecipeDatabase.Tables.RECIPES
                + "."
                + RecipeColumns.ID
                + ")";

        static final String STEP_COUNT = "(SELECT COUNT(*) FROM "
                + RecipeDatabase.Tables.STEPS
                + " WHERE "
                + RecipeDatabase.Tables.STEPS
                + "."
                + StepColumns.RECIPE_ID
                + "="
                + RecipeDatabase.Tables.STEPS
                + "."
                + RecipeColumns.ID
                + ")";

        @NotifyBulkInsert(paths = Path.RECIPES)
        public static Uri[] onBulkInsert(Context context, Uri uri, ContentValues[] values) {
            return new Uri[] {
                    uri,
            };
        }
    }

    @TableEndpoint(table = RecipeDatabase.Tables.INGREDIENTS)
    public static class Ingredients {

        @ContentUri(
                path = Path.INGREDIENTS,
                type = "vnd.android.cursor.dir/ingredient")
        public static final Uri CONTENT_URI = buildUri(Path.INGREDIENTS);

        @InexactContentUri(
                name = "INGREDIENT_ID",
                path = Path.INGREDIENTS + "/#",
                type = "vnd.android.cursor.item/ingredient",
                whereColumn = IngredientColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.INGREDIENTS, String.valueOf(id));
        }

        @InexactContentUri(
                name = "INGREDIENTS_FROM_RECIPES",
                path = Path.INGREDIENTS + "/" + Path.FROM_RECIPE + "/#",
                type = "vnd.android.cursor.dir/recipe",
                whereColumn = IngredientColumns.RECIPE_ID,
                pathSegment = 2)
        public static Uri fromRecipe(long listId) {
            return buildUri(Path.INGREDIENTS, Path.FROM_RECIPE, String.valueOf(listId));
        }

        @NotifyInsert(paths = Path.INGREDIENTS) public static Uri[] onInsert(ContentValues values) {
            final long recipeId = values.getAsLong(IngredientColumns.RECIPE_ID);
            return new Uri[] {
                    Recipes.withId(recipeId), fromRecipe(recipeId),
            };
        }

        @NotifyBulkInsert(paths = Path.INGREDIENTS)
        public static Uri[] onBulkInsert(Context context, Uri uri, ContentValues[] values, long[] ids) {
            return new Uri[] {
                    uri,
            };
        }

        @NotifyUpdate(paths = Path.INGREDIENTS + "/#") public static Uri[] onUpdate(Context context,
                                                                              Uri uri, String where, String[] whereArgs) {
            final long noteId = Long.valueOf(uri.getPathSegments().get(1));
            Cursor c = context.getContentResolver().query(uri, new String[] {
                    IngredientColumns.RECIPE_ID,
            }, null, null, null);
            c.moveToFirst();
            final long recipeId = c.getLong(c.getColumnIndex(IngredientColumns.RECIPE_ID));
            c.close();

            return new Uri[] {
                    withId(noteId), fromRecipe(recipeId), Recipes.withId(recipeId),
            };
        }

        @NotifyDelete(paths = Path.INGREDIENTS + "/#") public static Uri[] onDelete(Context context,
                                                                              Uri uri) {
            final long noteId = Long.valueOf(uri.getPathSegments().get(1));
            Cursor c = context.getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();
            final long recipeId = c.getLong(c.getColumnIndex(IngredientColumns.RECIPE_ID));
            c.close();

            return new Uri[] {
                    withId(noteId), fromRecipe(recipeId), Recipes.withId(recipeId),
            };
        }
    }

    @TableEndpoint(table = RecipeDatabase.Tables.STEPS) public static class Steps {

        @ContentUri(
                path = Path.STEPS,
                type = "vnd.android.cursor.dir/step")
        public static final Uri CONTENT_URI = buildUri(Path.STEPS);

        @InexactContentUri(
                name = "STEP_ID",
                path = Path.STEPS + "/#",
                type = "vnd.android.cursor.item/step",
                whereColumn = StepColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.STEPS, String.valueOf(id));
        }

        @InexactContentUri(
                name = "STEPS_FROM_RECIPES",
                path = Path.STEPS + "/" + Path.FROM_RECIPE + "/#",
                type = "vnd.android.cursor.dir/step",
                whereColumn = StepColumns.RECIPE_ID,
                pathSegment = 2)

        public static Uri fromRecipe(long listId) {
            return buildUri(Path.STEPS, Path.FROM_RECIPE, String.valueOf(listId));
        }

        @NotifyInsert(paths = Path.STEPS) public static Uri[] onInsert(ContentValues values) {
            final long recipeId = values.getAsLong(StepColumns.RECIPE_ID);
            return new Uri[] {
                    Recipes.withId(recipeId), fromRecipe(recipeId),
            };
        }

        @NotifyBulkInsert(paths = Path.STEPS)
        public static Uri[] onBulkInsert(Context context, Uri uri, ContentValues[] values, long[] ids) {
            return new Uri[] {
                    uri,
            };
        }

        @NotifyUpdate(paths = Path.STEPS + "/#") public static Uri[] onUpdate(Context context,
                                                                                    Uri uri, String where, String[] whereArgs) {
            final long noteId = Long.valueOf(uri.getPathSegments().get(1));
            Cursor c = context.getContentResolver().query(uri, new String[] {
                    StepColumns.RECIPE_ID,
            }, null, null, null);
            c.moveToFirst();
            final long recipeId = c.getLong(c.getColumnIndex(StepColumns.RECIPE_ID));
            c.close();

            return new Uri[] {
                    withId(noteId), fromRecipe(recipeId), Recipes.withId(recipeId),
            };
        }

        @NotifyDelete(paths = Path.STEPS + "/#") public static Uri[] onDelete(Context context,
                                                                                    Uri uri) {
            final long noteId = Long.valueOf(uri.getPathSegments().get(1));
            Cursor c = context.getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();
            final long recipeId = c.getLong(c.getColumnIndex(StepColumns.RECIPE_ID));
            c.close();

            return new Uri[] {
                    withId(noteId), fromRecipe(recipeId), Recipes.withId(recipeId),
            };
        }
    }
}

