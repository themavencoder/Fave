package com.example.breezil.favnews.di.module;

import com.example.breezil.favnews.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = MainFragmentsModule.class)
    abstract MainActivity contributeMainActivity();
}