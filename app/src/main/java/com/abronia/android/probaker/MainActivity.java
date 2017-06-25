package com.abronia.android.probaker;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.abronia.android.probaker.adapters.RecipeAdapter;
import com.abronia.android.probaker.api.ApiInterface;
import com.abronia.android.probaker.data.models.Recipe;
import com.abronia.android.probaker.data.provider.ProBakerDbContract;
import com.abronia.android.probaker.utilities.DataUtil;
import com.abronia.android.probaker.utilities.NetworkUtil;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final Context context = this;

    RecyclerView recipesRecyclerView;
    RecyclerView recipesGridRecyclerView;

    RecipeAdapter mAdapter;
    ProgressDialog progressDialog;
    List<Recipe> recipes = new ArrayList<>();

    private static final int LOADER_RECIPES = 10;

    private LoaderManager loaderManager;

    static final String[] RECIPE_PROJECTION = {
            ProBakerDbContract.RecipeEntry._ID,
            ProBakerDbContract.RecipeEntry.RECIPE_ID,
            ProBakerDbContract.RecipeEntry.NAME,
            ProBakerDbContract.RecipeEntry.SERVINGS,
            ProBakerDbContract.RecipeEntry.IMAGE
    };

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recipesRecyclerView = (RecyclerView) findViewById(R.id.recipes_recycler_view);
        recipesGridRecyclerView = (RecyclerView) findViewById(R.id.recipes_grid_recycler_view);

        if(recipesGridRecyclerView != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;

            recipesGridRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
            recipesGridRecyclerView.setHasFixedSize(true);


        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;

            recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            recipesRecyclerView.setHasFixedSize(true);
        }



        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait....");

        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(LOADER_RECIPES,null,this);

    }

    private void fetchRecipes(){

        progressDialog.show();

        ApiInterface service = NetworkUtil.getClient(context).create(ApiInterface.class);
        Call<List<Recipe>> call = service.getTopRatedMovies();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                progressDialog.dismiss();
                recipes.clear();
                recipes = response.body();

                saveRecipes(recipes);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,R.string.request_error_text,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ProBakerDbContract.RecipeEntry.CONTENT_URI, RECIPE_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.getCount() == 0)
        {
            fetchRecipes();
        }
        else if (mAdapter == null) {

            mAdapter = new RecipeAdapter(this, data);
            mAdapter.setOnItemClickListener(new RecipeAdapter.OnRecipeSelectedListener() {
                @Override
                public void onRecipeSelected(int recipeId) {

                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(context.getString(R.string.package_name), recipeId);
                    startActivity(intent);
                }
            });

            if(mTwoPane)
                recipesGridRecyclerView.setAdapter(mAdapter);
            else
                recipesRecyclerView.setAdapter(mAdapter);
        }
         else {
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        if(mAdapter != null)
            mAdapter.swapCursor(null);
    }

    private void saveRecipes(List<Recipe> recipes)
    {
        ContentValues[] recipeValues = new ContentValues[recipes.size()];
        for (int i = 0; i < recipes.size(); i++) {
            ContentValues recipeContentValues = new ContentValues();
            recipeContentValues.put(ProBakerDbContract.RecipeEntry.RECIPE_ID, recipes.get(i).getId());
            recipeContentValues.put(ProBakerDbContract.RecipeEntry.NAME, recipes.get(i).getName());
            recipeContentValues.put(ProBakerDbContract.RecipeEntry.SERVINGS, recipes.get(i).getServings());
            recipeContentValues.put(ProBakerDbContract.RecipeEntry.IMAGE, recipes.get(i).getImage());
            recipeValues[i] = recipeContentValues;

            Uri uriResult = getContentResolver().insert(ProBakerDbContract.RecipeEntry.CONTENT_URI, recipeContentValues);

            if (uriResult != null) {
                ContentValues[] ingredientContentValues = DataUtil.getIngredientsContentValues(recipes.get(i).getIngredients(), recipes.get(i).getId());
                ContentValues[] stepContentValues = DataUtil.getStepsContentValues(recipes.get(i).getSteps(),recipes.get(i).getId());
                getContentResolver().bulkInsert(ProBakerDbContract.IngredientEntry.CONTENT_URI,ingredientContentValues);
                getContentResolver().bulkInsert(ProBakerDbContract.StepEntry.CONTENT_URI,stepContentValues);
            }

        }
    }
}