package com.android.leonardotalero.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import com.android.leonardotalero.bakingapp.Utilities.JsonUtils;
import com.android.leonardotalero.bakingapp.data.BakingContract;
import com.android.leonardotalero.bakingapp.data.BakingPreferences;
import com.android.leonardotalero.bakingapp.objects.Recipe;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static Cursor mCursor;
    private static List<Recipe> mRecipes;
    private static String nameRecipe;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

         RemoteViews rv;
        rv = getGridRemoteView(context);
        appWidgetManager.updateAppWidget(appWidgetId, rv);

    }

    @Override
    public  void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        bakingWidgetService.startActionWidgetUpdate(context);
    }

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    private static RemoteViews getGridRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list);
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);
        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, StepDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        // Handle empty gardens
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);

        Integer id= BakingPreferences.areRecipeFavorite(context);
        //Query to get the plant that's most in need for water (last watered)
        String selection=String.valueOf(id);
        //Query to get the plant that's most in need for water (last watered)
        Uri PLANT_URI = BakingContract.BakingEntry.buildUri(Long.valueOf(selection));
        mCursor = context.getContentResolver().query(
                PLANT_URI,
                null,
                selection,
                null,
                null
        );
        if(mCursor!=null && mCursor.getCount()!=0){
            int ingredientsIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_INGREDIENTS);
            mCursor.moveToPosition(0);
            //ingredientsString = mCursor.getString(ingredientsIndex);
            //ingredientsList= JsonUtils.jsonToIngredientsList(ingredientsString);
            mRecipes=JsonUtils.cursorToList(mCursor);
            nameRecipe=mRecipes.get(0).mName;
        }



        views.setTextViewText(R.id.text_title_recipe,nameRecipe);

        return views;
    }
}

