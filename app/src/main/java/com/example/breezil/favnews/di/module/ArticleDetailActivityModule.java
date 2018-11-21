package com.example.breezil.favnews.di.module;


import com.example.breezil.favnews.ui.ArticleDetailActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ArticleDetailActivityModule {
    @ContributesAndroidInjector(modules = DetailFragmentModule.class)
    abstract ArticleDetailActivity contributeArticleDetailActivity();
}
