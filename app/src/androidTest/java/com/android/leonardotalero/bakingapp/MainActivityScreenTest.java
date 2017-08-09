package com.android.leonardotalero.bakingapp;

import org.junit.runner.RunWith;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.android.leonardotalero.bakingapp.data.BakingContract;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.CursorMatchers.withRowString;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by gtalero on 8/8/17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    public static final String NAME_RECIPE = "Nutella Pie";

    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @Before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testSample(){
       // if (getRVcount() > 0){
            onView(withId(R.id.my_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

         //   onView(withId(R.id.title)).check(matches(withText(NAME_RECIPE)));
        // Check item at position 3 has "Some content"
        onView(withId(R.id.my_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(NAME_RECIPE)), click()));

        onView(withId(R.id.my_recycler_view))
                .check(matches(hasDescendant(withText(NAME_RECIPE))));

    }

   /* private int getRVcount(){
        RecyclerView recyclerView = (RecyclerView) MainActivity.getActivity().findViewById(R.id.my_recycler_view);
        return recyclerView.getAdapter().getItemCount();
    }
    */

}