package bhanuteja.android.com.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by root on 5/26/17.
 */

public class NetworkUtils {
    private static final String BASE_URL="https://api.themoviedb.org/3/movie";
    private static final String MOST_POPULAR_PATH="popular";
    private static final String TOP_RATED_PATH="top_rated";
    private static final String APIKEY ="api_key";
    private static final String language="language";
    private static final String languagevalue="en-US";
    private static final String page="page";
    private static final String pagevalue ="1";
    private static final String key ="YOUR_MOVIEDB_APPI_KEY";

    private static final String TRAILERS_PATH="videos";
    private static final String REVIEWS_PATH="reviews";


    public URL buildURL(int id){
        URL url = null;
        if (id==0){
            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(MOST_POPULAR_PATH)
                    .appendQueryParameter(APIKEY, key)
                    .appendQueryParameter(language,languagevalue)
                    .appendQueryParameter(page,pagevalue)
                    .build();
            try {
                url = new URL(uri.toString());
                return url;
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (id==1) {
            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(TOP_RATED_PATH)
                    .appendQueryParameter(APIKEY, key)
                    .appendQueryParameter(language, languagevalue)
                    .appendQueryParameter(page, pagevalue)
                    .build();
            try {
                url = new URL(uri.toString());
                return url;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    public URL getURLS(int id,String movieid){
        URL url=null;
        if (id==0){
            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(movieid)
                    .appendQueryParameter(APIKEY,key)
                    .appendQueryParameter(language,languagevalue)
                    .appendQueryParameter(page,pagevalue)
                    .build();
            try {
                url = new URL(uri.toString());
                return url;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        if (id==1){
            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(movieid)
                    .appendPath(TRAILERS_PATH)
                    .appendQueryParameter(APIKEY, key)
                    .appendQueryParameter(language, languagevalue)
                    .appendQueryParameter(page, pagevalue)
                    .build();
            try {
                url = new URL(uri.toString());
                return url;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (id==2){
            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(movieid)
                    .appendPath(REVIEWS_PATH)
                    .appendQueryParameter(APIKEY, key)
                    .appendQueryParameter(language, languagevalue)
                    .appendQueryParameter(page, pagevalue)
                    .build();
            try {
                url = new URL(uri.toString());
                return url;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String GetMoviesJson(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream is = urlConnection.getInputStream();
            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");
            boolean hasInput=scanner.hasNext();
            if (hasInput){
                return scanner.next();
            }else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    public String GetDetailsJson(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream is = urlConnection.getInputStream();
            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");
            boolean hasInput=scanner.hasNext();
            if (hasInput){
                return scanner.next();
            }else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }




}
