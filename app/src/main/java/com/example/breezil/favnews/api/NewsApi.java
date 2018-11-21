package com.example.breezil.favnews.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.breezil.favnews.model.ArticleResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    @GET("/v2/top-headlines")
    Call<ArticleResult> getHeadlines(@Query("country") @Nullable String country,
                                                 @Query("sources") @Nullable String sources,
                                                 @Query("category") @Nullable String category,
                                                 @Query("q") @Nullable String query,
                                                 @Query("page") int page,
                                                 @Query("apiKey") @NonNull String apiKey);


    @GET("/v2/everything")
    Call<ArticleResult> getEverything(@Query("q") @NonNull String query,
                                      @Query("sources") @NonNull String sources,
                                      @Query("domains") @NonNull String domains,
                                      @Query("sortBy") @Nullable String sortBy,
                                      @Query("from") @Nullable String from,
                                      @Query("to") @Nullable String to,
                                      @Query("language") @Nullable String language,
                                      @Query("apiKey") @NonNull String apiKey);


}
