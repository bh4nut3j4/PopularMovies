package bhanuteja.android.com.popularmovies.ui.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 5/26/17.
 */

public class MovieModel implements Parcelable {
    private String poster_Url;
    private String movieID;

    public MovieModel(String poster_url, String movie_ID) {
        this.poster_Url = poster_url;
        this.movieID = movie_ID;
    }

    public MovieModel(Parcel in) {
        poster_Url = in.readString();
        movieID=in.readString();
    }
    public MovieModel() {

    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getPosterUrl() {
        return poster_Url;
    }

    public void setPosterUrl(String poster_url) {
        poster_Url = poster_url;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_Url);
        dest.writeString(movieID);
    }
}
