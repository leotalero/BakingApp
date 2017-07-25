package com.android.leonardotalero.bakingapp.Utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.android.leonardotalero.bakingapp.MainActivity;
import com.android.leonardotalero.bakingapp.R;
import com.android.leonardotalero.bakingapp.data.BakingContract;

/**
 * Created by leonardotalero on 7/8/17.
 */

public class NotificationUtils {

    /*
     * The columns of data that we are interested in displaying within our notification to let
     * the user know there is new weather data available.
     */
    public static final String[] RECIPE_NOTIFICATION_PROJECTION = {

            BakingContract.BakingEntry.COLUMN_DATE,
            BakingContract.BakingEntry.COLUMN_RECIPE_ID,
            BakingContract.BakingEntry.COLUMN_RECIPE_NAME,
            BakingContract.BakingEntry.COLUMN_INGREDIENTS,
            BakingContract.BakingEntry.COLUMN_STEPS

    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able
     * to access the data from our query. If the order of the Strings above changes, these
     * indices must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_COLUMN_DATE = 1;
    public static final int INDEX_COLUMN_RECIPE_ID = 2;
    public static final int INDEX_RECIPE_NAME = 3;
    public static final int INDEX_INGREDIENTS=4;
    public static final int INDEX_STEPS=5;

    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 3004 is in no way significant.
     */
    private static final int WEATHER_NOTIFICATION_ID = 1234;

    /**
     * Constructs and displays a notification for the newly updated weather for today.
     *
     * @param context Context used to query our ContentProvider and use various Utility methods
     */
    public static void notifyUserOfNewData(Context context) {

        Uri dataUri =BakingContract.BakingEntry
                .CONTENT_URI;

        /*
         * The MAIN_FORECAST_PROJECTION array passed in as the second parameter is defined in our WeatherContract
         * class and is used to limit the columns returned in our cursor.
         */
        Cursor cursor = context.getContentResolver().query(
                dataUri,
                RECIPE_NOTIFICATION_PROJECTION,
                null,
                null,
                null);

        /*
         * If cursor is empty, moveToFirst will return false. If our cursor is not
         * empty, we want to show the notification.
         */
        if (cursor.moveToFirst()) {

            /* Weather ID as returned by API, used to identify the icon to be used */
            Long date = cursor.getLong(INDEX_COLUMN_DATE);
            int recipeId = cursor.getInt(INDEX_COLUMN_RECIPE_ID);
            String recipeName = cursor.getString(INDEX_RECIPE_NAME);
            String ingredientes = cursor.getString(INDEX_INGREDIENTS);
            String steps = cursor.getString(INDEX_STEPS);


            Resources resources = context.getResources();
            //int largeArtResourceId = SunshineWeatherUtils
            //        .getLargeArtResourceIdForWeatherCondition(recipeId);

            Bitmap largeIcon = BitmapFactory.decodeResource(
                    resources,
                    R.mipmap.ic_notification);

            String notificationTitle = context.getString(R.string.app_name);

            String notificationText = getNotificationText(context, recipeId, recipeName);

            /* getSmallArtResourceIdForWeatherCondition returns the proper art to show given an ID */
            /*int smallArtResourceId = SunshineWeatherUtils
                    .getSmallArtResourceIdForWeatherCondition(recipeId);
            */
            /*
             * NotificationCompat Builder is a very convenient way to build backward-compatible
             * notifications. In order to use it, we provide a context and specify a color for the
             * notification, a couple of different icons, the title for the notification, and
             * finally the text of the notification, which in our case in a summary of today's
             * forecast.
             */
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setColor(ContextCompat.getColor(context,R.color.colorPrimary))
                    .setSmallIcon(R.mipmap.ic_notification)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setAutoCancel(true);

            /*
             * This Intent will be triggered when the user clicks the notification. In our case,
             * we want to open Sunshine to the DetailActivity to display the newly updated weather.
             */
            Intent detailIntentForToday = new Intent(context, MainActivity.class);
            detailIntentForToday.setData(dataUri);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(detailIntentForToday);
            PendingIntent resultPendingIntent = taskStackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            /* WEATHER_NOTIFICATION_ID alrecipeImages you to update or cancel the notification later on */
            notificationManager.notify(WEATHER_NOTIFICATION_ID, notificationBuilder.build());

            /*
             * Since we just showed a notification, save the current time. That way, we can check
             * next time the weather is refreshed if we should show another notification.
             */
            //SunshinePreferences.saveLastNotificationTime(context, System.currentTimeMillis());
        }

        /* Always close your cursor when you're done with it to avoid wasting resources. */
        cursor.close();
    }

    /**
     * Constructs and returns the summary of a particular day's forecast using various utility
     * methods and resources for formatting. This method is only used to create the text for the
     * notification that appears when the weather is refreshed.
     * <p>
     * The String returned from this method will look something like this:
     * <p>
     * Forecast: Sunny - recipeName: 14°C recipeImage 7°C
     *
     * @param context   Used to access utility methods and resources
     * @param recipeId ID as determined by Open Weather Map
     * @param recipeName      recipeName temperature (either celsius or fahrenheit depending on preferences)
      * @return Summary of a particular day's forecast
     */
    private static String getNotificationText(Context context, int recipeId, String recipeName) {

        /*
         * Short description of the weather, as provided by the API.
         * e.g "clear" vs "sky is clear".
         */
        String shortDescription =context.getString(R.string.notification)+recipeName;

        //String notificationFormat = context.getString(R.string.notification);

        /* Using String's format method, we create the forecast summary */
        /*String notificationText = String.format(notificationFormat,
                shortDescription,
                SunshineWeatherUtils.formatTemperature(context, recipeName),
                SunshineWeatherUtils.formatTemperature(context, recipeImage));

            */
        return shortDescription;
    }
}
