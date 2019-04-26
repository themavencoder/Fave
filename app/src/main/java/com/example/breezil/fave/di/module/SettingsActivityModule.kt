package com.example.breezil.fave.di.module

import com.example.breezil.fave.ui.preference.SettingsActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class SettingsActivityModule {
    @ContributesAndroidInjector(modules = [SettingsFragmentsModule::class])
    internal abstract fun contributeSettingsActivity(): SettingsActivity
}