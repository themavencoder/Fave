package com.example.breezil.favnews.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;

import com.example.breezil.favnews.Api.NewsApi;
import com.example.breezil.favnews.db.AppDatabase;
import com.example.breezil.favnews.model.ArticleResult;
import com.example.breezil.favnews.model.Articles;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class HeadlinesRepository {

    private final NewsApi newsApi;
    private MediatorLiveData<List<Articles>> articles;

    AppDatabase appDatabase;




    @Inject
    HeadlinesRepository(NewsApi newsApi) {
        this.newsApi = newsApi;
    }





    public LiveData<List<Articles>> getHeadlines(String country, String sources, String category, String query, String apikey, int page) {
        if (articles == null) {
            articles = new MediatorLiveData<>();


            newsApi.getHeadlines(country,sources,category,query,page,apikey).enqueue(new Callback<ArticleResult>() {
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
