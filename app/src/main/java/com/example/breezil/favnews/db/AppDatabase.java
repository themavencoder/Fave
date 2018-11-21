package com.example.breezil.favnews.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.breezil.favnews.model.Articles;
import com.example.breezil.favnews.model.BookMark;
import com.example.breezil.favnews.model.Source;

@Database(entities = {BookMark.class}, version = 1)
public abstract class AppDatabase  extends RoomDatabase {
    private static  AppDatabase appDatabase;
    public abstract BookMarkDao bookMarkDao();

    public static synchronized AppDatabase getAppDatabase(Context context){
        if(appDatabase == null){
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "article_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDatabase;
    }
}
