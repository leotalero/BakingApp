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
                handleActionSearch(getApplicationContext());
            }
        }
    }


    public static void startActionWaterPlants(Context context) {
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


    private void handleActionSearch(Context context) {


        Integer id= BakingPreferences.areRecipeFavorite(context);
        //Query to get the plant that's most in need for water (last watered)
        String selection=String.valueOf(id);
        Uri PLANT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_Recipe).build();
        Cursor cursor = getContentResolver().query(
                PLANT_URI,
                null,
                selection,
                null,
                null
        );
        // Extract the Baking details
       // int imgRes = R.drawable.grass; // Default image in case our garden is empty
      //  boolean canWater = false; // Default to hide the water drop button
      //  long plantId = INVALID_PLANT_ID;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int idIndex = cursor.getColumnIndex(BakingContract.BakingEntry._ID);
            int ingredientIndex = cursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_INGREDIENTS);
            //int waterTimeIndex = cursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME);
            //int plantTypeIndex = cursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_PLANT_TYPE);
            ingredients = cursor.getString(ingredientIndex);
            cursor.close();
        }
        ingredientsList = new ArrayList<Ingredient>();
        ingredientsList= JsonUtils.jsonToIngredientsList(ingredients);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        //Now update all widgets
        //PlantWidgetProvider.updatePlantWidgets(this, appWidgetManager, imgRes,plantId ,canWater,appWidgetIds);
    }

}
