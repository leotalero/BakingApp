package com.android.leonardotalero.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.leonardotalero.bakingapp.IngredientFragment.OnListFragmentInteractionListener;
import com.android.leonardotalero.bakingapp.objects.Ingredient;

import java.util.List;


public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder> {

    private final List<Ingredient> mValues;
    private final OnListFragmentInteractionListener mListener;

    public IngredientRecyclerViewAdapter(List<Ingredient> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).iIngredient);
        holder.mMeasuView.setText(mValues.get(position).iMeasure);
        holder.mQuantiView.setText(String.valueOf(mValues.get(position).iQuantity));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mQuantiView;
        public final TextView mMeasuView;
        public Ingredient mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.in_name);
            mQuantiView = (TextView) view.findViewById(R.id.in_quantity);
            mMeasuView = (TextView) view.findViewById(R.id.in_measure);
        }


    }
}
