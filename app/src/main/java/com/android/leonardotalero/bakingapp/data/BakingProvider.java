package com.android.leonardotalero.bakingapp.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by leonardotalero on 7/7/17.
 */

public class BakingProvider  extends ContentProvider {
    public static final int CODE_BAKING = 110;
    public static final int CODE_BAKING_WITH_ID = 121;

    /*
     * The URI Matcher used by this content provider. The leading "s" in this variable name
     * signifies that this UriMatcher is a static member variable of WeatherProvider and is a
     * common convention in Android programming.
     */
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private BakingDBhelper mOpenHelper;

    /**
     * Creates the UriMatcher that will match each URI to the CODE_BAKING and
     * CODE_BAKING_WITH_DATE constants defined above.
     * <p>
     * It's possible you might be thinking, "Why create a UriMatcher when you can use regular
     * expressions instead? After all, we really just need to match some patterns, and we can
     * use regular expressions to do that right?" Because you're not crazy, that's why.
     * <p>
     * UriMatcher does all the hard work for you. You just have to tell it which code to match
     * with which URI, and it does the rest automagically. Remember, the best programmers try
     * to never reinvent the wheel. If there is a solution for a problem that exists and has
     * been tested and proven, you should almost always use it unless there is a compelling
     * reason not to.
     *
     * @return A UriMatcher that correctly matches the constants for CODE_BAKING and CODE_BAKING_WITH_DATE
     */
    public static UriMatcher buildUriMatcher() {

        /*
         * All paths added to the UriMatcher have a corresponding code to return when a match is
         * found. The code passed into the constructor of UriMatcher here represents the code to
         * return for the root URI. It's common to use NO_MATCH as the code for this case.
         */
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BakingContract.CONTENT_AUTHORITY;

        /*
         * For each type of URI you want to add, create a corresponding code. Preferably, these are
         * constant fields in your class so that you can use them throughout the class and you no
         * they aren't going to change. In Sunshine, we use CODE_BAKING or CODE_BAKING_WITH_DATE.
         */

        /* This URI is content://com.example.android.sunshine/weather/ */
        matcher.addURI(authority, BakingContract.PATH_Recipe, CODE_BAKING);

        /*
         * This URI would look something like content://com.example.android.sunshine/weather/1472214172
         * The "/#" signifies to the UriMatcher that if PATH_WEATHER is followed by ANY number,
         * that it should return the CODE_BAKING_WITH_DATE code
         */
        matcher.addURI(authority, BakingContract.PATH_Recipe + "/#", CODE_BAKING_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {

        mOpenHelper = new BakingDBhelper(getContext());
        return true;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
       final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsInserted = 0;
        switch (sUriMatcher.match(uri)) {

            case CODE_BAKING:
                db.beginTransaction();

                /* try {

                   for (ContentValues value : values) {
                        if (value == null){
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;

                        try {
                            _id = db.insert(BakingContract.BakingEntry.TABLE_NAME, null, value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (_id != -1) {
                            rowsInserted++;
                        }

                        Log.i("record_inserted",String.valueOf(_id));
                        //Uri flag = insert(uri, value);
                        //rowsInserted++;
                    }


                    db.setTransactionSuccessful();
                }
                catch(Exception e){
                    e.printStackTrace();

                }
                finally {
                    db.endTransaction();
                }
                //db.endTransaction();

                */
                try {
                    for (ContentValues cv : values) {
                            long newID = db.insertOrThrow(BakingContract.BakingEntry.TABLE_NAME, null, cv);
                            if (newID <= 0) {
                                throw new SQLException("Failed to insert row into " + uri);
                            }
                        }

                    } finally {
                        db.setTransactionSuccessful();
                        db.endTransaction();
                    }
                //if (rowsInserted > 0) {

                    getContext().getContentResolver().notifyChange(uri, null);
                    rowsInserted = values.length;
                    //getContext().getContentResolver().notifyChange(uri, null);
                //}



            /*default:
                return super.bulkInsert(uri, values);
                */
        }
        return rowsInserted;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        /*
         * Here's the switch statement that, given a URI, will determine what kind of request is
         * being made and query the database accordingly.
         */
        switch (sUriMatcher.match(uri)) {

           

            case CODE_BAKING: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        BakingContract.BakingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            // Individual flavor based on Id selected
            case CODE_BAKING_WITH_ID:{
                cursor = mOpenHelper.getReadableDatabase().query(
                        BakingContract.BakingEntry.TABLE_NAME,
                        projection,
                        BakingContract.BakingEntry.COLUMN_RECIPE_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_BAKING:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        BakingContract.BakingEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                // reset _ID
                /*mOpenHelper.getWritableDatabase().execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        BakingContract.BakingEntry.TABLE_NAME + "'");
                */
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    /**
     * In Sunshine, we aren't going to do anything with this method. However, we are required to
     * override it as WeatherProvider extends ContentProvider and getType is an abstract method in
     * ContentProvider. Normally, this method handles requests for the MIME type of the data at the
     * given URI. For example, if your app provided images at a particular URI, then you would
     * return an image URI from this method.
     *
     * @param uri the URI to query.
     * @return nothing in Sunshine, but normally a MIME type string, or null if there is no type.
     */
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in Sunshine.");
    }


    @Override
    public Uri insert(Uri uri, ContentValues values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri=null;
        switch (sUriMatcher.match(uri)) {
            case CODE_BAKING: {
                long _id = 0;
                try {
                    _id = db.insert(BakingContract.BakingEntry.TABLE_NAME, null, values);
                    if (_id > 0) {
                        returnUri = BakingContract.BakingEntry.buildUri(_id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into: " + uri);
                    }
                    //db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //db.endTransaction();
                }
                // insert unless it is already contained in the database

                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(sUriMatcher.match(uri)){
            case CODE_BAKING:{
                numUpdated = db.update(BakingContract.BakingEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case CODE_BAKING_WITH_ID: {
                numUpdated = db.update(BakingContract.BakingEntry.TABLE_NAME,
                        contentValues,
                        BakingContract.BakingEntry.COLUMN_RECIPE_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

    /**
     * You do not need to call this method. This is a method specifically to assist the testing
     * framework in running smoothly. You can read more at:
     * http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
     */
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
