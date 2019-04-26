package com.example.breezil.fave.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

import com.example.breezil.fave.db.AppDatabase
import com.example.breezil.fave.db.BookMarkDao
import com.example.breezil.fave.model.BookMark

class BookMarkRepository(application: Application) {
    private val bookMarkDao: BookMarkDao
    val allBookMarks: LiveData<List<BookMark>>

    init {
        val database = AppDatabase.getAppDatabase(application)
        bookMarkDao = database.bookMarkDao()
        allBookMarks = bookMarkDao.allBookMarks


    }

    fun insert(bookMark: BookMark) {
        InsertBookMark(bookMarkDao).execute(bookMark)
    }

    fun delete(bookMark: BookMark) {
        DeleteBookMark(bookMarkDao).execute(bookMark)
    }

    fun deleteAllBookMark() {
        DeleteAllBookMarks(bookMarkDao).execute()
    }

    private class InsertBookMark(private val bookMarkDao: BookMarkDao) : AsyncTask<BookMark, Void, Void>() {

        override fun doInBackground(vararg bookMarks: BookMark): Void? {
            bookMarkDao.insert(bookMarks[0])
            return null
        }
    }


    private class DeleteBookMark(private val bookMarkDao: BookMarkDao) : AsyncTask<BookMark, Void, Void>() {

        override fun doInBackground(vararg bookMarks: BookMark): Void? {
            bookMarkDao.delete(bookMarks[0])
            return null
        }
    }

    private class DeleteAllBookMarks(private val bookMarkDao: BookMarkDao) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            bookMarkDao.deleteAllBookMarks()
            return null
        }
    }

}
