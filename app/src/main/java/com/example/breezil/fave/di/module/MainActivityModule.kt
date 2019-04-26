package com.example.breezil.fave.di.module

import com.example.breezil.fave.ui.main.MainActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [MainFragmentsModule::class])
    internal abstract fun contributeMainActivity(): MainActivity
}