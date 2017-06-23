package com.abronia.android.probaker;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.abronia.android.probaker.models.Recipe;
import com.abronia.android.probaker.provider.RecipeColumns;
import com.abronia.android.probaker.provider.RecipeProvider;
import com.abronia.android.probaker.utilities.DataUtil;
import com.abronia.android.probaker.utilities.NetworkUtil;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final Context context = this;

    @BindView(R.id.recipies_recycler_view)
    RecyclerView recipesRecyclerView;

    RecipeAdapter mAdapter;
    ProgressDialog progressDialog;
    List<Recipe> recipes = new ArrayList<>();

    private static final int LOADER_RECIPES = 10;

    private LoaderManager loaderManager;

    static final String[] RECIPE_PROJECTION = {
            RecipeColumns.ID,
            RecipeColumns.NAME,
            RecipeColumns.INGREDIENTS,
            RecipeColumns.STEPS,
            RecipeColumns.SERVINGS,
            RecipeColumns.IMAGE
    };

//    private OnRecipeSelectedListener listener;
//    private interface OnRecipeSelectedListener {
//        void onRecipeSelectedListener(long recipeId);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipesRecyclerView.setHasFixedSize(true);

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
        return new CursorLoader(this, RecipeProvider.Recipes.CONTENT_URI, RECIPE_PROJECTION, null, null, null);
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
                public void onRecipeSelected(long recipeId) {

                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(context.getString(R.string.package_name), recipeId);
                    startActivity(intent);
                }
            });
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
            recipeContentValues.put(RecipeColumns.NAME, recipes.get(i).getName());
            recipeContentValues.put(RecipeColumns.SERVINGS, recipes.get(i).getServings());
            recipeContentValues.put(RecipeColumns.IMAGE, recipes.get(i).getImage());
            recipeValues[i] = recipeContentValues;

            ContentValues[] ingredientContentValues = DataUtil.getIngredientsContentValues(recipes.get(i).getIngredients(), recipes.get(i).getId());
            RecipeProvider.Ingredients.onBulkInsert(context,RecipeProvider.Ingredients.CONTENT_URI, ingredientContentValues, null);

            ContentValues[] stepContentValues = DataUtil.getStepsContentValues(recipes.get(i).getSteps(),recipes.get(i).getId());
            RecipeProvider.Steps.onBulkInsert(context,RecipeProvider.Steps.CONTENT_URI, stepContentValues, null);
        }
        RecipeProvider.Recipes.onBulkInsert(context,RecipeProvider.Recipes.CONTENT_URI,recipeValues);
    }
}