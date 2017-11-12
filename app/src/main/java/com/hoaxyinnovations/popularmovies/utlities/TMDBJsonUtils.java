package com.hoaxyinnovations.popularmovies.utlities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class TMDBJsonUtils {

    public static Movie[] getMovieObjectsFromJson(String moviesJsonStr)
            throws JSONException {


        final String TMDB_MOVIE_LIST = "results";

        final String TMDB_MOVIE_TITLE = "title";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_MOVIE_POSTER = "poster_path";
        final String TMDB_VOTE_AVERAGE = "vote_average";
        final String TMDB_PLOT_OVERVIEW = "overview";


        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        JSONArray movieArray = moviesJson.getJSONArray(TMDB_MOVIE_LIST);

        Movie[] parsedMovieData;
        parsedMovieData = new Movie[movieArray.length()];


        for (int i = 0; i < movieArray.length(); i++) {
            String title;
            String poster_path;
            String release_date;
            String overview;
            String vote_average;

            /* Get the JSON object representing the day */
            JSONObject movieDetails = movieArray.getJSONObject(i);

            title = movieDetails.getString(TMDB_MOVIE_TITLE);
            poster_path = movieDetails.getString(TMDB_MOVIE_POSTER);
            release_date = movieDetails.getString(TMDB_RELEASE_DATE);
            overview = movieDetails.getString(TMDB_PLOT_OVERVIEW);
            vote_average = movieDetails.getString(TMDB_VOTE_AVERAGE);

            parsedMovieData[i] = new Movie(title,poster_path,release_date,overview,vote_average);
        }

        return parsedMovieData;
    }

}
