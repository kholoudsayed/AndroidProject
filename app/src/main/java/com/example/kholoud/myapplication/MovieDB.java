package com.example.kholoud.myapplication;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;


@Database(entities = {FavoriteMovie.class}, version = 3, exportSchema = false)
public abstract class MovieDB extends RoomDatabase{
    private static final String LOG_TAG = MovieDB.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movies";
    private static MovieDB instance;

    public static MovieDB getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDB.class, MovieDB.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return instance;
    }

    public abstract MovieRoom movieroom();
}
