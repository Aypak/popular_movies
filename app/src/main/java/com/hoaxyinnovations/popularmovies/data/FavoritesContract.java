package com.hoaxyinnovations.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kapsa on 11/10/2017.
 */

public class FavoritesContract {

    public static final String CONTENT_AUTHORITY = "com.hoaxyinnovations.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "movie_poster";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "plot_synopsis";

        public static Uri buildFavoritesUriWithMovieid(int movieId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .build();
        }

    }



}
