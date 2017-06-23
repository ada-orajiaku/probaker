package com.abronia.android.probaker.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.ExecOnCreate;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by adaobifrank on 6/17/17.
 */

@Database(version = RecipeDatabase.VERSION,
        packageName = "com.abronia.android.probaker")
public final class RecipeDatabase {

    private RecipeDatabase() {
    }

    public static final int VERSION = 1;

    public static class Tables {

        @Table(RecipeColumns.class)
        //@IfNotExists
        public static final String RECIPES = "recipes";

        @Table(IngredientColumns.class)
       // @IfNotExists
        public static final String INGREDIENTS = "ingredients";
        @Table(IngredientColumns.class)
        // @IfNotExists
        public static final String STEPS = "steps";
    }
    //@Table(RecipeColumns.class) public static final String RECIPES = "recipes";

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }

    @ExecOnCreate
    public static final String EXEC_ON_CREATE = "SELECT * FROM " + Tables.RECIPES;
}
