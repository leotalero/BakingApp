package com.android.leonardotalero.bakingapp.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.leonardotalero.bakingapp.data.BakingContract;
import com.android.leonardotalero.bakingapp.objects.Ingredient;
import com.android.leonardotalero.bakingapp.objects.Recipe;
import com.android.leonardotalero.bakingapp.objects.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardotalero on 7/12/17.
 */

public class JsonUtils {



        /* Location information */
        private static final String OWM_ID = "id";
        private static final String OWM_NAME = "name";

        /* Location coordinate */
        private static final String OWM_INGREDIENTS = "ingredients";
        private static final String OWM_QUANT = "quantity";

        /* Weather information. Each day's forecast info is an element of the "list" array */
        private static final String OWM_MEASURE = "measure";

        private static final String OWM_INGREDIENT= "ingredient";
        private static final String OWM_STEPS = "steps";
        private static final String OWM_STEP_ID = "id";
        private static final String OWM_STEP_SHORT_DESCRIPTION = "shortDescription";

        /* All temperatures are children of the "temp" object */
        private static final String OWM_STEP_DESCRIPTION = "description";

        /* Max temperature for the day */
        private static final String OWM_STEP_VIDEO = "videoURL";
        private static final String OWM_THUMBNAIL= "thumbnailURL";

        private static final String OWM_SERVINGS = "servings";

         private static final String OWM_IMAGE = "image";
        private static   ArrayList<Recipe> resultRecipes=new ArrayList<Recipe>();

        /**
         * This method parses JSON from a web response and returns an array of Strings
         * describing the weather over various days from the forecast.
         * <p/>
         * Later on, we'll be parsing the JSON into structured data within the
         * getFullWeatherDataFromJson function, leveraging the data we have stored in the JSON. For
         * now, we just convert the JSON into human-readable strings.
         *
         * @param jsonStr JSON response from server
         *
         * @return Array of Strings describing weather data
         *
         * @throws JSONException If JSON data cannot be properly parsed
         */
        public static ContentValues[] getContentValuesFromJson(Context context, String jsonStr)
                throws JSONException {

            JSONArray recipeJson = new JSONArray(jsonStr);


            //SunshinePreferences.setLocationDetails(context, cityLatitude, cityLongitude);

            ContentValues[] contentValues = new ContentValues[recipeJson.length()];


            for (int i = 0; i < recipeJson.length(); i++) {
                Recipe receta=new Recipe();

                JSONArray jsonIngredientsArray = recipeJson.getJSONObject(i).getJSONArray(OWM_INGREDIENTS);
                JSONArray jsonStepsArray = recipeJson.getJSONObject(i).getJSONArray(OWM_STEPS);

                int idRecipeJson = recipeJson.getJSONObject(i).getInt(OWM_ID);
                String nameRecipeJson = recipeJson.getJSONObject(i).getString(OWM_NAME);
                String servingsRecipeJson = recipeJson.getJSONObject(i).getString(OWM_SERVINGS);
                String imageRecipeJson = recipeJson.getJSONObject(i).getString(OWM_IMAGE);


                List<Ingredient> ingredientes=jsonToIngredientsList(jsonIngredientsArray.toString());
                List<Step> steps=jsonToStepList(jsonStepsArray.toString());

                receta.mId=idRecipeJson;
                receta.mName=nameRecipeJson;
                receta.mServings=servingsRecipeJson;
                receta.mImage=imageRecipeJson;
                receta.mIngredients =ingredientes;
                receta.mSteps=steps;

                ContentValues values = new ContentValues();
                values.put(BakingContract.BakingEntry.COLUMN_RECIPE_ID, receta.mId);
                values.put(BakingContract.BakingEntry.COLUMN_RECIPE_NAME, receta.mName);
                values.put(BakingContract.BakingEntry.COLUMN_INGREDIENTS, jsonIngredientsArray.toString());
                values.put(BakingContract.BakingEntry.COLUMN_STEPS, jsonStepsArray.toString());
                values.put(BakingContract.BakingEntry.COLUMN_SERVINGS, receta.mServings);
                values.put(BakingContract.BakingEntry.COLUMN_IMAGE, receta.mImage);


                contentValues[i] = values;


            }



            return contentValues;
        }


