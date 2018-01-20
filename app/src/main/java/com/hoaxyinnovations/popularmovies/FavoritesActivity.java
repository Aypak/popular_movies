package com.hoaxyinnovations.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hoaxyinnovations.popularmovies.R;
import com.hoaxyinnovations.popularmovies.data.FavoritesContract;
import com.hoaxyinnovations.popularmovies.utlities.Movie;

public class FavoritesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{


    private static final int ID_FAVORITES_LOADER = 1;

    private final String TAG = FavoritesActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private FavoritesAdapter mFavoritesAdapter;
    
    private TextView mNoFavoritesMessage;

    private ProgressBar mLoadingIndicator;

    private Movie[] moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        getSupportActionBar().setElevation(0f);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movielist);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mNoFavoritesMessage = (TextView) findViewById(R.id.tv_no_favorites_message_display);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);

        mFavoritesAdapter = new FavoritesAdapter(this);

        mRecyclerView.setAdapter(mFavoritesAdapter);

        LoaderManager.LoaderCallbacks<Cursor> callback = FavoritesActivity.this;

        getSupportLoaderManager().initLoader(ID_FAVORITES_LOADER, null, callback);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        switch (id) {

            case ID_FAVORITES_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri favoritesQueryUri = FavoritesContract.FavoriteEntry.CONTENT_URI;

                return new CursorLoader(this,
                        favoritesQueryUri,
                        null,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(data.getCount() == 0){
            mNoFavoritesMessage.setVisibility(View.VISIBLE);
        }
        else {
            mFavoritesAdapter.swapCursor(data);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


    }
}
