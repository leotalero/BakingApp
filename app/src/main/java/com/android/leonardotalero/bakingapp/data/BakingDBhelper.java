package com.android.leonardotalero.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.leonardotalero.bakingapp.data.BakingContract.BakingEntry;

/**
 * Created by leonardotalero on 7/7/17.
 */

public class BakingDBhelper extends SQLiteOpenHelper {

    /*
     * This is the name of our database. Database names should be descriptive and end with the
     * .db extension.
     */
    public static final String DATABASE_NAME = "baking.db";


    private static final int DATABASE_VERSION = 10;

    public BakingDBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our BAKING data.
         */
        final String SQL_CREATE_BAKING_TABLE =

                "CREATE TABLE " + BakingEntry.TABLE_NAME + " (" +

                /*
                 * BakingEntry did not explicitly declare a column called "_ID". However,
                 * BakingEntry implements the interface, "BaseColumns", which does have a field
                 * named "_ID". We use that here to designate our table's primary key.
                 */
                        BakingEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        BakingEntry.COLUMN_DATE       + "  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "                 +

                        BakingEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL,"                  +

                        BakingEntry.COLUMN_RECIPE_NAME   + " TEXT NOT NULL, "                    +
                        BakingEntry.COLUMN_INGREDIENTS   + " TEXT NOT NULL, "                    +

                        BakingEntry.COLUMN_STEPS   + " TEXT NOT NULL, "                    +
                        BakingEntry.COLUMN_SERVINGS   + " TEXT , "                    +

                        BakingEntry.COLUMN_IMAGE + " TEXT, "                    +
                        BakingEntry.COLUMN_ADITIONAL + " TEXT, "                    +


                /*
                 * To ensure this table can only contain one BAKING entry per date, we declare
                 * the date column to be unique. We also specify "ON CONFLICT REPLACE". This tells
                 * SQLite that if we have a BAKING entry for a certain date and we attempt to
                 * insert another BAKING entry with that date, we replace the old BAKING entry.
                 */
                        " UNIQUE (" + BakingEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_BAKING_TABLE);
    }

    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * you change the version number for your database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for your application found in your app/build.gradle file. If
     * you want to update the schema without wiping data, commenting out the current body of this
     * method should be your top priority before modifying this method.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}