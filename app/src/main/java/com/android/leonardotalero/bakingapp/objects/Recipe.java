package com.android.leonardotalero.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by leonardotalero on 7/8/17.
 */

public class Recipe implements Parcelable {


    public int mId;
    public String mName;
    public List<Ingredient> mIngredients;
    public List<Step> mSteps;
    public String mServings;
    public String mImage;


    public Recipe() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mName);
        dest.writeTypedList(this.mIngredients);
        dest.writeTypedList(this.mSteps);
        dest.writeString(this.mServings);
        dest.writeString(this.mImage);
    }

    protected Recipe(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mIngredients = in.createTypedArrayList(Ingredient.CREATOR);
        this.mSteps = in.createTypedArrayList(Step.CREATOR);
        this.mServings = in.readString();
        this.mImage = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
