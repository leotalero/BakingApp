package com.android.leonardotalero.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.android.leonardotalero.bakingapp.Utilities.JsonUtils;
import com.android.leonardotalero.bakingapp.data.BakingContract;
import com.android.leonardotalero.bakingapp.data.BakingPreferences;
import com.android.leonardotalero.bakingapp.objects.Ingredient;

import java.util.ArrayList;
import java.util.List;

import static com.android.leonardotalero.bakingapp.data.BakingContract.BASE_CONTENT_URI;
import static com.android.leonardotalero.bakingapp.data.BakingContract.PATH_Recipe;

/**
 * Created by gtalero on 7/31/17.
 */

public class bakingWidgetService  extends IntentService {

    public static final String ACTION_BAKING_SEARCH =
            "com.android.leonardotalero.action.widget";
    public String ingredients;
    public List<Ingredient> ingredientsList;

    public bakingWidgetService() {
        super("bakingWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_BAKING_SEARCH.equals(action)) {
                handleActionUpdatePlantWidgets();
            }
        }
    }


    public static void startActionWidgetUpdate(Context context) {
        Intent intent = new Intent(context, bakingWidgetService.class);
        intent.setAction(ACTION_BAKING_SEARCH);
        context.startService(intent);
    }


    private void handleActionWaterPlants() {
       /* Uri BAKIN_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_Recipe).build();
        ContentValues contentValues = new ContentValues();
        long timeNow = System.currentTimeMillis();
        contentValues.put(BakingContract.BakingEntry.COLUMN_LAST_WATERED_TIME, timeNow);
        // Update only plants that are still alive
        getContentResolver().update(
                BAKIN_URI,
                contentValues,
                PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME+">?",
                new String[]{String.valueOf(timeNow - PlantUtils.MAX_AGE_WITHOUT_WATER)});
                */
    }


    private void handleActionUpdatePlantWidgets() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        //Now update all widgets
        BakingAppWidget.updateWidgets(this, appWidgetManager, appWidgetIds);
    }





}
