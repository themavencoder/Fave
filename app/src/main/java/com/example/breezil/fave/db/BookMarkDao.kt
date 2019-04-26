package com.example.breezil.fave.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

import com.example.breezil.fave.model.BookMark

@Dao
interface BookMarkDao {

    @get:Query("SELECT * FROM bookmark_table ORDER BY id DESC")
    val allBookMarks: LiveData<List<BookMark>>

    @Insert
    fun insert(bookMark: BookMark)

    @Delete
    fun delete(bookMark: BookMark)

    @Query("DELETE FROM bookmark_table")
    fun deleteAllBookMarks()

    @Query("SELECT * FROM bookmark_table WHERE id = :bookMarkId")
    fun getMovieById(bookMarkId: Int): LiveData<BookMark>


}
