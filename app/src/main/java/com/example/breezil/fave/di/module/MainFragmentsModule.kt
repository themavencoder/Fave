package com.example.breezil.fave.di.module

import com.example.breezil.fave.ui.main.fragment.BookMarksFragment
import com.example.breezil.fave.ui.main.fragment.HeadlinesFragment
import com.example.breezil.fave.ui.main.fragment.PreferredFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsModule {
    @ContributesAndroidInjector
    internal abstract fun contributePreferredFragment(): PreferredFragment

    @ContributesAndroidInjector
    internal abstract fun contributeHeadlinesFragment(): HeadlinesFragment

    @ContributesAndroidInjector
    internal abstract fun contributeBookMarksFragment(): BookMarksFragment
}
