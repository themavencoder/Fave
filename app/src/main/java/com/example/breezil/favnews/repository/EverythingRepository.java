package com.example.breezil.favnews.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.example.breezil.favnews.api.NewsApi;
import com.example.breezil.favnews.db.AppDatabase;
import com.example.breezil.favnews.model.ArticleResult;
import com.example.breezil.favnews.model.Articles;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EverythingRepository{

    private NewsApi newApi;
    private MediatorLiveData<List<Articles>> articles;

    AppDatabase appDatabase;

    @Inject
    EverythingRepository(NewsApi newsApi, AppDatabase appDatabase){
        this.newApi = newsApi;
        this.appDatabase = appDatabase;
    }

    public LiveData<List<Articles>> getEverything(String query,String sources,
                                                  String domains, String sortBy,
                                                  String from, String to,
                                                  String language, String apiKey){
        if(articles == null ){
            articles = new MediatorLiveData<>();

            newApi.getEverything(query,sources,domains,sortBy,from,to,language,apiKey)
                    .enqueue(new Callback<ArticleResult>() {
                @Override
                public void onResponse(Call<ArticleResult> call, Response<ArticleResult> response) {
                    articles.setValue(response.body().getArticles());
                }

                @Override
                public void onFailure(Call<ArticleResult> call, Throwable t) {

                }
            });
        }
        return articles;
    }



}
