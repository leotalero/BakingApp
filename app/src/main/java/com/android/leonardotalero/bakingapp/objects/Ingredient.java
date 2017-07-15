package com.android.leonardotalero.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leonardotalero on 7/8/17.
 */

public class Ingredient implements Parcelable {
    public float iQuantity;
    public String iMeasure;
    public String iIngredient;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.iQuantity);
        dest.writeString(this.iMeasure);
        dest.writeString(this.iIngredient);
    }

    public Ingredient() {
    }

    protected Ingredient(Parcel in) {
        this.iQuantity = in.readFloat();
        this.iMeasure = in.readString();
        this.iIngredient = in.readString();
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
