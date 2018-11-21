package com.example.breezil.favnews.di.module;

import com.example.breezil.favnews.ui.SearchActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class SearchActivityModule {
    @ContributesAndroidInjector()
    abstract SearchActivity contributeSearchActivity();
}