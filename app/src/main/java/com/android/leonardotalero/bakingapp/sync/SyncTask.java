package com.android.leonardotalero.bakingapp.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateUtils;

import com.android.leonardotalero.bakingapp.Utilities.JsonUtils;
import com.android.leonardotalero.bakingapp.Utilities.NetworkUtils;
import com.android.leonardotalero.bakingapp.Utilities.NotificationUtils;
import com.android.leonardotalero.bakingapp.data.BakingContract;
import com.android.leonardotalero.bakingapp.data.BakingPreferences;

import java.net.URL;

/**
 * Created by leonardotalero on 7/8/17.
 */

public class SyncTask {


        /**
         * Performs the network request for updated weather, parses the JSON from that request, and
         * inserts the new weather information into our ContentProvider. Will notify the user that new
         * weather has been loaded if the user hasn't been notified of the weather within the last day
         * AND they haven't disabled notifications in the preferences screen.
         *
         * @param context Used to access utility methods and the ContentResolver
         */
        synchronized public static void syncRecipes(Context context) {

            try {
            /*
             * The getUrl method will return the URL that we need to get the forecast JSON for the
             * weather. It will decide whether to create a URL based off of the latitude and
             * longitude or off of a simple location as a String.
             */
            String query="";
                URL weatherRequestUrl = NetworkUtils.buildUrl(query);

            /* Use the URL to retrieve the JSON */
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

            /* Parse the JSON into a list of weather values */
                ContentValues[] weatherValues = JsonUtils
                        .getContentValuesFromJson(context, jsonResponse);

            /*
             * In cases where our JSON contained an error code, getWeatherContentValuesFromJson
             * would have returned null. We need to check for those cases here to prevent any
             * NullPointerExceptions being thrown. We also have no reason to insert fresh data if
             * there isn't any to insert.
             */
                if (weatherValues != null && weatherValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                    ContentResolver contentResolver = context.getContentResolver();

                /* Delete old weather data because we don't need to keep multiple days' data */
                    contentResolver.delete(
                            BakingContract.BakingEntry.CONTENT_URI,
                            null,
                            null);

                /* Insert our new weather data into Sunshine's ContentProvider */
                    int data = contentResolver.bulkInsert(
                            BakingContract.BakingEntry.CONTENT_URI,
                            weatherValues);

                /*
                 * Finally, after we insert data into the ContentProvider, determine whether or not
                 * we should notify the user that the weather has been refreshed.
                 */

                    boolean notificationsEnabled = BakingPreferences.areNotificationsEnabled(context);

                /*
                 * If the last notification was shown was more than 1 day ago, we want to send
                 * another notification to the user that the weather has been updated. Remember,
                 * it's important that you shouldn't spam your users with notifications.
                 */

                    //long timeSinceLastNotification = SunshinePreferences
                    //        .getEllapsedTimeSinceLastNotification(context);

                    boolean oneDayPassedSinceLastNotification = false;

                    //if (timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS) {
                   //     oneDayPassedSinceLastNotification = true;
                   // }


                /*
                 * We only want to show the notification if the user wants them shown and we
                 * haven't shown a notification in the past day.
                 */

                    if (notificationsEnabled) {
                       // NotificationUtils.notifyUserOfNewData(context);
                    }



            /* If the code reaches this point, we have successfully performed our sync */

                }

            } catch (Exception e) {
            /* Server probably invalid */
                e.printStackTrace();
            }
        }

}
