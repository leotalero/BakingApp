package com.android.leonardotalero.bakingapp;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;
import com.android.leonardotalero.bakingapp.data.BakingContract;

import com.android.leonardotalero.bakingapp.databinding.ActivityMainBinding;
import android.databinding.DataBindingUtil;

import com.android.leonardotalero.bakingapp.objects.Recipe;
import com.android.leonardotalero.bakingapp.sync.SyncIntentService;
import com.android.leonardotalero.bakingapp.sync.SyncUtils;


import java.util.ArrayList;
import java.util.List;

import static com.android.leonardotalero.bakingapp.sync.SyncUtils.startImmediateSync;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {


    ActivityMainBinding mBinding;


    private ProgressBar mLoadingIndicator;
    private static final int ID_RECIPE_LOADER =21;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private int mPosition = RecyclerView.NO_POSITION;
    private boolean mTablet=false;

    private List<Recipe> mRecipe=new ArrayList<Recipe>();

    public static final String[] ALL_DATA = {
            BakingContract.BakingEntry.COLUMN_DATE,
            BakingContract.BakingEntry.COLUMN_RECIPE_ID,
            BakingContract.BakingEntry.COLUMN_RECIPE_NAME,
            BakingContract.BakingEntry.COLUMN_IMAGE,
            BakingContract.BakingEntry.COLUMN_INGREDIENTS,
            BakingContract.BakingEntry.COLUMN_STEPS,
            BakingContract.BakingEntry.COLUMN_SERVINGS


    };
    private RecipeRecyclerAdapter mAdapter;
    private int mColumnCount=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mLoadingIndicator=mBinding.pbLoadingIndicator;


        //mRecipe.add(new Recipe(1,"prueba",new ArrayList<Ingredient>(),new ArrayList<Step>(),"",""));
        showLoading();
        //load data in content provider

        //Intent intentToSyncImmediately = new Intent(this, SyncIntentService.class);
        //startService(intentToSyncImmediately);





        if(findViewById(R.id.linear_layout_tablet) != null){
            mTablet=true;
            mColumnCount=2;

        }
        loadData();

        SyncUtils.initialize(this);
       getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
       // SyncUtils.initialize(this);
    }

    private void loadData() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        //mLayoutManager = new LinearLayoutManager(this);
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }
        //mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecipeRecyclerAdapter(this,null);
        mRecyclerView.setAdapter(mAdapter);
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
                return new CursorLoader(this, forecastQueryUri, null,
                        null, null, null);


            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);



    }


    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        mAdapter.notifyDataSetChanged();
        //Refresh your stuff here
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }



    private void showDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        //mFragment.setVisibility(View.VISIBLE);
    }
    private void showLoading() {
        /* Then, hide the weather data */
        //mFragment.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }


    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("resultList", mAdapter);
        outState.putParcelableArrayList("resultReviews", resultReviews);
        outState.putBoolean("isInFavorites", isInFavorites);

        super.onSaveInstanceState(outState);
    }


*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        if (itemThatWasClickedId == R.id.action_search) {

            startImmediateSync(this);
            return true;
        }

        //buildUrlSearch
        return super.onOptionsItemSelected(item);
    }
}

