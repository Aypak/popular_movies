package com.hoaxyinnovations.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.hoaxyinnovations.popularmovies.utlities.Movie;
import com.hoaxyinnovations.popularmovies.utlities.NetworkUtils;
import com.hoaxyinnovations.popularmovies.utlities.TMDBJsonUtils;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie[]> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private String sortOrder;

    private Movie[] moviesList;

    private static final int MOVIES_LOADER_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movielist);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /*Initially set sortorder to popular*/
        sortOrder = "popular";


        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this,moviesList);

        mRecyclerView.setAdapter(mMovieAdapter);

        LoaderManager.LoaderCallbacks<Movie[]> callback = MainActivity.this;

        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, callback);
    }




    private void showMovieDataView() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Movie[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Movie[]>(this) {

            Movie[] mMovieData = null;

            @Override
            protected void onStartLoading() {

                mLoadingIndicator.setVisibility(View.VISIBLE);

                if (mMovieData!= null) {
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Movie[] loadInBackground() {

                URL moviesListUrl = NetworkUtils.moviesListUrl(sortOrder);

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(moviesListUrl);
                    return TMDBJsonUtils.getMovieObjectsFromJson(jsonMovieResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(Movie[] movieData) {
                mMovieData = movieData;
                super.deliverResult(movieData);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie[]> loader, Movie[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        moviesList = data;
        mMovieAdapter.setMovieData(moviesList);
        if (null == data) {
            showErrorMessage();
        } else {
            showMovieDataView();
        }

    }

    @Override
    public void onLoaderReset(Loader<Movie[]> loader) {

    }

    private void invalidateData() {
        mMovieAdapter.setMovieData(null);
    }

    private void startFavoritesActivity() {
        Intent favoritesIntent = new Intent(MainActivity.this, FavoritesActivity.class);
        startActivity(favoritesIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            invalidateData();
            sortOrder = "popular";
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
            return true;
        }

        if (id == R.id.action_top_rated) {
            invalidateData();
            sortOrder = "top_rated";
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
            return true;
        }

        if (id == R.id.action_favorites) {
         startFavoritesActivity();
        }

        return super.onOptionsItemSelected(item);
    }



}
