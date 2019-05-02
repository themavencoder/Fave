package com.fave.breezil.fave.ui.main.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList

import javax.inject.Inject

import com.fave.breezil.fave.db.AppDatabase
import com.fave.breezil.fave.model.Articles
import com.fave.breezil.fave.repository.ArticleDbRepository
import com.fave.breezil.fave.repository.NetworkState
import com.fave.breezil.fave.repository.headlines.HeadlineDataSource
import com.fave.breezil.fave.repository.headlines.HeadlineDataSourceFactory
import com.fave.breezil.fave.utils.Constant.Companion.FIVE
import com.fave.breezil.fave.utils.Constant.Companion.TEN
import com.fave.breezil.fave.utils.helpers.AppExecutors

class HeadlineViewModel @Inject
constructor(private val headlineDataSourceFactory: HeadlineDataSourceFactory,
            private val appsExecutor: AppExecutors, application: Application) : AndroidViewModel(application) {
    var articlesList: LiveData<PagedList<Articles>>

    lateinit var articlesDBList: LiveData<PagedList<Articles>>
    private val networkState: LiveData<NetworkState> = Transformations.switchMap(headlineDataSourceFactory.
            headlineDataSourceMutableLiveData, HeadlineDataSource::mNetworkState)

    private val mainDbRepository: ArticleDbRepository = ArticleDbRepository(application)
    private val appDatabase: AppDatabase = AppDatabase.getAppDatabase(this.getApplication())

    val fromDbList: LiveData<PagedList<Articles>>
        get() {
            val factory = appDatabase.articleDao().pagedArticle()
            val livePagedListBuilder = LivePagedListBuilder(factory, FIVE)
            articlesDBList = livePagedListBuilder.build()
            return articlesDBList
        }

    init {


        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(TEN)
                .setPrefetchDistance(TEN)
                .setPageSize(TEN)
                .build()

        articlesList = LivePagedListBuilder(headlineDataSourceFactory, config)
                .setFetchExecutor(appsExecutor.networkIO())
                .build()
    }

    fun setParameter(country: String, sources: String, category: String, query: String) {
        headlineDataSourceFactory.headlineDataSource.country = country
        headlineDataSourceFactory.headlineDataSource.sources = sources
        headlineDataSourceFactory.headlineDataSource.category = category
        headlineDataSourceFactory.headlineDataSource.query = query
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return networkState
    }

    fun refreshArticle(): LiveData<PagedList<Articles>> {
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(TEN)
                .build()

        articlesList = LivePagedListBuilder(headlineDataSourceFactory, config)
                .setFetchExecutor(appsExecutor.networkIO())
                .build()

        return articlesList
    }


    fun deleteAllInDb() {
        mainDbRepository.deleteAllArticles()
    }


}
