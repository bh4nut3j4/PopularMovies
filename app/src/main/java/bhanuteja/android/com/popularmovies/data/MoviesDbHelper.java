package bhanuteja.android.com.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 5/30/17.
 */

public class MoviesDbHelper extends SQLiteOpenHelper{

    private static final String DbName="moviesDb.db";

    private static final int Db_Version=1;

    public MoviesDbHelper(Context context){
        super(context,DbName,null,Db_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE ="create table "+MoviesContract.MoviesEntry.TABLE_NAME +" ( " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID+" INTEGER UNIQUE , "+
                MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME+" TEXT NOT NULL , "+
                MoviesContract.MoviesEntry.COLUMN_POSTER_URL+" TEXT NOT NULL , "+
                MoviesContract.MoviesEntry.COLUMN_DATE+" TEXT , "+
                MoviesContract.MoviesEntry.COLUMN_RATING+" TEXT )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);

    }
}
