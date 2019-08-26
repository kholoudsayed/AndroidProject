package com.example.kholoud.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity  implements TAdapter.ListItemClickListener{
    private static final String TAG = MovieDetails.class.getSimpleName();
    //DEF
    private MoviesClass movieItem;
    private ArrayList<ReviewClass> reviewList;
    private ArrayList<TrailerClass> trailerList;
    private RecyclerView trailerRV;
    private TAdapter tAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private MovieDB MDB;
    private ImageView FAV;
    private Boolean isFav = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String user_name = "name";
        intent.putExtra("NAME", user_name);
        if (intent == null) {
            closeOnError("Intent is null");
        }

        movieItem = (MoviesClass) intent.getSerializableExtra("movieItem");
        if (movieItem == null) {
            closeOnError("No DATA Movie");
            return;
        }

        // Recycler view for trailers
        trailerRV = findViewById(R.id.rv_trailers);
        tAdapter = new TAdapter(this, trailerList, this);
        trailerRV.setAdapter(tAdapter);
        layoutManager = new LinearLayoutManager(this);
        trailerRV.setLayoutManager(layoutManager);

        // favorite movies
        FAV = findViewById(R.id.iv_favButton);
        MDB = MovieDB.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final FavoriteMovie fmov = MDB.movieroom().loadMovieById(Integer.parseInt(movieItem.getId()));
                setFavorite((fmov != null)? true : false);
            }
        });

        // Fetch data from the Web API
        FatchData(movieItem.getId());

    }

    private void setFavorite(Boolean fav){
        if (fav) {
            isFav = true;
            FAV.setImageResource(R.drawable.ic_favorite_solid_24dp);
        } else {
            isFav = false;
            FAV.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private static class SearchURLs {
        URL reviewSearchUrl;
        URL trailerSearchUrl;
        SearchURLs(URL reviewSearchUrl, URL trailerSearchUrl){
            this.reviewSearchUrl = reviewSearchUrl;
            this.trailerSearchUrl = trailerSearchUrl;
        }
    }
    private static class ResultsStrings {
        String reviewString;
        String trailerString;
        ResultsStrings(String reviewString, String trailerString){
            this.reviewString = reviewString;
            this.trailerString = trailerString;
        }
    }
    private void FatchData(String id) {
        String review = id + File.separator + "reviews";
        String trailer= id + File.separator + "videos";
        SearchURLs searchURLs = new SearchURLs(
                Network.buildUrl(review, "62513231849c8b7be10def21e3b89425"),
                Network.buildUrl(trailer, "62513231849c8b7be10def21e3b89425")
                );
        new ReviewsQueryTask().execute(searchURLs);
    }


    // AsyncTask to perform query
    public class ReviewsQueryTask extends AsyncTask<SearchURLs, Void, ResultsStrings> {
        @Override
        protected ResultsStrings doInBackground(SearchURLs... params) {
            URL reviewUrl = params[0].reviewSearchUrl;
            URL trailerUrl = params[0].trailerSearchUrl;

            String reviewResults = null;
            try {
                reviewResults = Network.getResponseFromHttpUrl(reviewUrl);
                reviewList = Json.ReviewsJson(reviewResults);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String trailerResults = null;
            try {
                trailerResults = Network.getResponseFromHttpUrl(trailerUrl);
                trailerList = Json.TrailersJson(trailerResults);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ResultsStrings results = new ResultsStrings(reviewResults,trailerResults);

            return results;
        }

        @Override
        protected void onPostExecute(ResultsStrings results) {
            String searchResults = results.reviewString;
            if (searchResults != null && !searchResults.equals("")) {
                reviewList = Json.ReviewsJson(searchResults);
                populateDetails();
            }
        }
    }

    private void Youtube(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        webIntent.putExtra("finish_on_ended", true);
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public void OnListItemClick(TrailerClass trailerItem) {
        Log.d(TAG,trailerItem.getKey());
        Youtube(trailerItem.getKey());
    }

    private void populateDetails() {

        ((TextView)findViewById(R.id.title)).setText(movieItem.getTitle());
        ((TextView)findViewById(R.id.tv_header_rating)).append(" ("+movieItem.getVote()+"/10)");
        ((RatingBar)findViewById(R.id.user_rating)).setRating(Float.parseFloat(movieItem.getVote()));
        ((TextView)findViewById(R.id.release_date)).setText(movieItem.getReleaseDate());
        ((TextView)findViewById(R.id.synopsis)).setText(movieItem.getSynopsis());

        // Favorite
        FAV.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                final FavoriteMovie mov = new FavoriteMovie(
                        Integer.parseInt(movieItem.getId()),
                        movieItem.getTitle(),
                        movieItem.getReleaseDate(),
                        movieItem.getVote(),
                        movieItem.getPopularity(),
                        movieItem.getSynopsis(),
                        movieItem.getImage(),
                        movieItem.getBackdrop()
                );
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isFav) {
                            // delete item
                            MDB.movieroom().deleteMovie(mov);
                        } else {
                            // insert item
                            MDB.movieroom().insertMovie(mov);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setFavorite(!isFav);
                            }
                        });
                    }

                });
            }
        });



        // Trailers
        tAdapter.PutTrailer(trailerList);

        // Reviews
        ((TextView)findViewById(R.id.tv_reviews)).setText("");
        for(int i=0; i<reviewList.size(); i++) {
            ((TextView)findViewById(R.id.tv_reviews)).append("\n");
            ((TextView)findViewById(R.id.tv_reviews)).append(reviewList.get(i).getContent());
            ((TextView)findViewById(R.id.tv_reviews)).append("\n\n");
            ((TextView)findViewById(R.id.tv_reviews)).append(" - Reviewed by ");
            ((TextView)findViewById(R.id.tv_reviews)).append(reviewList.get(i).getAuthor());
            ((TextView)findViewById(R.id.tv_reviews)).append("\n\n--------------\n");
        }

        String backdropPathURL = Network.buildPosterUrl(movieItem.getBackdrop());

        try {
            Picasso.with(this)
                    .load(backdropPathURL)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into((ImageView)this.findViewById(R.id.backdrop));
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

        String imagePathURL = Network.buildPosterUrl(movieItem.getImage());

        try {
            Picasso.with(this)
                    .load(imagePathURL)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into((ImageView)this.findViewById(R.id.image));
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

    }

    private void closeOnError(String msg) {
        finish();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
