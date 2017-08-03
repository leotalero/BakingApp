package com.android.leonardotalero.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.leonardotalero.bakingapp.Utilities.JsonUtils;
import com.android.leonardotalero.bakingapp.data.BakingContract;
import com.android.leonardotalero.bakingapp.data.BakingPreferences;
import com.android.leonardotalero.bakingapp.objects.Ingredient;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.AppCompatDrawableManager.get;
import static com.android.leonardotalero.bakingapp.data.BakingContract.BASE_CONTENT_URI;
import static com.android.leonardotalero.bakingapp.data.BakingContract.PATH_Recipe;


public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public  void onDataSetChanged() {
        Integer id= BakingPreferences.areRecipeFavorite(mContext);
        //Query to get the plant that's most in need for water (last watered)
        String selection=String.valueOf(id);
        //Query to get the plant that's most in need for water (last watered)
        Uri PLANT_URI = BakingContract.BakingEntry.buildUri(Long.valueOf(selection));
        mCursor = mContext.getContentResolver().query(
                PLANT_URI,
                null,
                selection,
                null,
                null
        );
    }

    @Override
    public void onDestroy() {
        if(mCursor!=null){
            mCursor.close();
        }

    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0)
            return null;
        mCursor.moveToPosition(position);
        int idIndex = mCursor.getColumnIndex(BakingContract.BakingEntry._ID);
        int ingredientsIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_INGREDIENTS);
        //int waterTimeIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_LAST_WATERED_TIME);
        //int plantTypeIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_PLANT_TYPE);

        long plantId = mCursor.getLong(idIndex);
        String ingredientsString = mCursor.getString(ingredientsIndex);
        //long createdAt = mCursor.getLong(createTimeIndex);
        //long wateredAt = mCursor.getLong(waterTimeIndex);
        //long timeNow = System.currentTimeMillis();

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.plant_widget);
        List<Ingredient> ingredientsList = new ArrayList<Ingredient>();
        ingredientsList= JsonUtils.jsonToIngredientsList(ingredientsString);

        // Update the plant image
        //int imgRes = PlantUtils.getPlantImageRes(mContext, timeNow - createdAt, timeNow - wateredAt, plantType);
        views.setTextViewText(R.id.widget_plant_name, ingredientsList.get(0).iIngredient);
        //views.setTextViewText(R.id.widget_plant_name, String.valueOf(plantId));
        // Always hide the water drop in GridView mode
        views.setViewVisibility(R.id.widget_water_button, View.GONE);

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        Bundle extras = new Bundle();
        extras.putString(StepDetailActivity.EXTRA_INGREDIENTS_ID, ingredientsString);
        extras.putString(StepDetailActivity.EXTRA_ID, "990");
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_plant_image, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

