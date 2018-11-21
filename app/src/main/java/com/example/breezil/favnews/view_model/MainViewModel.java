package com.example.breezil.favnews.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.breezil.favnews.model.Articles;
import com.example.breezil.favnews.repository.EverythingRepository;
import com.example.breezil.favnews.repository.HeadlinesRepository;
import com.example.breezil.favnews.utils.helpers.Resource;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends AndroidViewModel
{

    private LiveData<List<Articles>> articlesList;
    private HeadlinesRepository headlinesRepository;
    private EverythingRepository everythingRepository;


    @Inject
    public MainViewModel(HeadlinesRepository headlinesRepository,EverythingRepository everythingRepository, Application application){
        super(application);
        this.headlinesRepository = headlinesRepository;
        this.everythingRepository = everythingRepository;
    }

    public LiveData<List<Articles>> getHeadlines(String country, String sources, String category, String query, String apikey, int page){
        if(articlesList == null){
            articlesList = headlinesRepository.getHeadlines(country,sources,category,query,apikey, page);
        }
        return articlesList;
    }

    public LiveData<List<Articles>> getEverything(String query,String sources,
                                                  String domains, String sortBy,
                                                  String from, String to,
                                                  String language,String apiKey){
        if(articlesList == null){
            articlesList = everythingRepository.getEverything(query,sources,domains,sortBy,from,to,language,apiKey);
        }
        return articlesList;
    }







}
