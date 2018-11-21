package com.example.breezil.favnews.di.module;



import com.example.breezil.favnews.ui.BookMarkDetailsFragment;
import com.example.breezil.favnews.ui.ArticleDetailFragment;
import com.example.breezil.favnews.ui.TabletListFragment;
import com.example.breezil.favnews.ui.TabletSearchFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class DetailFragmentModule {
    @ContributesAndroidInjector
    abstract TabletListFragment contributeTabletListFragment();

    @ContributesAndroidInjector
    abstract ArticleDetailFragment contributeTabletDetailFragment();

    @ContributesAndroidInjector
    abstract TabletSearchFragment contributeTabletSearchFragment();

    @ContributesAndroidInjector
    abstract BookMarkDetailsFragment contributeBookMarkDetailsFragment();


}
