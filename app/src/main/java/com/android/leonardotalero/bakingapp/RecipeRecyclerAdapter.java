package com.android.leonardotalero.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.leonardotalero.bakingapp.RecipeMainFragment.OnListFragmentInteractionListener;

import com.android.leonardotalero.bakingapp.objects.Recipe;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Recipe} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.ViewHolder> {




    private final List<Recipe> mValues;
    private final OnListFragmentInteractionListener mListener;
    private  Context context;
    private Cursor mCursor;


    public RecipeRecyclerAdapter(List<Recipe> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recepie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).mId));
        holder.mContentView.setText(mValues.get(position).mName);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    showDetails(context,holder.mItem);
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    private void showDetails(Context context,Recipe item) {


        Class destinationClass = RecipeListActivity.class;
        Intent intent = new Intent(context, destinationClass);
        //MovieClass movieSelected=resultMovies.get(position);
        //intentToStartDetailActivity.putExtra("MovieObject" , movieSelected);
        context.startActivity(intent);



    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Recipe mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            context = itemView.getContext();
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }



}
