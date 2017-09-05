package com.hoaxyinnovations.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static com.hoaxyinnovations.popularmovies.utlities.NetworkUtils.moviePosterUrl;

public class DetailActivity extends AppCompatActivity {
    private ImageView movieimage;
    private TextView movieTitle;
    private TextView movieOverview;
    private TextView movieVoteAverage;
    private TextView movieReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieimage = (ImageView) findViewById(R.id.detail_movie_image);
        movieTitle = (TextView) findViewById(R.id.tv_movie_title);
        movieOverview = (TextView) findViewById(R.id.tv_movie_overview);
        movieVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
        movieReleaseDate = (TextView) findViewById(R.id.tv_release_date);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String posterPath = intent.getStringExtra("poster_path");
        String overview = intent.getStringExtra("overview");
        String vote_average = intent.getStringExtra("vote_average");
        String release_date = intent.getStringExtra("release_date");


        String moviePosterUrl = (moviePosterUrl(posterPath,"w185")).toString();
        Picasso.with(getApplicationContext()).load(moviePosterUrl).into(movieimage);

        movieTitle.setText(title);
        movieOverview.setText(overview);
        movieVoteAverage.setText("Vote Average: " + vote_average);
        movieReleaseDate.setText("Release Date: " + release_date);


    }
}
