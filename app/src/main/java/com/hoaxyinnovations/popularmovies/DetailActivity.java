package com.hoaxyinnovations.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoaxyinnovations.popularmovies.utlities.Movie;
import com.squareup.picasso.Picasso;

import static com.hoaxyinnovations.popularmovies.utlities.NetworkUtils.moviePosterUrl;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView movieImage = (ImageView) findViewById(R.id.detail_movie_image);
        TextView movieTitle = (TextView) findViewById(R.id.tv_movie_title);
        TextView movieOverview = (TextView) findViewById(R.id.tv_movie_overview);
        TextView movieVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
        TextView movieReleaseDate = (TextView) findViewById(R.id.tv_release_date);

        Movie movie = getIntent().getParcelableExtra("movie");


        String moviePosterUrl = (moviePosterUrl(movie.poster_path)).toString();
        Picasso.with(getApplicationContext()).load(moviePosterUrl).into(movieImage);

        movieTitle.setText(movie.title);
        movieOverview.setText(movie.overview);
        movieVoteAverage.setText(String.format("%s%s", getString(R.string.vote_average), movie.vote_average));
        movieReleaseDate.setText(String.format("%s%s", getString(R.string.release_date), movie.release_date));


    }
}
