package com.hoaxyinnovations.popularmovies.utlities;

import android.net.Uri;
import android.util.Log;

import com.hoaxyinnovations.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String API_KEY_PARAM = "api_key";

    private static final String API_KEY = BuildConfig.API_KEY;

    /**
     * Builds the URL used to talk to TMDB API using a sort param and API key
     *
     * @param sortBy Can be top_rated or popular.
     * @return The URL to use to query the TMDB API.
     */

    public static URL moviesListUrl(String sortBy){
        String baseURL = BASE_URL + sortBy;
        Uri builtUri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;

    }

    /**
     * Builds the URL used to talk to TMDB API and get the path to the poster for a movie. No API key needed to get the path to the poster
     *
     * @param posterPath The poster_path as it appears in the query response from moviesList.
     * @param posterSize w185 recomended
     * @return The URL to use to query the TMDB API.
     */

    public static URL moviePosterUrl(String posterPath, String posterSize){
        String moviePosterPath = IMAGE_BASE_URL+posterSize+posterPath;
        Uri builtUri = Uri.parse(moviePosterPath).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;

    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
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
}