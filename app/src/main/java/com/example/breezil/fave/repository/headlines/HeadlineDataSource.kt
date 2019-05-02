package com.example.breezil.fave.repository.headlines

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource

import com.example.breezil.fave.api.EndPointRepository
import com.example.breezil.fave.db.AppDatabase
import com.example.breezil.fave.db.ArticleDao
import com.example.breezil.fave.model.ArticleResult
import com.example.breezil.fave.model.Articles
import com.example.breezil.fave.repository.ArticleDbRepository
import com.example.breezil.fave.repository.NetworkState
import com.example.breezil.fave.repository.PaginationListener

import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

import com.example.breezil.fave.utils.Constant.Companion.ONE
import com.example.breezil.fave.utils.Constant.Companion.TEN
import com.example.breezil.fave.utils.Constant.Companion.TWO
import com.example.breezil.fave.utils.Constant.Companion.ZERO

@Singleton
class HeadlineDataSource @Inject
constructor(private var endpointRepository: EndPointRepository,
            private val articleDbRepository: ArticleDbRepository,
            private val compositeDisposable: CompositeDisposable,
            application: Application) : PageKeyedDataSource<Int, Articles>(),
        PaginationListener<ArticleResult, Articles> {

    lateinit var country: String
    lateinit var sources: String
    lateinit var category: String
    lateinit  var query: String
    val mNetworkState = MutableLiveData<NetworkState>()
    val mInitialLoading =  MutableLiveData<NetworkState>()
    private var articleDao: ArticleDao


    init {
        val database = AppDatabase.getAppDatabase(application)
        articleDao = database.articleDao()

    }

    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>, callback: PageKeyedDataSource.LoadInitialCallback<Int, Articles>) {

        mNetworkState.postValue(NetworkState.LOADING)
        mInitialLoading.postValue(NetworkState.LOADING)

        val articlesList = ArrayList<Articles>()
        val articles = endpointRepository.getHeadline(country, sources, category, query,
                TEN, ONE).subscribe({ articleResult ->
            onInitialSuccess(articleResult,
                    callback, articlesList)
            for (article in articleResult.articles) {
                articleDbRepository.insert(article)
            }
        }, { throwable -> onInitialError(throwable) })
        compositeDisposable.add(articles)
    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, Articles>) {

    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, Articles>) {
        mNetworkState.postValue(NetworkState.LOADING)

        val articlesList = ArrayList<Articles>()
        val articles = endpointRepository.getHeadline(country, sources, category, query,
                TEN, params.key).subscribe({ articleResult -> onPaginationSuccess(articleResult,
                callback, params, articlesList) }, { throwable -> onPaginationError(throwable) })
        compositeDisposable.add(articles)
    }

    override fun onInitialError(throwable: Throwable) {
        mInitialLoading.postValue(NetworkState(NetworkState.Status.FAILED))
        mNetworkState.postValue(NetworkState(NetworkState.Status.FAILED))
        Timber.e(throwable)

    }

    override fun onInitialSuccess(response: ArticleResult,
                                  callback: PageKeyedDataSource.LoadInitialCallback<Int, Articles>,
                                  results: MutableList<Articles>) {
        if (response.articles.size > ZERO) {
            results.addAll(response.articles)
            callback.onResult(results, null, TWO)
            mInitialLoading.postValue(NetworkState.LOADED)
            mNetworkState.postValue(NetworkState.LOADED)

        } else {
            mInitialLoading.postValue(NetworkState(NetworkState.Status.NO_RESULT))
            mNetworkState.postValue(NetworkState(NetworkState.Status.NO_RESULT))
        }

    }

    override fun onPaginationError(throwable: Throwable) {
        mNetworkState.postValue(NetworkState(NetworkState.Status.FAILED))
        Timber.e(throwable)
    }

    override fun onPaginationSuccess(response: ArticleResult,
                                     callback: PageKeyedDataSource.LoadCallback<Int, Articles>,
                                     params: PageKeyedDataSource.LoadParams<Int>,
                                     results: MutableList<Articles>) {
        if (response.articles.size > ZERO) {
            results.addAll(response.articles)

            val key = (if (params.key > ONE) params.key + ONE else null)!!.toInt()
            callback.onResult(results, key)

            mNetworkState.postValue(NetworkState.LOADED)
        } else {
            mNetworkState.postValue(NetworkState(NetworkState.Status.NO_RESULT))
        }
    }

    override fun clear() {
        compositeDisposable.clear()
    }
}
