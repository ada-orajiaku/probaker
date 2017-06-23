package com.abronia.android.probaker.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;

/**
 * Created by adaobifrank on 6/17/17.
 */

public interface IngredientColumns {
    @DataType(INTEGER) @PrimaryKey
    @AutoIncrement
    String ID = "_id";

    @DataType(INTEGER) @References(table = RecipeDatabase.Tables.RECIPES, column = RecipeColumns.ID)
    String RECIPE_ID = "recipeId";

    @DataType(DataType.Type.REAL)
    @NotNull
    String QUANTITY = "quantity";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String MEASURE = "measure";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String INGREDIENT = "ingredient";
}
