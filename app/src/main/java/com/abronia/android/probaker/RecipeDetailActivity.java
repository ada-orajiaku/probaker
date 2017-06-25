package com.abronia.android.probaker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.abronia.android.probaker.adapters.MyStepRecyclerViewAdapter;
import com.abronia.android.probaker.data.models.Ingredient;
import com.abronia.android.probaker.data.models.Step;
import com.abronia.android.probaker.fragments.IngredientFragment;
import com.abronia.android.probaker.fragments.StepDetailsFragment;
import com.abronia.android.probaker.fragments.StepFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class RecipeDetailActivity extends AppCompatActivity
 implements StepFragment.OnListFragmentInteractionListener,
        IngredientFragment.OnListFragmentInteractionListener,
        StepDetailsFragment.OnFragmentInteractionListener {

    private final Context context = this;

    private Uri uri;
    private int recipeId;

    private static final String FRAGMENT_STEPS_LIST =
            "com.abronia.android.probaker.RecipeDetailActivity.STEPS";
    private static final String FRAGMENT_INGREDIENTS_LIST =
            "com.abronia.android.probaker.RecipeDetailActivity.INGREDIENTS";
    private static final String FRAGMENT_STEP_DETAIL =
            "com.abronia.android.probaker.RecipeDetailActivity.STEP_DETAIL";

    private boolean mTwoPane;

    @OnClick(R.id.ingredientButton)
    public void viewIngredients() {
        if(mTwoPane){
            Fragment ingredientFragment = IngredientFragment.newInstance(recipeId);
            getSupportFragmentManager() //
                    .beginTransaction() //
                    .replace(R.id.recipe_detail_container, ingredientFragment, FRAGMENT_INGREDIENTS_LIST) //
                    .commit();
        }else{
            Intent intent = new Intent(this, IngredientListActivity.class);
            intent.putExtra(IngredientListActivity.ARG_RECIPE_ID,recipeId);
            startActivity(intent);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_recipe_detail);
        ButterKnife.bind(this);

        //setSupportActionBar(toolbar);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        recipeId = bundle.getInt(context.getString(R.string.package_name));

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Fragment stepFragment = StepFragment.newInstance(recipeId,mTwoPane);
            getSupportFragmentManager() //
                    .beginTransaction() //
                    .replace(android.R.id.content, stepFragment, FRAGMENT_STEPS_LIST) //
                    .commit();
        }

        if(findViewById(R.id.step_detail_linear_layout) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;

            Fragment ingredientFragment = IngredientFragment.newInstance(recipeId);
            getSupportFragmentManager() //
                    .beginTransaction() //
                    .replace(R.id.recipe_detail_container, ingredientFragment, FRAGMENT_INGREDIENTS_LIST) //
                    .commit();

        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
        }



    }

    @Override
    public void onListFragmentInteraction(Step step) {

        if(mTwoPane){

            Fragment f = StepDetailsFragment.newInstance(step);
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
