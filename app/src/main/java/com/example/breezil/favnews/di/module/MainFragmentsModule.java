package com.example.breezil.favnews.di.module;

import com.example.breezil.favnews.ui.BookMarksFragment;
import com.example.breezil.favnews.ui.HeadlinesFragment;
import com.example.breezil.favnews.ui.PreferredFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentsModule {
    @ContributesAndroidInjector
    abstract PreferredFragment contributePreferredFragment();

    @ContributesAndroidInjector
    abstract HeadlinesFragment contributeHeadlinesFragment();

    @ContributesAndroidInjector
    abstract BookMarksFragment contributeBookMarksFragment();
}
