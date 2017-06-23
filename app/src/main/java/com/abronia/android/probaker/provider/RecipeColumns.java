package com.abronia.android.probaker.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;

/**
 * Created by adaobifrank on 6/17/17.
 */

//@Constraints(
//        unique = @UniqueConstraint(
//                name = "UNQ_TAG_FOR_NOTE",
//                columns = {RecipeColumns.NAME,RecipeColumns.SERVINGS,RecipeColumns.IMAGE},
//                onConflict = REPLACE)
//)
public interface RecipeColumns {

    @DataType(INTEGER) @PrimaryKey @AutoIncrement String ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String NAME = "name";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String SERVINGS = "servings";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String IMAGE = "image";

    String INGREDIENTS = "ingredients";

    String STEPS = "steps";
}
