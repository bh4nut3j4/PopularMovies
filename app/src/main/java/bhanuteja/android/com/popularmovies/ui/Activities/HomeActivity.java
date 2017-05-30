package bhanuteja.android.com.popularmovies.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import bhanuteja.android.com.popularmovies.R;
import bhanuteja.android.com.popularmovies.ui.Adapters.MovieAdapter;
import bhanuteja.android.com.popularmovies.ui.Models.MovieModel;
import bhanuteja.android.com.popularmovies.utils.Connection;
import bhanuteja.android.com.popularmovies.utils.MoviesJsonUtil;
import bhanuteja.android.com.popularmovies.utils.NetworkUtils;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONArray> {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    MovieAdapter adapter;
    ArrayList<MovieModel> list;
    ProgressBar progressBar;
    public static final int POPULAR = 0;
    public static final int TOP_RATED = 1;
    NetworkUtils utils;
    MoviesJsonUtil jsonUtil;
    TextView nodetails;
    private static final String LIST_CONSTANT = "list";
    Connection connection;
    private static final int THUMNAILS_CALL = 11;
    private static final String THUMNAILS_URL_EXTRA = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        nodetails = (TextView) findViewById(R.id.nodetails);
        list = new ArrayList<>();
        final int columns = getResources().getInteger(R.integer.gallery_columns);
        gridLayoutManager = new GridLayoutManager(this, columns);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        utils = new NetworkUtils();
        jsonUtil = new MoviesJsonUtil();
        connection = new Connection(this);
        if (savedInstanceState != null) {
            getSupportLoaderManager().initLoader(THUMNAILS_CALL, null, this);
        } else if (connection.isInternet()) {
            FetchDetails(POPULAR);
        } else {
            Snackbar.make(recyclerView,R.string.no_internet,Snackbar.LENGTH_SHORT).show();
            nodetails.setVisibility(View.VISIBLE);
        }
    }

    protected void onSaveInstanceState(Bundle state) {
        state.putParcelableArrayList(LIST_CONSTANT, list);
        super.onSaveInstanceState(state);
    }

    private void FetchDetails(int id) {
        URL url = utils.buildURL(id);
        Bundle queryBundle = new Bundle();
        queryBundle.putString(THUMNAILS_URL_EXTRA, url.toString());
        LoaderManager loaderManager = getSupportLoaderManager();
        android.support.v4.content.Loader<JSONArray> getThumNails = loaderManager.getLoader(THUMNAILS_CALL);
        if (getThumNails == null) {
            loaderManager.initLoader(THUMNAILS_CALL, queryBundle, this);
        } else {
            loaderManager.restartLoader(THUMNAILS_CALL, queryBundle, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.most_popular) {
            if (list != null) {
                list.clear();
                recyclerView.removeAllViews();
                FetchDetails(POPULAR);
            } else {
                Snackbar.make(recyclerView,R.string.no_internet,Snackbar.LENGTH_SHORT).show();
            }
        } else if (id == R.id.top_rated) {
            if (list != null) {
                list.clear();
                recyclerView.removeAllViews();
                FetchDetails(TOP_RATED);
            } else {
                Snackbar.make(recyclerView,R.string.no_internet,Snackbar.LENGTH_SHORT).show();
            }
        } else if (id==R.id.favorites) {
           startActivity(new Intent(getApplicationContext(),FavoritesActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public android.support.v4.content.Loader<JSONArray> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<JSONArray>(this) {
            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public JSONArray loadInBackground() {
                String thumUrl = args.getString(THUMNAILS_URL_EXTRA);
                if (thumUrl == null || thumUrl.equals("")) {
                    return null;
                }
                try {
                    URL url = new URL(thumUrl);
                    String jsondata = utils.GetMoviesJson(url);
                    try {
                        return jsonUtil.ExtractJson(jsondata);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<JSONArray> loader, JSONArray data) {
        if (data == null) {
            list = null;
        } else {
            list.clear();
            int array_length = data.length();
            for (int i = 0; i <= array_length; i++) {
                MovieModel model = new MovieModel();
                JSONObject movie_object = null;
                try {
                    movie_object = data.getJSONObject(i);
                    model.setPosterUrl(MoviesJsonUtil.POSTER_BASE_URL + movie_object.getString(MoviesJsonUtil.POSTER_URL_NAME));
                    model.setMovieID(movie_object.getString(MoviesJsonUtil.MOVIE_ID));
                    list.add(model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        progressBar.setVisibility(View.INVISIBLE);
        if (list == null) {
            nodetails.setVisibility(View.VISIBLE);
        } else {
            adapter = new MovieAdapter(list, HomeActivity.this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<JSONArray> loader) {

    }

}
