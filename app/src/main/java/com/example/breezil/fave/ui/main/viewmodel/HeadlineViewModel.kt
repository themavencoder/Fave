package com.example.breezil.fave.ui.main.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList

import com.example.breezil.fave.db.AppDatabase
import com.example.breezil.fave.model.Articles
import com.example.breezil.fave.repository.ArticleDbRepository
import com.example.breezil.fave.repository.NetworkState
import com.example.breezil.fave.repository.headlines.HeadlineDataSource
import com.example.breezil.fave.repository.headlines.HeadlineDataSourceFactory
import com.example.breezil.fave.utils.helpers.AppExecutors

import javax.inject.Inject

import com.example.breezil.fave.utils.Constant.Companion.FIVE
import com.example.breezil.fave.utils.Constant.Companion.TEN

class HeadlineViewModel @Inject
constructor(private val headlineDataSourceFactory: HeadlineDataSourceFactory,
                     private val appsExecutor: AppExecutors, application: Application) : AndroidViewModel(application) {
    var articlesList: LiveData<PagedList<Articles>>

    lateinit var articlesDBList: LiveData<PagedList<Articles>>
    private val networkState: LiveData<NetworkState>

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

        networkState = Transformations.switchMap(headlineDataSourceFactory.
                headlineDataSourceMutableLiveData,HeadlineDataSource::mNetworkState)


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
