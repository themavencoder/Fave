package com.example.breezil.fave.db


import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

import com.example.breezil.fave.model.Articles
import com.example.breezil.fave.model.BookMark

@Database(entities = [BookMark::class, Articles::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookMarkDao(): BookMarkDao
    abstract fun articleDao(): ArticleDao

    companion object {
        private var appDatabase: AppDatabase? = null

        @Synchronized
        fun getAppDatabase(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "article_db")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return appDatabase as AppDatabase
        }
    }
}
