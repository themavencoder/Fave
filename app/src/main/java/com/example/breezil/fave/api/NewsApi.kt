package com.example.breezil.fave.api

import com.example.breezil.fave.model.ArticleResult

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines")
    fun getHeadline(@Query("country") country: String?,
                    @Query("sources") sources: String?,
                    @Query("category") category: String?,
                    @Query("q") query: String?,
                    @Query("pageSize") pageSize: Int,
                    @Query("page") page: Int,
                    @Query("apiKey") apiKey: String): Single<ArticleResult>


    @GET("/v2/everything")
    fun getEverythings(@Query("q") query: String,
                       @Query("sources") sources: String,
                       @Query("sortBy") sortBy: String?,
                       @Query("from") from: String?,
                       @Query("to") to: String?,
                       @Query("language") language: String?,
                       @Query("pageSize") pageSize: Int,
                       @Query("page") page: Int,
                       @Query("apiKey") apiKey: String): Single<ArticleResult>

}
