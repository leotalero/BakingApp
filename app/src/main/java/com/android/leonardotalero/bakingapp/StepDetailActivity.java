package com.android.leonardotalero.bakingapp;

import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.android.leonardotalero.bakingapp.Utilities.JsonUtils;
import com.android.leonardotalero.bakingapp.objects.Ingredient;
import com.android.leonardotalero.bakingapp.objects.Recipe;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepDetailActivity extends AppCompatActivity implements IngredientFragment.OnListFragmentInteractionListener {

    private String RECIPE_OBJECT="recipe";
    private String ARG_ITEM_INGREDIENT="ingredient-list";
    private String  ARG_ITEM_ID="mid";
    private Recipe mRecipe;
    private String mId;
    private int ID_STEP_INGREDIENT=990;
    private List<Ingredient> mIngredients;
    private Bundle arguments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
/*
            intent.putExtra(StepDetailFragment.ARG_ITEM_INGREDIENT, JsonUtils.listToArrayIngredient(mIngredients));
            intent.putExtra(StepDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.sId));
            intent.putExtra(StepDetailFragment.ARG_ITEM_STEP, holder.mItem);
       */

            arguments = new Bundle();
            arguments.putString(StepDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(StepDetailFragment.ARG_ITEM_ID));
            arguments.putParcelableArrayList(StepDetailFragment.ARG_ITEM_INGREDIENT,
                    getIntent().getParcelableArrayListExtra(StepDetailFragment.ARG_ITEM_INGREDIENT));
            arguments.putParcelable(StepDetailFragment.ARG_ITEM_STEP,
                    getIntent().getParcelableExtra(StepDetailFragment.ARG_ITEM_STEP));
            arguments.putParcelable(StepDetailFragment.ARG_ITEM,
                    getIntent().getParcelableExtra(StepDetailFragment.ARG_ITEM));
            mRecipe=getIntent().getParcelableExtra(StepDetailFragment.ARG_ITEM);
            mIngredients=getIntent().getParcelableArrayListExtra(StepDetailFragment.ARG_ITEM_INGREDIENT);

            mId=getIntent().getStringExtra(StepDetailFragment.ARG_ITEM_ID);

            gotoFragment();

        }else{


            mRecipe=savedInstanceState.getParcelable(RECIPE_OBJECT);
            mIngredients=savedInstanceState.getParcelableArrayList(ARG_ITEM_INGREDIENT);
            mId=savedInstanceState.getString(ARG_ITEM_ID);
            arguments=savedInstanceState.getParcelable("arguments");
            gotoFragment();
        }
    }


    public void gotoFragment(){

        if(Integer.valueOf(mId)==ID_STEP_INGREDIENT){
            //ingredients_show list

            IngredientFragment fragment = new IngredientFragment().newInstance(1,mIngredients);
            //fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();

        }else{
            //steps_show_ video and detailss
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();


        }




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
           Intent intent=new Intent(this, StepListActivity.class);
            intent.putExtra(RECIPE_OBJECT,mRecipe);
            navigateUpTo(intent
                   );
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    @Override
    public void onListFragmentInteraction(Ingredient item) {

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_OBJECT,mRecipe);
        //outState.putParcelable(ARG_ITEM_INGREDIENT,mIngredients);
        outState.putParcelableArrayList(ARG_ITEM_INGREDIENT,JsonUtils.listToArrayIngredient(mRecipe.mIngredients));
        outState.putString(ARG_ITEM_ID,mId);
        outState.putParcelable("arguments",arguments);
        super.onSaveInstanceState(outState);
    }
}
