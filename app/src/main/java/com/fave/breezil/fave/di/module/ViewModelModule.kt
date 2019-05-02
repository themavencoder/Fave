package com.fave.breezil.fave.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import com.fave.breezil.fave.ui.main.viewmodel.BookMarkViewModel
import com.fave.breezil.fave.ui.main.viewmodel.HeadlineViewModel
import com.fave.breezil.fave.ui.main.viewmodel.PreferredViewModel
import com.fave.breezil.fave.view_model.ViewModelFactory

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HeadlineViewModel::class)
    internal abstract fun bindHeadlineViewModel(headlineViewModel: HeadlineViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PreferredViewModel::class)
    internal abstract fun bindPreferredViewModel(preferredViewModel: PreferredViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(BookMarkViewModel::class)
    internal abstract fun bindBookMarkViewModel(bookMarkViewModel: BookMarkViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}