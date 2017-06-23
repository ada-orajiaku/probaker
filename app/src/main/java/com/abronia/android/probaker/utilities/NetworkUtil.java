package com.abronia.android.probaker.utilities;

import android.content.Context;

import com.abronia.android.probaker.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by adaobifrank on 6/15/17.
 */

public class NetworkUtil {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context){
        if(retrofit == null){
            retrofit = new  Retrofit.Builder()
                    .baseUrl(context.getString(R.string.api_base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
