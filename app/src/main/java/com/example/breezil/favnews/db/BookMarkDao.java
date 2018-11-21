package com.example.breezil.favnews.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.breezil.favnews.model.BookMark;

import java.util.List;

@Dao
public interface BookMarkDao {
    @Insert
    void insert(BookMark bookMark);

    @Delete
    void delete(BookMark bookMark);

    @Query("DELETE FROM bookmark_table")
    void deleteAllBookMarks();

    @Query("SELECT * FROM bookmark_table ORDER BY id DESC")
    LiveData<List<BookMark>> getAllBookMarks();

    @Query("SELECT * FROM bookmark_table WHERE id = :bookMarkId")
    LiveData<BookMark> getMovieById(int bookMarkId);


}
