package bhanuteja.android.com.popularmovies.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by root on 5/26/17.
 */

public class MoviesJsonUtil {

    public static final String STATUSCODE="status_code";
    public static final String RESULT_ARRAY_NAME ="results";
    public static final String POSTER_URL_NAME="poster_path";
    public static final String MOVIE_ID="id";
    public static final String POSTER_BASE_URL="https://image.tmdb.org/t/p/w185/";
    public static final String Movie_Title="title";
    public static final String Movie_Synopsis="overview";
    public static final String Movie_User_Rating="vote_average";
    public static final String Movie_Release_Date="release_date";
    //constants for traiers
    public static final String YouTube_BASE_URL="https://www.youtube.com/watch?v=";
    public static final String Trailer_ID="id";
    public static final String Video_KEY="key";
    public static final String Video_Name="name";
    //constants for reviews
    public static final String Review_ID="id";
    public static final String Review_Auther="author";
    public static final String Review_Content="content";

    public JSONArray ExtractJson(String jsondata) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsondata);

        if (jsonObject.has(STATUSCODE)) {
            int errorCode = jsonObject.getInt(STATUSCODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    return null;
                default:
                    return null;
            }
        }
        try {
            JSONArray j = jsonObject.getJSONArray(RESULT_ARRAY_NAME);
            Log.d("DATAA",j.toString());
            return j;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
