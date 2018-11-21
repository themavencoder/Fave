package com.example.breezil.favnews.di.module;

import com.example.breezil.favnews.ui.PrefFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SettingsFragmentsModule {
    @ContributesAndroidInjector
    abstract PrefFragment contributePrefFragment();

}
