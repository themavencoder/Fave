package com.example.breezil.favnews.di.module;

import com.example.breezil.favnews.ui.SettingsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class SettingsActivityModule {
    @ContributesAndroidInjector(modules = SettingsFragmentsModule.class)
    abstract SettingsActivity contributeSettingsActivity();
}