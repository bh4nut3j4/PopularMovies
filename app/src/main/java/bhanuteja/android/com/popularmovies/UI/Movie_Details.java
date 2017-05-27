package bhanuteja.android.com.popularmovies.UI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import bhanuteja.android.com.popularmovies.R;
import bhanuteja.android.com.popularmovies.Utils.Movies_JsonUtil;
import bhanuteja.android.com.popularmovies.Utils.NetworkUtils;

public class Movie_Details extends AppCompatActivity {
    NetworkUtils utils;
    String MovieID;
    TextView movie_name,movie_synopsis,movie_rating,movie_date,nodetails;
    ImageView poster;
    String movie_poster_url;
    LinearLayout linearLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Bundle bundle = getIntent().getExtras();
        MovieID= bundle.getString(Movies_JsonUtil.MOVIE_ID);
        utils = new NetworkUtils();
        movie_name= (TextView) findViewById(R.id.movie_name);
        movie_synopsis= (TextView) findViewById(R.id.movie_synopsis);
        movie_rating= (TextView) findViewById(R.id.movie_rating);
        movie_date= (TextView) findViewById(R.id.movie_date);
        nodetails= (TextView) findViewById(R.id.no_details);
        poster = (ImageView) findViewById(R.id.movie_details_poster);
        linearLayout= (LinearLayout) findViewById(R.id.linear_layout);
        progressBar = (ProgressBar) findViewById(R.id.progess_bar);
        StartFetching();

    }

    private void StartFetching(){
        URL url = utils.build_Details_Url(MovieID);
        new GetDetails().execute(url);

    }

    private class GetDetails extends AsyncTask<URL,Void,String>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(URL... params) {
            URL url = params[0];
            try {
                String jsondata = utils.GetDetailsJson(url);
                return jsondata;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            progressBar.setVisibility(View.GONE);

            if (json==null){
                linearLayout.setVisibility(View.INVISIBLE);
                nodetails.setVisibility(View.VISIBLE);
            }
            try{
                JSONObject jsonObject = new JSONObject(json);
                movie_name.setText(jsonObject.getString(Movies_JsonUtil.Movie_Title));
                movie_poster_url = Movies_JsonUtil.POSTER_BASE_URL+jsonObject.getString(Movies_JsonUtil.POSTER_URL_NAME);
                Picasso.with(getApplicationContext()).load(movie_poster_url).into(poster);
                movie_synopsis.setText(jsonObject.getString(Movies_JsonUtil.Movie_Synopsis));
                movie_rating.setText(jsonObject.getString(Movies_JsonUtil.Movie_User_Rating));
                movie_date.setText(jsonObject.getString(Movies_JsonUtil.Movie_Release_Date));
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

}
