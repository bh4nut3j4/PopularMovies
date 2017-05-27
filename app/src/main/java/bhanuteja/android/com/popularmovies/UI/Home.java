package bhanuteja.android.com.popularmovies.UI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bhanuteja.android.com.popularmovies.R;
import bhanuteja.android.com.popularmovies.Utils.Movies_JsonUtil;
import bhanuteja.android.com.popularmovies.Utils.NetworkUtils;

public class Home extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    Movie_Adapter adapter;
    List<Movie_Model> list;
    ProgressBar progressBar;
    public static final int POPULAR=0;
    public static final int TOP_RATED=1;
    NetworkUtils utils;
    Movies_JsonUtil jsonUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        list = new ArrayList<>();
        gridLayoutManager  = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        utils= new NetworkUtils();
        jsonUtil = new Movies_JsonUtil();
        FetchDetails(POPULAR);
    }

    private void FetchDetails(int id){
        URL url = utils.buildURL(id);
        new GetMoviePosters().execute(url);
    }

    private class GetMoviePosters extends AsyncTask<URL,Void,JSONArray>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONArray doInBackground(URL... params) {
            URL url = params[0];
            try {
                String jsondata = utils.GetMoviesJson(url);
                try {
                    return jsonUtil.ExtractJson(jsondata);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonarray) {
            if (jsonarray==null){
                list=null;
            }else {
                int array_length = jsonarray.length();
                for (int i=0;i<=array_length;i++){
                    Movie_Model model = new Movie_Model();
                    JSONObject movie_object = null;
                    try {
                        movie_object = jsonarray.getJSONObject(i);
                        model.setPoster_url(Movies_JsonUtil.POSTER_BASE_URL+movie_object.getString(Movies_JsonUtil.POSTER_URL_NAME));
                        model.setMovie_ID(movie_object.getString(Movies_JsonUtil.MOVIE_ID));
                        list.add(model);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            progressBar.setVisibility(View.INVISIBLE);
            if (list==null){

            }else {
                adapter = new Movie_Adapter(list,Home.this);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.most_popular){
            list.clear();
            recyclerView.removeAllViews();
            FetchDetails(POPULAR);
        }else if (id==R.id.top_rated){
            list.clear();
            recyclerView.removeAllViews();
            FetchDetails(TOP_RATED);
        }
        return super.onOptionsItemSelected(item);
    }
}
