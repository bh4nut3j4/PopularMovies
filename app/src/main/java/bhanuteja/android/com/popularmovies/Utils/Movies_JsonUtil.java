package bhanuteja.android.com.popularmovies.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by root on 5/26/17.
 */

public class Movies_JsonUtil {

    public static final String STATUSCODE="status_code";
    public static final String RESULT_ARRAY_NAME ="results";
    public static final String POSTER_URL_NAME="poster_path";
    public static final String MOVIE_ID="id";
    public static final String POSTER_BASE_URL="https://image.tmdb.org/t/p/w185/";
    public static final String Movie_Title="title";
    public static final String Movie_Synopsis="overview";
    public static final String Movie_User_Rating="vote_average";
    public static final String Movie_Release_Date="release_date";

    public JSONArray ExtractJson(String jsondata) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsondata);
        Log.d("OBJ",jsonObject.toString());

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

            JSONArray jsonArray = jsonObject.getJSONArray(RESULT_ARRAY_NAME);
            Log.e("JSONUTil",jsonArray.toString());
            return jsonArray;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
