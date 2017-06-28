package com.abronia.android.probaker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.abronia.android.probaker.data.models.Ingredient;
import com.abronia.android.probaker.data.models.Step;
import com.abronia.android.probaker.fragments.IngredientFragment;
import com.abronia.android.probaker.fragments.StepDetailsFragment;
import com.abronia.android.probaker.fragments.StepFragment;

import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements StepFragment.OnListFragmentInteractionListener,
        IngredientFragment.OnListFragmentInteractionListener, StepDetailsFragment.OnFragmentInteractionListener {

    private final Context context = this;
    private int recipeId;
    private String recipeName;

    public static final String ARGS_RECIPE_ID = "recipe_id";
    public static final String ARGS_RECIPE_NAME = "recipe_name";

    private static final String FRAGMENT_STEPS_LIST =
            "com.abronia.android.probaker.fragments.StepFragment";
    private static final String FRAGMENT_INGREDIENTS_LIST =
            "com.abronia.android.probaker.fragments.IngredientFragment";
    private static final String FRAGMENT_STEP_DETAIL =
            "com.abronia.android.probaker.fragments.StepDetailsFragment";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_recipe_detail);
        ButterKnife.bind(this);

        if(savedInstanceState != null){

            recipeId = savedInstanceState.getInt(ARGS_RECIPE_ID);
            recipeName = savedInstanceState.getString(ARGS_RECIPE_NAME);

        }else{
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                recipeId = bundle.getInt(ARGS_RECIPE_ID);
                recipeName = bundle.getString(ARGS_RECIPE_NAME);
            }
        }

        setTitle(recipeName+" Recipe");

        if(findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;

            FragmentManager fragmentManager = getSupportFragmentManager();

            Fragment stepFragment = StepFragment.newInstance(recipeId,recipeName,mTwoPane);
            fragmentManager
                .beginTransaction()
                .add(R.id.step_list_fragment, stepFragment, FRAGMENT_STEPS_LIST)
                .commit();

            Fragment ingredientFragment = IngredientFragment.newInstance(recipeId, mTwoPane);
            fragmentManager
                .beginTransaction()
                .add(R.id.recipe_detail_container, ingredientFragment, FRAGMENT_INGREDIENTS_LIST)
                .commit();

        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;

                Fragment stepFragment = StepFragment.newInstance(recipeId,recipeName,mTwoPane);
                getSupportFragmentManager() //
                        .beginTransaction() //
                        .replace(R.id.step_list_fragment, stepFragment, FRAGMENT_STEPS_LIST) //
                        .commit();

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARGS_RECIPE_ID, recipeId);
        outState.putString(ARGS_RECIPE_NAME, recipeName);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        recipeId = savedInstanceState.getInt(ARGS_RECIPE_ID);
        recipeName = savedInstanceState.getString(ARGS_RECIPE_NAME);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onListFragmentInteraction(Step step) {

        if(mTwoPane){
            Fragment f = StepDetailsFragment.newInstance(step,mTwoPane);
            getSupportFragmentManager() //
                    .beginTransaction() //
                    .replace(R.id.recipe_detail_container, f, FRAGMENT_STEP_DETAIL) //
                    .commit();

        }else{
            Intent intent = new Intent(RecipeDetailActivity.this, StepDetailActivity.class);
            intent.putExtra(context.getString(R.string.step_package_name),step);
            startActivity(intent);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Ingredient ingredient) {

    }
}
