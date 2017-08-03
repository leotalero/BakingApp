package com.android.leonardotalero.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.android.leonardotalero.bakingapp.Utilities.JsonUtils;
import com.android.leonardotalero.bakingapp.data.BakingContract;
import com.android.leonardotalero.bakingapp.data.BakingPreferences;
import com.android.leonardotalero.bakingapp.objects.Ingredient;
import com.android.leonardotalero.bakingapp.objects.Recipe;

import java.util.ArrayList;
import java.util.List;


public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;
    List<Ingredient> ingredientsList=new ArrayList<Ingredient>();
    String ingredientsString;
    List<Recipe> mRecipes;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {

        List<Ingredient> ingredientsList=new ArrayList<Ingredient>();

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
        int ingredientsIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_INGREDIENTS);
        mCursor.moveToPosition(0);
        ingredientsString = mCursor.getString(ingredientsIndex);
        ingredientsList= JsonUtils.jsonToIngredientsList(ingredientsString);
        mRecipes=JsonUtils.cursorToList(mCursor);



    }

    @Override
    public void onDestroy() {
        if(mCursor!=null){
            mCursor.close();
        }

    }

    @Override
    public int getCount() {
        if (ingredientsList == null) return 0;
        return ingredientsList.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {

       // if (mCursor == null || mCursor.getCount() == 0 || ingredientsList==null  ) return null ;

        //mCursor.moveToPosition(position);
        //int idIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_ID);
        //int ingredientsIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_INGREDIENTS);
       // long plantId = mCursor.getLong(idIndex);
        //ingredientsString = mCursor.getString(ingredientsIndex);
        //ingredientsList= JsonUtils.jsonToIngredientsList(ingredientsString);
        if (mCursor != null || mCursor.getCount() != 0) {

           /* mCursor.moveToPosition(0);
            int idIndex = mCursor.getColumnIndex(BakingContract.BakingEntry._ID);
            int ingredientsIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_INGREDIENTS);
            long plantId = mCursor.getLong(idIndex);
            ingredientsString = mCursor.getString(ingredientsIndex);
            ingredientsList= JsonUtils.jsonToIngredientsList(ingredientsString);
    */
        }


        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget);

        // Update the plant image
        //int imgRes = PlantUtils.getPlantImageRes(mContext, timeNow - createdAt, timeNow - wateredAt, plantType);
        views.setTextViewText(R.id.in_name, ingredientsList.get(position).iIngredient);
        views.setTextViewText(R.id.in_measure, ingredientsList.get(position).iMeasure);
        views.setTextViewText(R.id.in_quantity, String.valueOf(ingredientsList.get(position).iQuantity));
        //views.setTextViewText(R.id.widget_plant_name, String.valueOf(plantId));
        // Always hide the water drop in GridView mode
        //views.setViewVisibility(R.id.widget_water_button, View.GONE);

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        Bundle extras = new Bundle();
        extras.putString(StepDetailActivity.EXTRA_INGREDIENTS_ID, ingredientsString);
        extras.putString(StepDetailActivity.EXTRA_ID, "990");
        extras.putParcelable(StepDetailActivity.EXTRA_RECIPE, mRecipes.get(0));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.itemwidget, fillInIntent);

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

