package bhanuteja.android.com.popularmovies.UI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import bhanuteja.android.com.popularmovies.R;
import bhanuteja.android.com.popularmovies.Utils.Connection;
import bhanuteja.android.com.popularmovies.Utils.Movies_JsonUtil;
import bhanuteja.android.com.popularmovies.Utils.NetworkUtils;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    MovieAdapter adapter;
    ArrayList<MovieModel> list = new ArrayList<>();
    ProgressBar progressBar;
    public static final int POPULAR=0;
    public static final int TOP_RATED=1;
    NetworkUtils utils;
    Movies_JsonUtil jsonUtil;
    TextView nodetails;
    private static final String LIST_CONSTANT = "list";
    Parcelable listState;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        nodetails = (TextView) findViewById(R.id.nodetails);
        final int columns = getResources().getInteger(R.integer.gallery_columns);
        gridLayoutManager  = new GridLayoutManager(this,columns);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        utils= new NetworkUtils();
        jsonUtil = new Movies_JsonUtil();
        connection = new Connection(this);
        if (connection.isInternet()){
            if (savedInstanceState!=null){
                list =savedInstanceState.getParcelableArrayList(LIST_CONSTANT);
                adapter = new MovieAdapter(list,HomeActivity.this);
                recyclerView.setAdapter(adapter);
            }else {
                FetchDetails(POPULAR);
            }
        }else {
            Toast.makeText(getApplicationContext(),R.string.no_internet,Toast.LENGTH_SHORT).show();
            nodetails.setVisibility(View.VISIBLE);
        }


    }

    protected void onSaveInstanceState(Bundle state) {
        listState = recyclerView.getLayoutManager().onSaveInstanceState();
        state.putParcelableArrayList(LIST_CONSTANT, list);
        super.onSaveInstanceState(state);
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
                    MovieModel model = new MovieModel();
                    JSONObject movie_object = null;
                    try {
                        movie_object = jsonarray.getJSONObject(i);
                        model.setPosterUrl(Movies_JsonUtil.POSTER_BASE_URL+movie_object.getString(Movies_JsonUtil.POSTER_URL_NAME));
                        model.setMovieID(movie_object.getString(Movies_JsonUtil.MOVIE_ID));
                        list.add(model);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            progressBar.setVisibility(View.INVISIBLE);
            if (list==null){
                nodetails.setVisibility(View.VISIBLE);
            }else {
                adapter = new MovieAdapter(list,HomeActivity.this);
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
            if (list!=null){
                list.clear();
                recyclerView.removeAllViews();
                FetchDetails(POPULAR);
            }else {
                Toast.makeText(getApplicationContext(),R.string.no_internet,Toast.LENGTH_SHORT).show();
            }
        }else if (id==R.id.top_rated){
            if (list!=null){
                list.clear();
                recyclerView.removeAllViews();
                FetchDetails(TOP_RATED);
            }else {
                Toast.makeText(getApplicationContext(),R.string.no_internet,Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
