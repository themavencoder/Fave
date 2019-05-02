package com.fave.breezil.fave.di.module

import com.fave.breezil.fave.ui.preference.SettingsActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class SettingsActivityModule {
    @ContributesAndroidInjector(modules = [SettingsFragmentsModule::class])
    internal abstract fun contributeSettingsActivity(): SettingsActivity
}