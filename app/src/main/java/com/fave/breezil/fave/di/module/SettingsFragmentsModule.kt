package com.fave.breezil.fave.di.module

import com.fave.breezil.fave.ui.preference.PrefFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsFragmentsModule {
    @ContributesAndroidInjector
    internal abstract fun contributePrefFragment(): PrefFragment

}
