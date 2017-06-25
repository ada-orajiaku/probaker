package com.abronia.android.probaker.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by adaobifrank on 6/24/17.
 */

public class ProBakerDbProvider extends ContentProvider {

    private static final String TAG = ProBakerDbProvider.class.getSimpleName();

    public static final int CODE_RECIPE = 123;
    public static final int CODE_RECIPE_BY_ID = 125;

    public static final int CODE_STEP = 126;
    public static final int CODE_STEPS_USING_RECIPE_ID = 127;
    public static final int CODE_STEP_BY_ID = 128;

    public static final int CODE_INGREDIENT = 129;
    public static final int CODE_INGREDIENTS_USING_RECIPE_ID = 130;
    public static final int CODE_INGREDIENT_BY_ID = 131;

    private static final UriMatcher sUriMatcher = builderUriMatcher();
    private ProBakerDbHelper dbHelper;

    public static UriMatcher builderUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ProBakerDbContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, ProBakerDbContract.PATH_RECIPE, CODE_RECIPE);
        uriMatcher.addURI(authority, ProBakerDbContract.PATH_RECIPE + "/#", CODE_RECIPE_BY_ID);

        uriMatcher.addURI(authority, ProBakerDbContract.PATH_INGREDIENT, CODE_INGREDIENT);
        uriMatcher.addURI(authority, ProBakerDbContract.PATH_INGREDIENT + "/#", CODE_INGREDIENT_BY_ID);
        uriMatcher.addURI(authority, ProBakerDbContract.PATH_INGREDIENT + "/#", CODE_INGREDIENTS_USING_RECIPE_ID);

        uriMatcher.addURI(authority, ProBakerDbContract.PATH_STEP, CODE_STEP);
        uriMatcher.addURI(authority, ProBakerDbContract.PATH_STEP + "/#",CODE_STEP_BY_ID);
        uriMatcher.addURI(authority, ProBakerDbContract.PATH_STEP + "/#",CODE_STEPS_USING_RECIPE_ID);

        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        dbHelper = new ProBakerDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case CODE_RECIPE:
                cursor = dbHelper.getReadableDatabase().query(ProBakerDbContract.RecipeEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CODE_INGREDIENTS_USING_RECIPE_ID:
                String recipeId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{recipeId};

                cursor = dbHelper.getReadableDatabase().query(ProBakerDbContract.IngredientEntry.TABLE_NAME,projection,
                        ProBakerDbContract.IngredientEntry.RECIPE_ID +" = ?",selectionArguments,null,null,sortOrder);
                break;
            case CODE_STEPS_USING_RECIPE_ID:
                String stepForeignKey = uri.getLastPathSegment();
                String[] stepsSelectionArguments = new String[]{stepForeignKey};

                cursor = dbHelper.getReadableDatabase().query(ProBakerDbContract.StepEntry.TABLE_NAME,projection,
                        ProBakerDbContract.StepEntry.RECIPE_ID +" = ?",stepsSelectionArguments,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : "+ uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri = null;

        switch (sUriMatcher.match(uri)){
            case CODE_RECIPE:
                long recipeId = dbHelper.getWritableDatabase().insert(ProBakerDbContract.RecipeEntry.TABLE_NAME,null,values);
                if(recipeId > 0){
                    returnUri = ContentUris.withAppendedId(ProBakerDbContract.RecipeEntry.CONTENT_URI,recipeId);
                }else {
                    Log.e(TAG,"Failed to insert row into " + uri);
                    //throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case CODE_STEP:
                long stepId = dbHelper.getWritableDatabase().insert(ProBakerDbContract.StepEntry.TABLE_NAME,null,values);
                if(stepId > 0){
                    returnUri = ContentUris.withAppendedId(ProBakerDbContract.StepEntry.CONTENT_URI,stepId);
                }else {
                    Log.e(TAG,"Failed to insert row into " + uri);
                    //throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case CODE_INGREDIENT:
                long ingredientId = dbHelper.getWritableDatabase().insert(ProBakerDbContract.IngredientEntry.TABLE_NAME,null,values);
                if(ingredientId > 0){
                    returnUri = ContentUris.withAppendedId(ProBakerDbContract.IngredientEntry.CONTENT_URI,ingredientId);
                }else {
                    Log.e(TAG,"Failed to insert row into " + uri);
                    //throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                Log.e(TAG,"Unknown uri : "+uri);
                //throw new UnsupportedOperationException("Unknown uri : "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case CODE_RECIPE:
                db.beginTransaction();
                int recipesInserted = 0;
                try {
                    for (ContentValues value : values){
                        long _id = db.insert(ProBakerDbContract.RecipeEntry.TABLE_NAME,null,value);
                        if(_id != -1){
                            recipesInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }

                if(recipesInserted > 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return recipesInserted;

            case CODE_STEP:
                db.beginTransaction();
                int stepsInserted = 0;
                try {
                    for (ContentValues value : values){
                        long _id = db.insert(ProBakerDbContract.StepEntry.TABLE_NAME,null,value);
                        if(_id != -1){
                            stepsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }

                if(stepsInserted > 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return stepsInserted;

            case CODE_INGREDIENT:
                db.beginTransaction();
                int ingredientsInserted = 0;
                try {
                    for (ContentValues value : values){
                        long _id = db.insert(ProBakerDbContract.IngredientEntry.TABLE_NAME,null,value);
                        if(_id != -1){
                            ingredientsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }

                if(ingredientsInserted > 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return ingredientsInserted;
            default:
                return super.bulkInsert(uri,values);
        }
    }

}
