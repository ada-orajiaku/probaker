package com.abronia.android.probaker.api;

import com.abronia.android.probaker.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by adaobifrank on 6/15/17.
 */

public interface ApiInterface {
    @GET("baking.json")
    Call<List<Recipe>> getTopRatedMovies();
}
