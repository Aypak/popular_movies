package com.hoaxyinnovations.popularmovies.utlities;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public String title;
    public String poster_path;
    public String release_date;
    public String overview;
    public String vote_average;

    Movie(String title, String poster_path, String release_date, String overview, String vote_average){
        this.title = title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.poster_path);
        dest.writeString(this.release_date);
        dest.writeString(this.overview);
        dest.writeString(this.vote_average);
    }

    protected Movie(Parcel in) {
        this.title = in.readString();
        this.poster_path = in.readString();
        this.release_date = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
