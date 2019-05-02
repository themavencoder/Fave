package com.fave.breezil.fave.ui.main.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

import com.fave.breezil.fave.db.AppDatabase
import com.fave.breezil.fave.model.BookMark
import com.fave.breezil.fave.repository.BookMarkRepository

import javax.inject.Inject

class BookMarkViewModel @Inject
constructor(application: Application) : AndroidViewModel(application) {
    private val bookMarkRepository: BookMarkRepository = BookMarkRepository(application)
    val bookmarkList: LiveData<List<BookMark>>
    private val appDatabase: AppDatabase
    private var bookMark: LiveData<BookMark>? = null

    init {

        bookmarkList = bookMarkRepository.allBookMarks

        appDatabase = AppDatabase.getAppDatabase(this.getApplication())
    }

    fun insert(bookMark: BookMark) {
        bookMarkRepository.insert(bookMark)
    }

    fun delete(bookMark: BookMark) {
        bookMarkRepository.delete(bookMark)
    }

    fun deleteAll() {
        bookMarkRepository.deleteAllBookMark()
    }

    fun getBookMarkById(bookMarkId: Int): LiveData<BookMark> {
        bookMark = appDatabase.bookMarkDao().getMovieById(bookMarkId)
        return bookMark as LiveData<BookMark>
    }


}
