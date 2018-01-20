package com.hoaxyinnovations.popularmovies.utlities;

import android.database.Cursor;

import com.hoaxyinnovations.popularmovies.data.FavoritesContract;

/**
 * Created by kapsa on 11/25/2017.
 */

public class DbUtils {
    public static Movie getMovieObjectsFromCursor(Cursor cursor){

        final String MOVIE_ID = FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID;
        final String MOVIE_TITLE = FavoritesContract.FavoriteEntry.COLUMN_TITLE;
        final String RELEASE_DATE = FavoritesContract.FavoriteEntry.COLUMN_RELEASE_DATE;
        final String MOVIE_POSTER = FavoritesContract.FavoriteEntry.COLUMN_POSTER_PATH;
        final String VOTE_AVERAGE = FavoritesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE;
        final String PLOT_OVERVIEW = FavoritesContract.FavoriteEntry.COLUMN_OVERVIEW;


        String id;
        String title;
        String poster_path;
        String release_date;
        String overview;
        String vote_average;
        String trailer;


        id = cursor.getString(cursor.getColumnIndex(MOVIE_ID));
        title = cursor.getString(cursor.getColumnIndex(MOVIE_TITLE));
        poster_path = cursor.getString(cursor.getColumnIndex(MOVIE_POSTER));
        release_date = cursor.getString(cursor.getColumnIndex(RELEASE_DATE));
        overview = cursor.getString(cursor.getColumnIndex(PLOT_OVERVIEW));
        vote_average = cursor.getString(cursor.getColumnIndex(VOTE_AVERAGE));
        trailer = null;

        return new Movie(id,title,poster_path,release_date,overview,vote_average, null);
    }


}
