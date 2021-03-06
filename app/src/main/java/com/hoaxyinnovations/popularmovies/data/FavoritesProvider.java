package com.hoaxyinnovations.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by kapsa on 11/10/2017.
 */

public class FavoritesProvider extends ContentProvider {

    private static final int CODE_FAVORITES = 200;
    private static final int CODE_FAVORITES_WITH_MOVIE_ID = 203;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavoritesDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoritesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoritesContract.PATH_FAVORITES, CODE_FAVORITES);

        matcher.addURI(authority, FavoritesContract.PATH_FAVORITES + "/#", CODE_FAVORITES_WITH_MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FavoritesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES:{
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoritesContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_FAVORITES_WITH_MOVIE_ID:{
                String movie_id = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movie_id};
                cursor= mOpenHelper.getReadableDatabase().query(
                        FavoritesContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES:{
                int rowsInserted = 0;
                long _id = db.insert(FavoritesContract.FavoriteEntry.TABLE_NAME, null, values);
                if (_id != -1) {
                    rowsInserted++;
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
            }

        }
        return Uri.withAppendedPath(FavoritesContract.BASE_CONTENT_URI, values != null ? values.getAsString(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID) : null);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        FavoritesContract.FavoriteEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
