package com.hoaxyinnovations.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.hoaxyinnovations.popularmovies.utlities.Movie;
import com.hoaxyinnovations.popularmovies.utlities.NetworkUtils;
import com.hoaxyinnovations.popularmovies.utlities.TMDBJsonUtils;
import com.squareup.picasso.Picasso;


import java.net.URL;
import java.util.ArrayList;

import static com.hoaxyinnovations.popularmovies.utlities.NetworkUtils.moviePosterUrl;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private ArrayList<Movie> moviestList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (ListView) findViewById(R.id.recyclerview_movielist);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);



        ArrayList<Movie> moviesList = new ArrayList<Movie>();

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        loadMovieData("popular");

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        mMovieAdapter = new MovieAdapter(this,moviesList);

        mRecyclerView.setAdapter(mMovieAdapter);

        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

            //on click
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie movie = mMovieAdapter.getItem(position);

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("title", movie.title);
                intent.putExtra("release_date", movie.release_date);
                intent.putExtra("overview",movie.overview);
                intent.putExtra("vote_average",movie.vote_average);
                intent.putExtra("poster_path",movie.poster_path);

                startActivity(intent);
            }
        };
        mRecyclerView.setOnItemClickListener(adapterViewListener);
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
                showMovieDataView();
                mMovieAdapter.addAll(movieData);
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
            mMovieAdapter.clear();
            loadMovieData("popular");
            return true;
        }

        if (id == R.id.action_top_rated) {
            mMovieAdapter.clear();;
            loadMovieData("top_rated");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
