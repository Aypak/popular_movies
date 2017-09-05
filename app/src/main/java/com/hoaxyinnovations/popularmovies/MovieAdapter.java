package com.hoaxyinnovations.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import com.hoaxyinnovations.popularmovies.utlities.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.hoaxyinnovations.popularmovies.utlities.NetworkUtils.moviePosterUrl;


public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Activity context, ArrayList<Movie> movies){
        super(context, 0, movies);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Movie movie = getItem(position);

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        ImageView moviePoster = (ImageView) rootView.findViewById(R.id.movie_list_item);

        String posterPath = (moviePosterUrl(movie.poster_path,"w185")).toString();
        Picasso.with(getContext()).load(posterPath).into(moviePoster);
        return rootView;

    }


}