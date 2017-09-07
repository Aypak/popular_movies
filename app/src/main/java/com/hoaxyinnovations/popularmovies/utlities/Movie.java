package com.hoaxyinnovations.popularmovies.utlities;

public class Movie {
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

}
