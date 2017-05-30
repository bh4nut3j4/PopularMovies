package bhanuteja.android.com.popularmovies.ui.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import bhanuteja.android.com.popularmovies.R;
import bhanuteja.android.com.popularmovies.data.MoviesContract;
import bhanuteja.android.com.popularmovies.ui.Adapters.ReviewsAdapter;
import bhanuteja.android.com.popularmovies.ui.Adapters.TrailersAdapter;
import bhanuteja.android.com.popularmovies.ui.Models.ReviewsModel;
import bhanuteja.android.com.popularmovies.ui.Models.TrailersModel;
import bhanuteja.android.com.popularmovies.utils.Connection;
import bhanuteja.android.com.popularmovies.utils.MoviesJsonUtil;
import bhanuteja.android.com.popularmovies.utils.NetworkUtils;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    NetworkUtils utils;
    String MovieID;
    TextView movie_name, movie_synopsis, movie_rating, movie_date, nodetails,trailerNotAvailable,reviewsNotAvailable;
    ImageView poster;
    static String movie_poster_url;
    FrameLayout linearLayout;
    ProgressBar progressBar,trailerprogress,reviewsprogress;
    private static final String movieNameKey = "mNK", movieUrlKey = "mUK", movieSynopsisKey = "mSK", movieRatingKey = "mRK", movieDateKey = "mDK";
    Connection connection;
    private static final String MOVIE_DETAILS_STRING_NAME = "details";
    private static final int MOVIE_DETAILS_LOADER_INT = 22;
    private static final String MOVIE_TRAILERS_STRING_NAME= "trailer";
    private static final int MOVIE_TRAILERS_LOADER_ID =23;
    private static final String TRAILERS_LIST_CONSTANT="trailers";
    private static final String MOVIE_REVIEWS_STRING_NAME= "reviews";
    private static final int MOVIE_REVIEWS_LOADER_ID =24;
    private static final String REVIEWS_LIST_CONSTANT="reviews";
    RecyclerView trailersRecyclerView,reviewsRecyclerView;
    CardView c1;
    MoviesJsonUtil moviesJsonUtil;
    ArrayList<TrailersModel> trailersModels = new ArrayList<>();
    ArrayList<ReviewsModel> reviewsModels= new ArrayList<>();
    TrailersAdapter trailersAdapter;
    ReviewsAdapter reviewsAdapter;
    Button addToFavories;
    FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Bundle bundle = getIntent().getExtras();
        MovieID = bundle.getString(MoviesJsonUtil.MOVIE_ID);
        utils = new NetworkUtils();
        movie_name = (TextView) findViewById(R.id.movie_name);
        movie_synopsis = (TextView) findViewById(R.id.movie_synopsis);
        movie_rating = (TextView) findViewById(R.id.movie_rating);
        movie_date = (TextView) findViewById(R.id.movie_date);
        nodetails = (TextView) findViewById(R.id.no_details);
        poster = (ImageView) findViewById(R.id.movie_details_poster);
        linearLayout = (FrameLayout) findViewById(R.id.linear_layout);
        progressBar = (ProgressBar) findViewById(R.id.progess_bar);
        trailersRecyclerView= (RecyclerView) findViewById(R.id.trailers_recyclerview);
        reviewsRecyclerView= (RecyclerView) findViewById(R.id.reviews_recyclerview);
        trailerprogress= (ProgressBar) findViewById(R.id.trailers_progressbar);
        reviewsprogress= (ProgressBar) findViewById(R.id.reviews_progressbar);
        trailerNotAvailable= (TextView) findViewById(R.id.trailers_not_available_text);
        reviewsNotAvailable= (TextView) findViewById(R.id.reviews_not_available_text);
        trailersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        c1= (CardView) findViewById(R.id.c1);
        frameLayout= (FrameLayout) findViewById(R.id.f1);
        addToFavories= (Button) findViewById(R.id.addToFavorites);
        addToFavories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,MovieID);
                values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME,movie_name.getText().toString());
                values.put(MoviesContract.MoviesEntry.COLUMN_POSTER_URL,movie_poster_url);
                values.put(MoviesContract.MoviesEntry.COLUMN_DATE,movie_date.getText().toString());
                values.put(MoviesContract.MoviesEntry.COLUMN_RATING,movie_rating.getText().toString());
                try{
                    Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI,values);
                    if (uri!=null){
                        Snackbar.make(linearLayout,"Added to favorites",Snackbar.LENGTH_LONG).
                                setAction("VIEW", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(getApplicationContext(),FavoritesActivity.class));
                                        finish();
                                    }
                                }).show();
                    }
                }catch (Exception e){
                    Snackbar.make(linearLayout,"Already Added to favorites",Snackbar.LENGTH_LONG).
                            setAction("VIEW", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getApplicationContext(),FavoritesActivity.class));
                                    finish();
                                }
                            }).show();
                }
            }
        });
        moviesJsonUtil = new MoviesJsonUtil();
        connection = new Connection(this);
        if (savedInstanceState != null) {
            getSupportLoaderManager().initLoader(MOVIE_DETAILS_LOADER_INT, null, this);
            getSupportLoaderManager().initLoader(MOVIE_TRAILERS_LOADER_ID, null, this);
            getSupportLoaderManager().initLoader(MOVIE_REVIEWS_LOADER_ID, null, this);
        } else if (connection.isInternet()) {
            StartFetching();
        } else{
            Snackbar.make(linearLayout,R.string.no_internet,Snackbar.LENGTH_SHORT).show();
            linearLayout.setVisibility(View.INVISIBLE);
            nodetails.setVisibility(View.VISIBLE);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(movieNameKey, movie_name.getText().toString());
        outState.putString(movieUrlKey, movie_poster_url);
        outState.putString(movieSynopsisKey, movie_synopsis.getText().toString());
        outState.putString(movieRatingKey, movie_rating.getText().toString());
        outState.putString(movieDateKey, movie_date.getText().toString());
        outState.putParcelableArrayList(TRAILERS_LIST_CONSTANT,trailersModels);
        outState.putParcelableArrayList(REVIEWS_LIST_CONSTANT,reviewsModels);
        super.onSaveInstanceState(outState);
    }

    private void StartFetching() {

        URL detailsUrl = utils.getURLS(0,MovieID);
        Bundle detailsBundle = new Bundle();
        detailsBundle.putString(MOVIE_DETAILS_STRING_NAME, detailsUrl.toString());
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Object> getMovieDetails = loaderManager.getLoader(MOVIE_DETAILS_LOADER_INT);
        if (getMovieDetails == null) {
            loaderManager.initLoader(MOVIE_DETAILS_LOADER_INT, detailsBundle, this);
        } else {
            loaderManager.restartLoader(MOVIE_DETAILS_LOADER_INT, detailsBundle, this);
        }

        LoaderManager trailerLoader =getSupportLoaderManager();
        URL trailerUrl = utils.getURLS(1,MovieID);
        Log.d("URL",trailerUrl.toString());
        Bundle trailerBundle = new Bundle();
        trailerBundle.putString(MOVIE_TRAILERS_STRING_NAME,trailerUrl.toString());
        Loader<Object> getTrailerDetails = trailerLoader.getLoader(MOVIE_TRAILERS_LOADER_ID);
        if (getTrailerDetails == null) {
            trailerLoader.initLoader(MOVIE_TRAILERS_LOADER_ID, trailerBundle, this);
        } else {
            trailerLoader.restartLoader(MOVIE_TRAILERS_LOADER_ID, trailerBundle, this);
        }

        LoaderManager reviewsLoader =getSupportLoaderManager();
        URL reviewsUrl = utils.getURLS(2,MovieID);
        Log.d("URLL",reviewsUrl.toString());
        Bundle reviewsBundle = new Bundle();
        reviewsBundle.putString(MOVIE_REVIEWS_STRING_NAME,reviewsUrl.toString());
        Loader<Object> getReviewsDetails = reviewsLoader.getLoader(MOVIE_REVIEWS_LOADER_ID);
        if (getReviewsDetails == null) {
            reviewsLoader.initLoader(MOVIE_REVIEWS_LOADER_ID, reviewsBundle, this);
        } else {
            reviewsLoader.restartLoader(MOVIE_REVIEWS_LOADER_ID, reviewsBundle, this);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        if (id==MOVIE_DETAILS_LOADER_INT){
            return new AsyncTaskLoader<String>(this) {
                @Override
                protected void onStartLoading() {
                    if (args == null) {
                        return;
                    }
                    frameLayout.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
                @Override
                public String loadInBackground() {
                    String uRL = args.getString(MOVIE_DETAILS_STRING_NAME);
                    if (uRL == null || uRL.equals("")) {
                        return null;
                    }
                    try {
                        URL url = new URL(uRL);
                        try {
                            return utils.GetDetailsJson(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }else if (id== MOVIE_TRAILERS_LOADER_ID){
            return new AsyncTaskLoader<String>(this) {
                @Override
                protected void onStartLoading(){
                    if (args == null) {
                        return;
                    }
                    trailerprogress.setVisibility(View.VISIBLE);
                    forceLoad();
                }
                @Override
                public String loadInBackground() {
                    String uRL = args.getString(MOVIE_TRAILERS_STRING_NAME);
                    if (uRL == null || uRL.equals("")) {
                        return null;
                    }
                    try {
                        URL url = new URL(uRL);
                        try {
                            return utils.GetDetailsJson(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }else if (id== MOVIE_REVIEWS_LOADER_ID){
            return new AsyncTaskLoader<String>(this) {
                @Override
                protected void onStartLoading(){
                    if (args == null) {
                        return;
                    }
                    reviewsprogress.setVisibility(View.VISIBLE);
                    forceLoad();
                }
                @Override
                public String loadInBackground() {
                    String uRL = args.getString(MOVIE_REVIEWS_STRING_NAME);
                    if (uRL == null || uRL.equals("")) {
                        return null;
                    }
                    try {
                        URL url = new URL(uRL);
                        try {
                            return utils.GetDetailsJson(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }
            return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {


        int i =loader.getId();
        if (i==MOVIE_DETAILS_LOADER_INT){
            frameLayout.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            if (data == null) {
                c1.setVisibility(View.GONE);
                nodetails.setVisibility(View.VISIBLE);
            }
            try {
                JSONObject jsonObject = new JSONObject(data);
                movie_name.setText(jsonObject.getString(MoviesJsonUtil.Movie_Title));
                movie_poster_url = MoviesJsonUtil.POSTER_BASE_URL + jsonObject.getString(MoviesJsonUtil.POSTER_URL_NAME);
                Picasso.with(getApplicationContext()).load(movie_poster_url).placeholder(R.drawable.image_placeholder).into(poster);
                movie_synopsis.setText(jsonObject.getString(MoviesJsonUtil.Movie_Synopsis));
                movie_rating.setText(jsonObject.getString(MoviesJsonUtil.Movie_User_Rating));
                movie_date.setText(jsonObject.getString(MoviesJsonUtil.Movie_Release_Date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (i== MOVIE_TRAILERS_LOADER_ID){
            if (data == null) {
                showTrailerError();
            }
            try {
                JSONArray jsonArray = moviesJsonUtil.ExtractJson(data);
                if (jsonArray==null){
                    showTrailerError();
                }
                try{
                    int length =jsonArray.length();
                    for (int j = 0; j<=length; j++){
                        TrailersModel model = new TrailersModel();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = jsonArray.getJSONObject(j);
                            model.setTrailerName(jsonObject.getString(MoviesJsonUtil.Video_Name));
                            model.setTrailerUrl(jsonObject.getString(MoviesJsonUtil.Video_KEY));
                            model.setTrailerID(jsonObject.getString(MoviesJsonUtil.Trailer_ID));
                            trailersModels.add(model);
                            Log.d("MODEL",reviewsModels.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    trailerprogress.setVisibility(View.GONE);
                    if (trailersModels==null){
                        showTrailerError();
                    }else {
                        trailersAdapter = new TrailersAdapter(trailersModels,MovieDetailsActivity.this);
                        trailersRecyclerView.setAdapter(trailersAdapter);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (i== MOVIE_REVIEWS_LOADER_ID){
            if (data == null) {
                showReviewsError();
            }
            try {
                JSONArray jsonArray = moviesJsonUtil.ExtractJson(data);
                if (jsonArray==null){
                    showReviewsError();
                }
                try{
                    int length =jsonArray.length();
                    for (int j = 0; j<=length; j++){
                        ReviewsModel model = new ReviewsModel();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = jsonArray.getJSONObject(j);
                            model.setAuthor(jsonObject.getString(MoviesJsonUtil.Review_Auther));
                            model.setContent(jsonObject.getString(MoviesJsonUtil.Review_Content));
                            model.setReview_ID(jsonObject.getString(MoviesJsonUtil.Review_ID));
                            reviewsModels.add(model);
                            Log.d("MODEL",jsonObject.getString(MoviesJsonUtil.Review_Auther)+jsonObject.getString(MoviesJsonUtil.Review_Content)+
                                    jsonObject.getString(MoviesJsonUtil.Review_ID));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    reviewsprogress.setVisibility(View.GONE);
                    if (reviewsModels==null){
                        showReviewsError();
                    }else {
                        reviewsAdapter=new ReviewsAdapter(reviewsModels,MovieDetailsActivity.this);
                        reviewsRecyclerView.setAdapter(reviewsAdapter);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    public void showTrailerError(){
        trailersRecyclerView.setVisibility(View.GONE);
        trailerNotAvailable.setVisibility(View.VISIBLE);
    }
    public void showReviewsError(){
        reviewsRecyclerView.setVisibility(View.GONE);
        reviewsNotAvailable.setVisibility(View.VISIBLE);
    }

}
