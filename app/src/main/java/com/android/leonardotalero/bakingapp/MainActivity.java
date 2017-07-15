package com.android.leonardotalero.bakingapp;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.android.leonardotalero.bakingapp.data.BakingContract;
import com.android.leonardotalero.bakingapp.data.BakingProvider;
import com.android.leonardotalero.bakingapp.databinding.ActivityMainBinding;
import android.databinding.DataBindingUtil;
import android.widget.Toast;

import com.android.leonardotalero.bakingapp.objects.Recipe;
import com.android.leonardotalero.bakingapp.sync.SyncIntentService;

public class MainActivity extends AppCompatActivity implements RecipeMainFragment.OnListFragmentInteractionListener,
        LoaderManager.LoaderCallbacks<Cursor> {


    ActivityMainBinding mBinding;
    private RecipeRecyclerAdapter adapter;
    private FrameLayout mFragment;
    private ProgressBar mLoadingIndicator;
    private static final int ID_RECIPE_LOADER = 11;
    private static final int ID_LOAD_DUMMY_LOADER=87;

    public static final String[] ALL_DATA = {
            BakingContract.BakingEntry.COLUMN_DATE,
            BakingContract.BakingEntry.COLUMN_RECIPE_ID,
            BakingContract.BakingEntry.COLUMN_RECIPE_NAME,
            BakingContract.BakingEntry.COLUMN_IMAGE,
            BakingContract.BakingEntry.COLUMN_INGREDIENTS,
            BakingContract.BakingEntry.COLUMN_STEPS,
            BakingContract.BakingEntry.COLUMN_SERVINGS


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mLoadingIndicator=mBinding.pbLoadingIndicator;
        mFragment=mBinding.contentMain;

        showLoading();
        //load data in content provider
        //getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);


        //load data dummy bulck
        loadDataDummy();
        Intent intentToSyncImmediately = new Intent(this, SyncIntentService.class);
        startService(intentToSyncImmediately);





        
    }

    private void loadDataDummy() {

        //getSupportLoaderManager().initLoader(ID_LOAD_DUMMY_LOADER, null, this);
    }


    private void MainFragmentLoad() {
        RecipeMainFragment RecipeMainFragment = com.android.leonardotalero.bakingapp.RecipeMainFragment.newInstance(2);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, RecipeMainFragment).commit();
    }



    public void onListFragmentInteraction(Recipe item) {

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {

            case ID_RECIPE_LOADER:

                Uri forecastQueryUri = BakingContract.BakingEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = BakingContract.BakingEntry.COLUMN_DATE + " ASC";

                String selection = null;

                return new android.support.v4.content.CursorLoader(this,
                        forecastQueryUri,
                        ALL_DATA,
                        selection,
                        null,
                        sortOrder);

            case ID_LOAD_DUMMY_LOADER:

                Uri QueryUri = BakingContract.BakingEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                 sortOrder = null;

                 selection = null;
                //ContentValues[] contentValues;


               // Uri uri = getContentResolver().bulkInsert(QueryUri, contentValues);

                // Display the URI that's returned with a Toast
                // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
               // if(uri != null) {

               //     Toast.makeText(getBaseContext(), "data saved", Toast.LENGTH_LONG).show();
               // }

                return null;
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        //adapter.swapCursor(data);
        //if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        //mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) {
            showDataView();
            MainFragmentLoad();
        }else{
            Toast.makeText(MainActivity.this,
                    R.string.no_data, Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }



    private void showDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mFragment.setVisibility(View.VISIBLE);
    }
    private void showLoading() {
        /* Then, hide the weather data */
        mFragment.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

}

