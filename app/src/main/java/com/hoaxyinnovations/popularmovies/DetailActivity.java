package com.hoaxyinnovations.popularmovies;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoaxyinnovations.popularmovies.data.FavoritesContract;
import com.hoaxyinnovations.popularmovies.utlities.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;

import static com.hoaxyinnovations.popularmovies.utlities.NetworkUtils.moviePosterUrl;
import static com.hoaxyinnovations.popularmovies.utlities.NetworkUtils.youtubevideoUri;

public class DetailActivity extends AppCompatActivity{

    private ImageView movieImage;
    private TextView movieTitle;
    private TextView movieOverview;
    private TextView movieVoteAverage;
    private TextView movieReleaseDate;
    private ImageButton addToFavorites;
    private Button playTrailerBtn;
    private LinearLayout rootview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieImage = (ImageView) findViewById(R.id.detail_movie_image);
        movieTitle = (TextView) findViewById(R.id.tv_movie_title);
        movieOverview = (TextView) findViewById(R.id.tv_movie_overview);
        movieVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
        movieReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        addToFavorites = (ImageButton) findViewById(R.id.add_to_favorites_btn);
        playTrailerBtn = (Button) findViewById(R.id.play_trailer_btn);

        Movie movie = getIntent().getParcelableExtra("movie");
        rootview = (LinearLayout) findViewById(R.id.reviews_container);

        /*add reviews to layout*/
        int index = 0;
        if(movie.reviews == null || movie.reviews.size() == 0){
            TextView errorMessage = new TextView(this);
            errorMessage.setText("Sorry, There are no reviews to display");
            rootview.addView(errorMessage);
        }
        else{
            while(index<movie.reviews.size()){

                TextView review = new TextView(this);

            /*convert padding from pixels to dp*/
                int paddingPixel = 25;
                float density = this.getResources().getDisplayMetrics().density;
                int paddingDp = (int)(paddingPixel * density);
                review.setPadding(0,paddingDp,0,paddingDp);

            /*create horizontal line between textviews*/
                View horizontalLine = new View(this);
                horizontalLine.setBackgroundColor(Color.parseColor("#c0c0c0"));
                horizontalLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,2));

            /*set text in textview to review then add it and horizontal line*/
                review.setText((String) movie.reviews.get(index));
                rootview.addView(review);
                rootview.addView(horizontalLine);
                index++;
            }
        }


        /*asynctask to play trailer when button is pressed*/
        playTrailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Movie movie = getIntent().getParcelableExtra("movie");
                            playTrailer(youtubevideoUri(movie.getTrailerIdFromJson(movie.id)));
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        addToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = getIntent().getParcelableExtra("movie");
                ContentValues contentValues = new ContentValues();
                contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID,movie.id);
                contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_OVERVIEW,movie.overview);
                contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_TITLE,movie.title);
                contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_POSTER_PATH,movie.poster_path);
                contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE,movie.vote_average);
                contentValues.put(FavoritesContract.FavoriteEntry.COLUMN_RELEASE_DATE,movie.release_date);
                getContentResolver().insert(FavoritesContract.FavoriteEntry.CONTENT_URI,contentValues);

                Toast.makeText(DetailActivity.this, movie.title + ": has been added to favorites", Toast.LENGTH_LONG).show();

            }
        });

        /*movie = getIntent().getParcelableExtra("movie");*/
        String moviePosterUrl = (moviePosterUrl(movie.poster_path)).toString();
        Picasso.with(getApplicationContext()).load(moviePosterUrl).into(movieImage);

        movieTitle.setText(movie.title);
        movieOverview.setText(movie.overview);
        movieVoteAverage.setText(String.format("%s%s", getString(R.string.vote_average), movie.vote_average));
        movieReleaseDate.setText(String.format("%s%s", getString(R.string.release_date), movie.release_date));

        if(checkIfMovieInFavorites(movie.id)){
            addToFavorites.setVisibility(View.INVISIBLE);
        }

    }


    private boolean checkIfMovieInFavorites(String movieID){
        String[] selectionArgs = new String[]{movieID};
        Cursor cursor = getContentResolver().query(FavoritesContract.FavoriteEntry.CONTENT_URI,null, FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID + "= ?",selectionArgs, null);
        if((cursor != null ? cursor.getCount() : 0) > 0){
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }
    }

    private void playTrailer(Uri youtubeURL){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, youtubeURL);
        startActivity(browserIntent);
    }


}
