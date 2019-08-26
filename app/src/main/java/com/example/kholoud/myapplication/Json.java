package com.example.kholoud.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Json {
    private static final String TAG = Json.class.getSimpleName();

    public static ArrayList<MoviesClass> MoviesJson(String json) {
        try {

            MoviesClass movie;
            JSONObject object = new JSONObject(json);

            JSONArray res = new JSONArray(object.optString("results","[\"\"]"));

            ArrayList<MoviesClass> Arrays = new ArrayList<>();
            int i= 0;
            while ( i < res.length()) {
                String item = res.optString(i, "");
                JSONObject movieJson = new JSONObject(item);

                movie = new MoviesClass(
                        movieJson.optString("id","Not Available"),
                        movieJson.optString("original_title","Not Available"),
                        movieJson.optString("release_date","Not Available"),
                        movieJson.optString("vote_average","Not Available"),
                        movieJson.optString("popularity","Not Available"),
                        movieJson.optString("overview","Not Available"),
                        movieJson.optString("poster_path","Not Available"),
                        movieJson.optString("backdrop_path","Not Available")
                );

                Arrays.add(movie);
                i++;
            }
            return Arrays;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static ArrayList<ReviewClass> ReviewsJson(String json) {
        try {

            ReviewClass review;
            JSONObject object = new JSONObject(json); //(json.replace("\\",""));

            JSONArray res = new JSONArray(object.optString("results","[\"\"]"));

            ArrayList<ReviewClass> Arrays = new ArrayList<>();
            int  i =0;

            while ( i < res.length()) {
                String item = res.optString(i, "");
                JSONObject movieJson = new JSONObject(item);

                review = new ReviewClass(
                        movieJson.optString("author","Not Available"),
                        movieJson.optString("content","Not Available"),
                        movieJson.optString("id","Not Available"),
                        movieJson.optString("url","Not Available")
                );

                Arrays.add(review);
                i++;
            }
            return Arrays;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static ArrayList<TrailerClass> TrailersJson(String json) {
        try {
            TrailerClass trailer;
            JSONObject object = new JSONObject(json); //(json.replace("\\",""));
            JSONArray res = new JSONArray(object.optString("results","[\"\"]"));

            ArrayList<TrailerClass> Arrays = new ArrayList<>();
            int i =0;

            while ( i < res.length()) {
                String thisitem = res.optString(i, "");
                JSONObject movieJson = new JSONObject(thisitem);

                trailer = new TrailerClass(
                        movieJson.optString("name","Not Available"),
                        movieJson.optString("site","Not Available"),
                        movieJson.optString("key","Not Available")
                );

                Arrays.add(trailer);
                i++;
            }
            return Arrays;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
