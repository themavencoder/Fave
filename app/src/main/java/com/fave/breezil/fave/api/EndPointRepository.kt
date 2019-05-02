package com.fave.breezil.fave.api



import javax.inject.Inject

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import com.fave.breezil.fave.model.ArticleResult
import com.fave.breezil.fave.utils.Constant.Companion.API_KEY

class EndPointRepository @Inject
constructor(private val newsApi: NewsApi) {
    fun getHeadline(country: String, sources: String, category: String, query: String,
                    pageSize: Int, page: Int): Single<ArticleResult> {
        return newsApi.getHeadline(country, sources, category, query, pageSize, page, API_KEY)
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun getEverything(query: String, sources: String, sortBy: String, from: String,
                      to: String, language: String, pageSize: Int, page: Int): Single<ArticleResult> {
        return newsApi.getEverything(query, sources, sortBy, from, to, language, pageSize, page, API_KEY)
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }


}
