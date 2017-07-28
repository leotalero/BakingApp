package com.android.leonardotalero.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leonardotalero.bakingapp.Utilities.JsonUtils;
import com.android.leonardotalero.bakingapp.Utilities.NetworkUtils;
import com.android.leonardotalero.bakingapp.data.BakingContract;
import com.android.leonardotalero.bakingapp.objects.Recipe;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;


public class RecipeRecyclerAdapter extends   RecyclerView.Adapter<RecipeRecyclerAdapter.ViewHolder>  {


        //private String[] mDataset;
        private Cursor mCursor;
        private Context mContext;
        private List<Recipe> mRecipes;
    private String RECIPE_OBJECT="recipe";


    public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            //public TextView mTextView;
            private final TextView title, count;
            private final ImageView thumbnail, overflow;


            public ViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.title);
                count = (TextView) v.findViewById(R.id.count);
                thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
                overflow = (ImageView) v.findViewById(R.id.overflow);
            }
        }

        public RecipeRecyclerAdapter(@NonNull Context mContext) {
            this.mContext = mContext;
            //this.mRecipes = recipes;


        }

        // Create new views (invoked by the layout manager)
        @Override
        public RecipeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);

            return new ViewHolder(itemView);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            mCursor.moveToPosition(position);
            int name = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_NAME);
            int image = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_IMAGE);

            holder.title.setText(mCursor.getString(name));
           // holder.thumbnail


            /*String urlImage=mCursor.getString(image);
            if (urlImage==null || urlImage.equals("")){
                urlImage="https://www.google.com/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&ved=0ahUKEwjYmfGs6KzVAhXFCD4KHYqzCIoQjRwIBw&url=https%3A%2F%2Fpixabay.com%2Fen%2Frecipe-label-icon-symbol-spoon-575434%2F&psig=AFQjCNGn3pJgdYIAK5Dj7QODic4BNczlyg&ust=1501360094865522";
            }*/
            String url_path = mCursor.getString(image);
            if (url_path==null || url_path.equals("")){
                url_path="https://www.google.com/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&ved=0ahUKEwjYmfGs6KzVAhXFCD4KHYqzCIoQjRwIBw&url=https%3A%2F%2Fpixabay.com%2Fen%2Frecipe-label-icon-symbol-spoon-575434%2F&psig=AFQjCNGn3pJgdYIAK5Dj7QODic4BNczlyg&ust=1501360094865522";
            }
            URL url = NetworkUtils.buildUrlImage(url_path);

            Picasso.with(mContext).load(url.toString())
                    .placeholder(R.drawable.recipe)
                    .error(R.drawable.recipe)
                    .into(holder.thumbnail);

            //Recipe recipe = mRecipes.get(position);
            //holder.title.setText(recipe.mName.toString());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // showPopupMenu(holder.overflow);
                    Intent intentToStartDetailActivity = new Intent(mContext, StepListActivity.class);
                    Recipe recipe=mRecipes.get(position);
                    intentToStartDetailActivity.putExtra(RECIPE_OBJECT , recipe);
                    mContext.startActivity(intentToStartDetailActivity);
                Toast.makeText(mContext,
                            R.string.no_data, Toast.LENGTH_LONG).show();
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if (null == mCursor) return 0;
           int num =mCursor.getCount();
            return   num;

            // case list not cursor -
            // return mRecipes.size();
        }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mRecipes=JsonUtils.cursorToList(mCursor);
    }


    public interface AdapterOnClickHandler {
        void onClick(int id);
    }

}







