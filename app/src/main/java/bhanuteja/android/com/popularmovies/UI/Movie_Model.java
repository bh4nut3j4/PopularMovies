package bhanuteja.android.com.popularmovies.UI;

/**
 * Created by root on 5/26/17.
 */

public class Movie_Model {
    private String Poster_url;
    private String Movie_ID;

    public Movie_Model(String poster_url, String movie_ID){
        this.Poster_url=poster_url;
        this.Movie_ID=movie_ID;
    }

    public Movie_Model() {

    }

    public String getPoster_url() {
        return Poster_url;
    }

    public void setPoster_url(String poster_url) {
        Poster_url = poster_url;
    }

    public String getMovie_ID() {
        return Movie_ID;
    }

    public void setMovie_ID(String movie_ID) {
        Movie_ID = movie_ID;
    }

}
