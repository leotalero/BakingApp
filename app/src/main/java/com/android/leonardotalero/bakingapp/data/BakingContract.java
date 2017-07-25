package com.android.leonardotalero.bakingapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by leonardotalero on 7/7/17.
 */

public class BakingContract {


    public static final String CONTENT_AUTHORITY = "com.android.leonardotalero.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_Recipe = "recipe";


    /* Inner class that defines the table contents of the Recipe table */
    public static final class BakingEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Recipe table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_Recipe)
                .build();

        /* Used internally as the name of our Recipe table. */
        public static final String TABLE_NAME = "recipe";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_NAME = "name";
        public static final String COLUMN_INGREDIENTS= "ingredients";
        public static final String COLUMN_STEPS = "steps";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_ADITIONAL= "aditional";


        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // for building URIs on insertion
        public static Uri buildUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildRecipeUriWithDate(long date) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }


    }
}
