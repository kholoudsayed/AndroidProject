package com.example.kholoud.myapplication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.example.kholoud.myapplication.FavoriteMovie;
import com.example.kholoud.myapplication.Json;
import com.example.kholoud.myapplication.MAdapter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String SORT_POPULAR = "popular";
    private static final String SORT_TOP_RATED = "top_rated";
    private static final String SORT_FAVORITE = "favorite";
    private static String DefultSort = SORT_POPULAR;
    private ArrayList<MoviesClass> movieList;
    private RecyclerView MovieRV;
    private MAdapter mAdapter;
    private List<FavoriteMovie> FAV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MovieRV = (RecyclerView) findViewById(R.id.mainRV);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        MovieRV.setLayoutManager(layoutManager);
        MovieRV.setHasFixedSize(true);

        mAdapter = new MAdapter(movieList, this, this);
        MovieRV.setAdapter(mAdapter);

        FAV = new ArrayList<FavoriteMovie>();

        setTitle(getString(R.string.app_name) + " - Popular");

        SET();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_popular && !DefultSort.equals(SORT_POPULAR)) {
            Clear();
            DefultSort = SORT_POPULAR;
            setTitle(getString(R.string.app_name) + " - Popular");
            load();
            return true;
        }
        if (id == R.id.sort_top_rated && !DefultSort.equals(SORT_TOP_RATED)) {
            Clear();
            DefultSort = SORT_TOP_RATED;
            setTitle(getString(R.string.app_name) + " - Top rated");
            load();
            return true;
        }
        if (id == R.id.sort_favorite && !DefultSort.equals(SORT_FAVORITE)) {
            Clear();
            DefultSort = SORT_FAVORITE;
            setTitle(getString(R.string.app_name) + " - Favorite");
            load();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Clear() {
        if (movieList != null) {
            movieList.clear();
        } else {
            movieList = new ArrayList<MoviesClass>();
        }
    }

    private void Search() {
        if (DefultSort.equals(SORT_FAVORITE)) {
            Clear();
            int i= 0;
            while (i< FAV.size()){
               MoviesClass mov = new MoviesClass(
                        String.valueOf(FAV.get(i).getId()),
                        FAV.get(i).getTitle(),
                        FAV.get(i).getReleaseDate(),
                        FAV.get(i).getVote(),
                        FAV.get(i).getPopularity(),
                        FAV.get(i).getSynopsis(),
                        FAV.get(i).getImage(),
                        FAV.get(i).getBackdrop()
                );
                movieList.add( mov );
                i++;
            }
            mAdapter.setMovieData(movieList);
        } else {
            String movieQuery = DefultSort;
            URL movieSearchUrl =Network.buildUrl(movieQuery, "62513231849c8b7be10def21e3b89425");
            new MovieSyn().execute(movieSearchUrl);
        }
    }

    public class MovieSyn extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = Network.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {
                movieList = Json.MoviesJson(searchResults);
                mAdapter.setMovieData(movieList);
            }
        }
    }

    private void SET() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovie> favs) {
                if(favs.size()>0) {
                    FAV.clear();
                    FAV = favs;
                }

                load();
            }
        });
    }

    @Override
    public void OnListItemClick(MoviesClass movieItem) {
        Intent myIntent = new Intent(this, MovieDetails.class);
        myIntent.putExtra("movieItem", movieItem);
        startActivity(myIntent);
    }

    private void load() {
        Search();
    }

}