        public static List<Ingredient> jsonToIngredientsList(String ingredients){

            List<Ingredient> Ingredientes=new ArrayList<Ingredient>();
            try {

                JSONArray recipeJson = new JSONArray(ingredients);



                for (int i = 0; i < recipeJson.length(); i++) {


                    int quantity;
                    String measure;
                    String ingredient;
                    JSONObject ingrediente = recipeJson.getJSONObject(i);


                    quantity = ingrediente.getInt(OWM_QUANT);
                    measure = ingrediente.getString(OWM_MEASURE);
                    ingredient = ingrediente.getString(OWM_INGREDIENT);



                    Ingredient ingredientFinal= new Ingredient();
                    ingredientFinal.iQuantity=quantity;
                    ingredientFinal.iMeasure=measure;
                    ingredientFinal.iIngredient=ingredient;

                    Ingredientes.add(ingredientFinal);


                }




            } catch (JSONException e) {
                e.printStackTrace();
            }


            return Ingredientes;
        }



    public static List<Step> jsonToStepList(String steps){

        List<Step> stepsFinal=new ArrayList<Step>();
        try {

            JSONArray recipeJson = new JSONArray(steps);



            for (int i = 0; i < recipeJson.length(); i++) {


                int id;
                String shortDesc;
                String description;
                String videoUrl;
                String thumbnail;

                JSONObject step = recipeJson.getJSONObject(i);


                id = step.getInt(OWM_STEP_ID);
                shortDesc = step.getString(OWM_STEP_SHORT_DESCRIPTION);
                description = step.getString(OWM_STEP_DESCRIPTION);
                videoUrl = step.getString(OWM_STEP_VIDEO);
                thumbnail = step.getString(OWM_THUMBNAIL);



                Step stepObject= new Step();
                stepObject.sId=id;
                stepObject.sShortDescription=shortDesc;
                stepObject.sDescription=description;
                stepObject.sVideoUrl=videoUrl;
                stepObject.sThumbnailURL=thumbnail;


                stepsFinal.add(stepObject);


            }




        } catch (JSONException e) {
            e.printStackTrace();
        }


        return stepsFinal;
    }


    public static ArrayList<Recipe> listToArrayRecipe(List<Recipe> list) {
        //resultMovies= new ArrayList<MovieClass>();
        resultRecipes.addAll(list);
        return  resultRecipes;
    }
    public static ArrayList<Ingredient> listToArrayIngredient(List<Ingredient> list) {
        //resultMovies= new ArrayList<MovieClass>();
        ArrayList<Ingredient> result=new ArrayList<Ingredient>();
        result.addAll(list);
        return  result;
    }
    public static ArrayList<Step> listToArrayStep(List<Step> list) {
        //resultMovies= new ArrayList<MovieClass>();
        ArrayList<Step> result=new ArrayList<Step>();
        result.addAll(list);
        return  result;
    }




    public static List<Recipe> cursorToList(Cursor data) {

       List<Recipe> mRecipe=new ArrayList<>();
        if(data ==null){
            return mRecipe;
        }
        int date=data.getColumnIndex(BakingContract.BakingEntry.COLUMN_DATE);
        int id=data.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_ID);
        int name=data.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_NAME);
        int image=data.getColumnIndex(BakingContract.BakingEntry.COLUMN_IMAGE);
        int ingredients=data.getColumnIndex(BakingContract.BakingEntry.COLUMN_INGREDIENTS);
        int steps=data.getColumnIndex(BakingContract.BakingEntry.COLUMN_STEPS);
        int servings=data.getColumnIndex(BakingContract.BakingEntry.COLUMN_SERVINGS);

        if (data.moveToFirst()){
            while(!data.isAfterLast()){
                String mDate = data.getString(date);
                int  mId= data.getInt(id);
                String mName = data.getString(name);
                String mIngredients=data.getString(ingredients);
                List<Ingredient> listIngredients = JsonUtils.jsonToIngredientsList(mIngredients);
                List<Step> mSteps=JsonUtils.jsonToStepList(data.getString(steps));
                String mServings=data.getString(servings);
                String mImage=data.getString(image);

                // do what ever you want here
                Recipe recipe=new Recipe(mId,mName,listIngredients,mSteps,mServings,mImage);

                mRecipe.add(recipe);

                data.moveToNext();
            }
        }
        //data.close();

        return mRecipe;

    }

}



