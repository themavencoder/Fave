package com.example.breezil.favnews.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.breezil.favnews.model.Articles;
import com.example.breezil.favnews.repository.EverythingRepository;
import com.example.breezil.favnews.repository.HeadlinesRepository;

import javax.inject.Inject;

public class DetailViewModel extends AndroidViewModel {

    HeadlinesRepository headlinesRepository;
    EverythingRepository everythingRepository;

    private MutableLiveData<Articles> articles = new MutableLiveData<>();

    @Inject
    public DetailViewModel(HeadlinesRepository headlinesRepository,
                           EverythingRepository everythingRepository,
                           @NonNull Application application) {
        super(application);
        this.headlinesRepository = headlinesRepository;
        this.everythingRepository = everythingRepository;
    }

    public void setArticle(Articles article){
        this.articles.setValue(article);
    }
    public MutableLiveData<Articles> getArticles(){
        return articles;
    }
}
