package com.example.breezil.fave.db

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction

import com.example.breezil.fave.model.Articles


@Dao
interface ArticleDao {

    @Transaction
    @Query("SELECT * FROM article_table ORDER BY id ASC")
    fun pagedArticle(): DataSource.Factory<Int, Articles>

    @Insert
    fun insert(articles: Articles)

    @Query("DELETE FROM article_table")
    fun deleteAllArticle()
}
