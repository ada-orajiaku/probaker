package com.abronia.android.probaker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.abronia.android.probaker.data.models.Ingredient;
import com.abronia.android.probaker.fragments.IngredientFragment;

import java.util.List;

public class IngredientListActivity extends AppCompatActivity
        implements IngredientFragment.OnListFragmentInteractionListener {

    int recipeId;
    static final String  FRAGMENT_INGREDIENT_LIST =
            "com.abronia.android.probaker.IngredientListActivity.INGREDIENT_LIST";
    public static final String ARG_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        recipeId = bundle.getInt(ARG_RECIPE_ID);

        Fragment f = IngredientFragment.newInstance(recipeId,false);
        getSupportFragmentManager() //
                .beginTransaction() //
                .add(R.id.ingredient_list_fragment, f, FRAGMENT_INGREDIENT_LIST) //
                .commit();
    }

    @Override
    public void onListFragmentInteraction(Ingredient ingredient) {

    }
}
