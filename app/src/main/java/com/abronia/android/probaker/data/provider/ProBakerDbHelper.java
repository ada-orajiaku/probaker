package com.abronia.android.probaker.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adaobifrank on 6/24/17.
 */

public class ProBakerDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "probaker.db";

    public static final int DATABASE_VERSION = 6;

    public ProBakerDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_RECIPE_TABLE =
                "CREATE TABLE "+ ProBakerDbContract.RecipeEntry.TABLE_NAME + "("+
                        ProBakerDbContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ProBakerDbContract.RecipeEntry.RECIPE_ID + " INTEGER NOT NULL, "+
                        ProBakerDbContract.RecipeEntry.NAME + " TEXT NOT NULL, "+
                        ProBakerDbContract.RecipeEntry.SERVINGS + " TEXT NOT NULL, "+
                        ProBakerDbContract.RecipeEntry.IMAGE + " TEXT NOT NULL ); ";

        final String SQL_CREATE_INGREDIENT_TABLE =
                "CREATE TABLE "+ ProBakerDbContract.IngredientEntry.TABLE_NAME + "("+
                        ProBakerDbContract.IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ProBakerDbContract.IngredientEntry.RECIPE_ID + " INTEGER NOT NULL, "+
                        ProBakerDbContract.IngredientEntry.QUANTITY + " REAL NOT NULL, "+
                        ProBakerDbContract.IngredientEntry.MEASURE + " TEXT NOT NULL, "+
                        ProBakerDbContract.IngredientEntry.INGREDIENT + " TEXT NOT NULL, "+
                        "FOREIGN KEY("+ProBakerDbContract.IngredientEntry.RECIPE_ID+") REFERENCES "+ ProBakerDbContract.RecipeEntry.TABLE_NAME+"(" + ProBakerDbContract.RecipeEntry.RECIPE_ID +"));";

        final String SQL_CREATE_STEP_TABLE =
                "CREATE TABLE "+ ProBakerDbContract.StepEntry.TABLE_NAME + "("+
                        ProBakerDbContract.StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ProBakerDbContract.StepEntry.RECIPE_ID + " INTEGER NOT NULL, "+
                        ProBakerDbContract.StepEntry.DESCRIPTION + " TEXT NOT NULL, "+
                        ProBakerDbContract.StepEntry.SHORT_DESCRIPTION + " TEXT NOT NULL, "+
                        ProBakerDbContract.StepEntry.VIDEO_URL + " TEXT NOT NULL, "+
                        ProBakerDbContract.StepEntry.THUMBNAIL_URL + " TEXT NOT NULL,"+
                        "FOREIGN KEY("+ProBakerDbContract.StepEntry.RECIPE_ID+") REFERENCES "+ ProBakerDbContract.RecipeEntry.TABLE_NAME+"(" + ProBakerDbContract.RecipeEntry.RECIPE_ID +"));";


        db.execSQL(SQL_CREATE_RECIPE_TABLE);
        db.execSQL(SQL_CREATE_INGREDIENT_TABLE);
        db.execSQL(SQL_CREATE_STEP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ ProBakerDbContract.RecipeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ ProBakerDbContract.StepEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ ProBakerDbContract.IngredientEntry.TABLE_NAME);
        onCreate(db);
    }
}
