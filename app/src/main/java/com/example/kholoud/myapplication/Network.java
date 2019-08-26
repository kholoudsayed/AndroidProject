package com.example.kholoud.myapplication;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Network {
    private static final String TAG = Network.class.getSimpleName();
    //**************************************DEF
    private final static String URL = "http://image.tmdb.org/t/p/";

    private final static String WIDTH = "w200";
    private final static String BASE = "http://api.themoviedb.org/3/movie";
    private final static String API_KEY = "api_key";

    public static URL buildUrl(String movieSearchQuery, String apiKey) {
        Uri builtUri = Uri.parse(BASE).buildUpon()
                .appendEncodedPath(movieSearchQuery)
                .appendQueryParameter(API_KEY,apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String buildPosterUrl(String poster) {

        String finalPath = URL + WIDTH + "/" + poster;
        return finalPath;

    }

}
