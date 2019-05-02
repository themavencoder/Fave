package com.fave.breezil.fave.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

import com.fave.breezil.fave.db.AppDatabase
import com.fave.breezil.fave.db.ArticleDao
import com.fave.breezil.fave.model.Articles

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleDbRepository @Inject
constructor(application: Application) {
    private val articleDao: ArticleDao

    init {
        val database = AppDatabase.getAppDatabase(application)
        articleDao = database.articleDao()
    }

    fun insert(articles: Articles) {
        InsertArticles(articleDao).execute(articles)
    }

    fun deleteAllArticles() {
        DeleteAllArticle(articleDao).execute()
    }

    private class InsertArticles(private val articleDao: ArticleDao) : AsyncTask<Articles, Void, Void>() {

        override fun doInBackground(vararg articles: Articles): Void? {
            articleDao.insert(articles[0])
            return null
        }
    }

    private class DeleteAllArticle(private val articleDao: ArticleDao) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            articleDao.deleteAllArticle()
            return null
        }
    }

}
