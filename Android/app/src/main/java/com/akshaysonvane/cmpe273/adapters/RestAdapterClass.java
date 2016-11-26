package com.akshaysonvane.cmpe273.adapters;

import com.akshaysonvane.cmpe273.api.ConnectionApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.akshaysonvane.cmpe273.utils.Utils.BASE_URL;

/**
 * Created by Akshay on 11/20/2016.
 */

public class RestAdapterClass
{


    public ConnectionApi getApiClassObject()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ConnectionApi.class);
    }


}
