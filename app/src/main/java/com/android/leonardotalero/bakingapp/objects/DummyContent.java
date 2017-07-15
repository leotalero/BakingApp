package com.android.leonardotalero.bakingapp.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Recipe> ITEMS = new ArrayList<Recipe>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Recipe> ITEM_MAP = new HashMap<String, Recipe>();

    private static final int COUNT = 2;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createRecipe(i));
        }
    }

    private static void addItem(Recipe item) {
        ITEMS.add(item);
        //ITEM_MAP.put(item.mId, item);
    }

    private static Recipe createRecipe(int position) {
        Recipe recipe =new Recipe();
        recipe.mId=position;
        recipe.mName="brwonies"+position;

        Ingredient ingr=new Ingredient();
        ingr.iIngredient="chocolate";
        ingr.iMeasure="1 spun";
        List<Ingredient> ingredients=new ArrayList<Ingredient>();
        ingredients.add(ingr);

        recipe.mIngredients=ingredients;

        Step step=new Step();
        step.sId=1;
        step.sDescription="Mix";
        List<Step> steps=new ArrayList<Step>();
        steps.add(step);
        recipe.mSteps=steps;

        return recipe;

    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    /*public static class Recipe {
        public final String id;
        public final String content;
        public final String details;

        public Recipe(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }*/
}
