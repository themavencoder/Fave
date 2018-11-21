package com.example.breezil.favnews.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.breezil.favnews.db.AppDatabase;
import com.example.breezil.favnews.model.BookMark;
import com.example.breezil.favnews.repository.BookMarkRepository;

import java.util.List;

import javax.inject.Inject;

public class BookMarkViewModel extends AndroidViewModel{
    private BookMarkRepository bookMarkRepository;
    private LiveData<List<BookMark>> bookmarkList;
    private AppDatabase appDatabase;
    private LiveData<BookMark> bookMark;

    @Inject
    public BookMarkViewModel(@NonNull Application application) {
        super(application);

        bookMarkRepository = new BookMarkRepository(application);
        bookmarkList = bookMarkRepository.getAllBookMarks();

        appDatabase = AppDatabase.getAppDatabase(this.getApplication());
    }
    public void insert(BookMark bookMark){
        bookMarkRepository.insert(bookMark);
    }
    public void delete(BookMark bookMark){
        bookMarkRepository.delete(bookMark);
    }
    public void deleteAll(){
        bookMarkRepository.deleteAllBookMark();
    }

    public LiveData<List<BookMark>> getBookmarkList() {
        return bookmarkList;
    }

    public  LiveData<BookMark>  getBookMarkById(int bookMarkId) {
        bookMark = appDatabase.bookMarkDao().getMovieById(bookMarkId);
        return bookMark;
    }


}
