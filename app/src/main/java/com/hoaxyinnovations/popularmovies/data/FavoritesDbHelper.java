package com.hoaxyinnovations.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kapsa on 11/10/2017.
 */

class FavoritesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 3;

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE =

                "CREATE TABLE IF NOT EXISTS " + FavoritesContract.FavoriteEntry.TABLE_NAME + " (" +
                        FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID + " VARCHAR PRIMARY KEY, " +
                        FavoritesContract.FavoriteEntry.COLUMN_TITLE + " VARCHAR NOT NULL, "+
                        FavoritesContract.FavoriteEntry.COLUMN_POSTER_PATH+ " VARCHAR NOT NULL, "+
                        FavoritesContract.FavoriteEntry.COLUMN_OVERVIEW + " VARCHAR NOT NULL, "+
                        FavoritesContract.FavoriteEntry.COLUMN_RELEASE_DATE + " VARCHAR NOT NULL, "+
                        FavoritesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE + " VARCHAR NOT NULL "+ ")";
        Log.d("sql query: ",SQL_CREATE_FAVORITES_TABLE);
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);

    }
}
