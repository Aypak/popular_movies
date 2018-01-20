package com.hoaxyinnovations.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hoaxyinnovations.popularmovies.data.FavoritesContract;
import com.hoaxyinnovations.popularmovies.utlities.DbUtils;
import com.hoaxyinnovations.popularmovies.utlities.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import static com.hoaxyinnovations.popularmovies.utlities.NetworkUtils.moviePosterUrl;


public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{
    private Cursor mCursor;

    private final Context mContext;

    public FavoritesAdapter(Context context) {
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View moviesView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(moviesView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        ImageView moviePoster = holder.moviePoster;
        String posterPathString = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_POSTER_PATH));
        String posterPath = (moviePosterUrl(posterPathString)).toString();
        Picasso.with(getContext()).load(posterPath).into(moviePoster);


    }



    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView moviePoster;

        ViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_list_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        int adapterPosition = getAdapterPosition();
                        mCursor.moveToPosition(adapterPosition);
                        Movie movie = DbUtils.getMovieObjectsFromCursor(mCursor);
                        movie.setReviews(movie.id);
                        Context context = getContext();
                        Class destinationClass = DetailActivity.class;
                        Intent intent = new Intent(context, destinationClass);
                        intent.putExtra("movie", movie);
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

}
