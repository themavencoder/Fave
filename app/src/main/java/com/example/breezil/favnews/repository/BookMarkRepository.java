package com.example.breezil.favnews.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.breezil.favnews.db.AppDatabase;
import com.example.breezil.favnews.db.BookMarkDao;
import com.example.breezil.favnews.model.BookMark;

import java.util.List;

public class BookMarkRepository {
    private BookMarkDao bookMarkDao;
    private LiveData<List<BookMark>> allBookMarks;

    public BookMarkRepository(Application application){
        AppDatabase database = AppDatabase.getAppDatabase(application);
        bookMarkDao = database.bookMarkDao();
        allBookMarks = bookMarkDao.getAllBookMarks();


    }

    public void insert(BookMark bookMark){
        new InsertBookMark(bookMarkDao).execute(bookMark);
    }

    public void delete(BookMark bookMark){
        new DeleteBookMark(bookMarkDao).execute(bookMark);
    }

    public void deleteAllBookMark(){
        new DeleteAllBookMarks(bookMarkDao).execute();
    }

    public LiveData<List<BookMark>> getAllBookMarks() {
        return allBookMarks;
    }

    private static class InsertBookMark extends AsyncTask<BookMark, Void, Void>{
        private BookMarkDao bookMarkDao;
        public InsertBookMark(BookMarkDao bookMarkDao){
            this.bookMarkDao = bookMarkDao;
        }

        @Override
        protected Void doInBackground(BookMark... bookMarks) {
            bookMarkDao.insert(bookMarks[0]);
            return null;
        }
    }





    private static class DeleteBookMark extends AsyncTask<BookMark, Void, Void>{
        private BookMarkDao bookMarkDao;
        public DeleteBookMark(BookMarkDao bookMarkDao){
            this.bookMarkDao = bookMarkDao;
        }

        @Override
        protected Void doInBackground(BookMark... bookMarks) {
            bookMarkDao.delete(bookMarks[0]);
            return null;
        }
    }

    private static class DeleteAllBookMarks extends AsyncTask<Void,Void,Void>{
        private BookMarkDao bookMarkDao;

        public DeleteAllBookMarks(BookMarkDao bookMarkDao) {
            this.bookMarkDao = bookMarkDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bookMarkDao.deleteAllBookMarks();
            return null;
        }
    }

}
