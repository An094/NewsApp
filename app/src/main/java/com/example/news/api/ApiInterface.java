package com.example.news.api;

import com.example.news.models.News;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
//    @GET("everything")
//    Call<News> getNews();
//link API: http://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=1b53e9b5926f4dee8ca683db4433eeeb
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    ApiInterface apiService = new Retrofit.Builder()
            .baseUrl("http://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiInterface.class);

    @GET("v2/top-headlines")
    Call<News> callApi(@Query("country") String country,
                                   @Query("category") String category,
                                   @Query("apiKey") String apiKey);
}
