package com.example.breezil.favnews.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.breezil.favnews.view_model.BookMarkViewModel;
import com.example.breezil.favnews.view_model.DetailViewModel;
import com.example.breezil.favnews.view_model.MainViewModel;
import com.example.breezil.favnews.view_model.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BookMarkViewModel.class)
    abstract ViewModel bindBookMarkViewModel(BookMarkViewModel bookMarkViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel.class)
    abstract ViewModel bindDetailViewModel(DetailViewModel detailViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}