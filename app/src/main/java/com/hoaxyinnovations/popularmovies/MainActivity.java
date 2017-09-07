package com.hoaxyinnovations.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;


    private Movie[] moviesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movielist);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovieData("popular");

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this,moviesList);

        mRecyclerView.setAdapter(mMovieAdapter);

    }

    private void loadMovieData(String sortBy) {
        showMovieDataView();
        new FetchMovieTask().execute(sortBy);
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

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String sortBy = params[0];
            URL moviesListUrl = NetworkUtils.moviesListUrl(sortBy);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviesListUrl);

                return TMDBJsonUtils
                        .getMovieObjectsFromJson(MainActivity.this, jsonMovieResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                moviesList = movieData;
                mMovieAdapter.setMovieData(moviesList);
            } else {
                showErrorMessage();
            }
        }


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
            mMovieAdapter.setMovieData(null);
            loadMovieData("popular");
            return true;
        }

        if (id == R.id.action_top_rated) {
            mMovieAdapter.setMovieData(null);
            loadMovieData("top_rated");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
