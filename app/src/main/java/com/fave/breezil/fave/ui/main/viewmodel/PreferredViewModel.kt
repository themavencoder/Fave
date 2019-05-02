package com.fave.breezil.fave.ui.main.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations.switchMap
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList

import javax.inject.Inject

import com.fave.breezil.fave.model.Articles
import com.fave.breezil.fave.repository.NetworkState
import com.fave.breezil.fave.repository.everything.EverythingDataSource
import com.fave.breezil.fave.repository.everything.EverythingDataSourceFactory
import com.fave.breezil.fave.utils.Constant.Companion.TEN
import com.fave.breezil.fave.utils.helpers.AppExecutors
import io.reactivex.disposables.CompositeDisposable

class PreferredViewModel @Inject
constructor(private val everythingDataSourceFactory: EverythingDataSourceFactory,
            private val appsExecutor: AppExecutors, application: Application)
    : AndroidViewModel(application) {
    var articlesList: LiveData<PagedList<Articles>>


    private val networkState: LiveData<NetworkState> = switchMap(everythingDataSourceFactory.everythingDataSourceMutableLiveData,
            EverythingDataSource::mNetworkState)

    private val compositeDisposable = CompositeDisposable()

    init {


        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(TEN)
                .setPrefetchDistance(TEN)
                .setPageSize(TEN)
                .build()

        articlesList = LivePagedListBuilder(everythingDataSourceFactory, config)
                .setFetchExecutor(appsExecutor.networkIO())
                .build()
    }

    fun setParameter(query: String, sources: String, sortBy: String, from: String,
                     to: String, language: String) {
        everythingDataSourceFactory.everythingDataSource.query = query
        everythingDataSourceFactory.everythingDataSource.sources = sources
        everythingDataSourceFactory.everythingDataSource.sortBy = sortBy
        everythingDataSourceFactory.everythingDataSource.from = from
        everythingDataSourceFactory.everythingDataSource.to = to
        everythingDataSourceFactory.everythingDataSource.language = language
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return networkState
    }

    fun refreshArticle(): LiveData<PagedList<Articles>> {
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(TEN)
                .build()

        articlesList = LivePagedListBuilder(everythingDataSourceFactory, config)
                .setFetchExecutor(appsExecutor.networkIO())
                .build()

        return articlesList
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
