package com.hoaxyinnovations.popularmovies.utlities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Movie implements Parcelable {
    public final String id;
    public final String title;
    public final String poster_path;
    public final String release_date;
    public final String overview;
    public final String vote_average;
    private final String trailer;
    public ArrayList reviews;

    Movie(String id, String title, String poster_path, String release_date, String overview, String vote_average, String trailer){
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.trailer = trailer;
        this.reviews = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.poster_path);
        dest.writeString(this.release_date);
        dest.writeString(this.overview);
        dest.writeString(this.vote_average);
        dest.writeString(this.trailer);
        if (reviews == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(reviews);
        }

    }

    private Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.poster_path = in.readString();
        this.release_date = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readString();
        this.trailer = in.readString();
        if (in.readByte() == 0x01) {
            reviews = new ArrayList();
            in.readList(reviews, String.class.getClassLoader());
        } else {
            reviews = null;
        }
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

    public String getTrailerIdFromJson(String movieID) throws IOException, JSONException {
        movieID = this.id;
        JSONObject videosJSoN = null;
        try {
            videosJSoN = new JSONObject(NetworkUtils.getResponseFromHttpUrl(NetworkUtils.videosListUrl(movieID)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray videoResultsArray = null;
        if (videosJSoN != null) {
            videoResultsArray = videosJSoN.getJSONArray("results");
        }
        String trailerString = null;
        int i = 0;
        while(i< (videoResultsArray != null ? videoResultsArray.length() : 0)){
            JSONObject videodetails = videoResultsArray.getJSONObject(i);
            if(videodetails.getString("name").contains("Trailer")){
                trailerString = videodetails.getString("key");
                break;
            }
            else{
                i++;
            }
        }
        return trailerString;
    }

    private ArrayList<String> getreviewsJSON(String movieID) throws JSONException {
        movieID = this.id;
        ArrayList<String> reviewsList = new ArrayList<>();
        try {
            JSONObject reviewsJSON = new JSONObject(NetworkUtils.getResponseFromHttpUrl(NetworkUtils.reviewsListUrl(movieID)));
            JSONArray reviewsArray;
            reviewsArray = reviewsJSON.getJSONArray("results");

            for(int i = 0;i<reviewsArray.length();i++){
                JSONObject reviewObject = reviewsArray.getJSONObject(i);
                reviewsList.add(reviewObject.getString("content"));
            }
            return reviewsList;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setReviews(String movieID) throws JSONException {
        movieID = this.id;
        this.reviews = getreviewsJSON(movieID);
    }

}
