package com.example.kholoud.myapplication;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;



import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
//DEF
    private LiveData<List<FavoriteMovie>> movies;

    public MainViewModel(Application application) {
        super(application);
        MovieDB database = MovieDB.getInstance(this.getApplication());
        movies = database.movieroom().loadAllMovies();
    }

    public LiveData<List<FavoriteMovie>> getMovies() {
        return movies;
    }
}
