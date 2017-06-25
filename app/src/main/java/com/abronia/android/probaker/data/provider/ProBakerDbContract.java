package com.abronia.android.probaker.data.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by adaobifrank on 6/23/17.
 */

public class ProBakerDbContract {

    public static final String CONTENT_AUTHORITY = "com.abronia.android.probaker";

    public static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECIPE = "recipe";
    public static final String PATH_STEP = "step";
    public static final String PATH_INGREDIENT = "ingredient";

    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECIPE)
                .build();

        //RECIPE TABLE
        public static final String TABLE_NAME = "recipe";

        public static final String RECIPE_ID = "id";
        public static final String NAME = "name";
        public static final String SERVINGS = "servings";
        public static final String IMAGE = "image";

    }

    public static final class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGREDIENT)
                .build();

        public static final String TABLE_NAME = "ingredient";

        public static final String RECIPE_FOREIGN_KEY = "fk_recipe";
        public static final String RECIPE_ID = "recipe_id";
        public static final String QUANTITY = "quantity";
        public static final String MEASURE = "measure";
        public static final String INGREDIENT = "ingredient";

    }

    public static final class StepEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STEP)
                .build();

        public static final String TABLE_NAME = "step";

        public static final String RECIPE_ID = "recipe_id";
        public static final String RECIPE_FOREIGN_KEY = "fk_recipe";
        public static final String SHORT_DESCRIPTION = "short_description";
        public static final String DESCRIPTION = "description";
        public static final String VIDEO_URL = "video_url";
        public static final String THUMBNAIL_URL = "thumbnail_url";
    }
}
