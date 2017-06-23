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

public interface StepColumns {
    @DataType(INTEGER) @PrimaryKey
    @AutoIncrement
    String ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String SHORT_DESCRIPTION = "shortDescription";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String DESCRIPTION = "description";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String VIDEO_URL = "videoURL";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String THUMBNAIL_URL = "thumbnailURL";

    @DataType(INTEGER) @References(table = RecipeDatabase.Tables.RECIPES, column = RecipeColumns.ID)
    String RECIPE_ID = "recipeId";
}
