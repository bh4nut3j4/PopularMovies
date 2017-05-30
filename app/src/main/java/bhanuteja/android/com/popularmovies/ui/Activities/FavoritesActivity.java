package bhanuteja.android.com.popularmovies.ui.Activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import bhanuteja.android.com.popularmovies.R;
import bhanuteja.android.com.popularmovies.data.MoviesContract;
import bhanuteja.android.com.popularmovies.ui.Adapters.FavoriteAdapter;
import bhanuteja.android.com.popularmovies.utils.Connection;

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Connection connection;
    FavoriteAdapter adapter;
    FrameLayout frameLayout;
    private static final int FavLoaderID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        recyclerView= (RecyclerView) findViewById(R.id.fav_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar= (ProgressBar) findViewById(R.id.fav_progressbar);
        frameLayout= (FrameLayout) findViewById(R.id.frame);
        connection =new Connection(this);
        adapter = new FavoriteAdapter(this);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                TextView id = (TextView) viewHolder.itemView.findViewById(R.id.fav_id);
                String iD = id.getText().toString();
                Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(iD).build();
                try{
                    getContentResolver().delete(uri,null,null);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (uri!=null){
                    Snackbar.make(frameLayout,"Removed Successfuly",Snackbar.LENGTH_SHORT).show();
                }
                getSupportLoaderManager().restartLoader(FavLoaderID, null, FavoritesActivity.this);
            }
        }).attachToRecyclerView(recyclerView);

        getSupportLoaderManager().initLoader(FavLoaderID, null, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(FavLoaderID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            @Override
            protected void onStartLoading(){
               progressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }
            @Override
            public Cursor loadInBackground() {
                try{
                    return getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MoviesContract.MoviesEntry.COLUMN_RATING);
                }catch (Exception e){

                    return null;
                }
            }
        };
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data!=null){
            progressBar.setVisibility(View.INVISIBLE);
            adapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
