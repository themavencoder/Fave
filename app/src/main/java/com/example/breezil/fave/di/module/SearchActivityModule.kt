package com.example.breezil.fave.di.module

import com.example.breezil.fave.ui.explore.SearchActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class SearchActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeSearchActivity(): SearchActivity
}