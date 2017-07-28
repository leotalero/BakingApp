package com.android.leonardotalero.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.android.leonardotalero.bakingapp.Utilities.JsonUtils;
import com.android.leonardotalero.bakingapp.Utilities.NetworkUtils;
import com.android.leonardotalero.bakingapp.objects.Ingredient;
import com.android.leonardotalero.bakingapp.objects.Recipe;
import com.android.leonardotalero.bakingapp.objects.Step;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity implements IngredientFragment.OnListFragmentInteractionListener{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Recipe mRecipe;
    private List<Step> mStep=new ArrayList<Step>();
    private List<Ingredient> mIngredients=new ArrayList<Ingredient>();
    private int ID_STEP_INGREDIENT=990;
    private String DESC_STEP_INGREDIENT="";
    private String RECIPE_OBJECT="recipe";
    private SimpleItemRecyclerViewAdapter mAdapter;
    public Context c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=this;
        setContentView(R.layout.activity_step_list);
        DESC_STEP_INGREDIENT=getString(R.string.ingredients);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

        }

        if (savedInstanceState != null) {
           mStep= savedInstanceState.getParcelableArrayList("stepsList");
            mRecipe= savedInstanceState.getParcelable(RECIPE_OBJECT);
           // mAdapter.notifyDataSetChanged();
        }

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(RECIPE_OBJECT) && savedInstanceState==null ) {
                mRecipe = intentThatStartedThisActivity.getExtras().getParcelable(RECIPE_OBJECT);
                mIngredients=mRecipe.mIngredients;
                Step step=new Step(ID_STEP_INGREDIENT,DESC_STEP_INGREDIENT,DESC_STEP_INGREDIENT,"","");
                mStep.add(step);
                mStep.addAll(mRecipe.mSteps);
            }
        }

        View recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView,mStep);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Step> steps) {
        mStep=steps;
        mAdapter=new SimpleItemRecyclerViewAdapter(mStep,c);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onListFragmentInteraction(Ingredient item) {

    }




    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Step> mValues;
        private Context mContex;

        public SimpleItemRecyclerViewAdapter(List<Step> items,@NonNull Context mContext) {
            this.mContex=mContext;
            mValues = items;
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.mItem = mValues.get(position);
            holder.title.setText(mValues.get(position).sShortDescription);
           // holder..setText(mValues.get(position).content);
            String url_path = mValues.get(position).sThumbnailURL;

            if (url_path==null || url_path.equals("")){

                if((holder.mItem.sId)==ID_STEP_INGREDIENT) {
                    //  url_path="https://www.google.com/imgres?imgurl=http%3A%2F%2Fwww.candyindustry.com%2Fext%2Fresources%2Fissues%2F2015-June%2FCIN_IngTech_0615_feature.jpg%3F1435000230&imgrefurl=http%3A%2F%2Fwww.candyindustry.com%2Farticles%2F86802-tracking-confectionery-ingredient-trends&docid=mdzwjrlOXyyOMM&tbnid=HWZPLpMP-dQ5kM%3A&vet=10ahUKEwi69oGu-qzVAhVEaD4KHVrAC0oQMwglKAAwAA..i&w=900&h=550&client=safari&bih=661&biw=1240&q=image%20ingredient&ved=0ahUKEwi69oGu-qzVAhVEaD4KHVrAC0oQMwglKAAwAA&iact=mrc&uact=8"; }
                    url_path = "https://www.google.com/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&ved=0ahUKEwjYmfGs6KzVAhXFCD4KHYqzCIoQjRwIBw&url=https%3A%2F%2Fpixabay.com%2Fen%2Frecipe-label-icon-symbol-spoon-575434%2F&psig=AFQjCNGn3pJgdYIAK5Dj7QODic4BNczlyg&ust=1501360094865522";

                } else{
                    url_path="https://www.google.com/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&ved=0ahUKEwieysKZ96zVAhUNfiYKHcbhCOgQjRwIBw&url=https%3A%2F%2Fthenounproject.com%2Fterm%2Fsteps%2F187775%2F&psig=AFQjCNH-SWQX8bBta4AZpvHbiKtCn2XxcA&ust=1501364077976036";
                }
            }else{

            }
            //URL url = NetworkUtils.buildUrlImage(url_path);
            URL url = NetworkUtils.buildUrlImage(url_path);

            Picasso.with(mContex).load(url.toString())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(holder.thumbnail);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int id=holder.itemView.getId();
                    if (mTwoPane) {


                        if((holder.mItem.sId)==ID_STEP_INGREDIENT){

                            IngredientFragment fragment = new IngredientFragment().newInstance(1,mIngredients);
                            //fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.step_detail_container, fragment)
                                    .commit();


                        }else{
                            StepDetailFragment fragment = new StepDetailFragment().newInstance(1,holder.mItem);
/*
                            Bundle arguments = new Bundle();
                            arguments.putParcelableArrayList(StepDetailFragment.ARG_ITEM_INGREDIENT, JsonUtils.listToArrayIngredient(mIngredients));
                            arguments.putString(StepDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.sId));
                            arguments.putParcelable(StepDetailFragment.ARG_ITEM_STEP, holder.mItem);
                            arguments.putParcelable(StepDetailFragment.ARG_ITEM, mRecipe);
*/

                            //fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.step_detail_container, fragment)
                                    .commit();
                        }

                    } else {
                        c = v.getContext();
                        Intent intent = new Intent(c, StepDetailActivity.class);
                        intent.putExtra(StepDetailFragment.ARG_ITEM_INGREDIENT, JsonUtils.listToArrayIngredient(mIngredients));
                        intent.putExtra(StepDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.sId));
                        intent.putExtra(StepDetailFragment.ARG_ITEM_STEP, holder.mItem);
                        intent.putExtra(StepDetailFragment.ARG_ITEM, mRecipe);
                        c.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            //public final View mView;
           // public final TextView mIdView;
           // public final TextView mContentView;
            public Step mItem;
            public  TextView title, count;
            public  ImageView thumbnail, overflow;

            public ViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title_step);
                count = (TextView) view.findViewById(R.id.count_step);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail_step);
                overflow = (ImageView) view.findViewById(R.id.overflow_step);
            }


        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("stepsList", JsonUtils.listToArrayStep(mStep));
        outState.putParcelable(RECIPE_OBJECT, mRecipe);

        super.onSaveInstanceState(outState);


    }
}
