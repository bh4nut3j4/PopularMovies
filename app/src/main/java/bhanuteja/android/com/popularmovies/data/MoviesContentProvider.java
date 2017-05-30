package bhanuteja.android.com.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by root on 5/30/17.
 */

public class MoviesContentProvider extends ContentProvider {

    public static final int MOVIES=100;
    public static final int MOVIES_WITH_ID=101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);
        return uriMatcher;
    }

    private MoviesDbHelper dbHelper;
    @Override
    public boolean onCreate() {
        Context context= getContext();
        dbHelper = new MoviesDbHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        int match  = sUriMatcher.match(uri);
        Cursor dataCursor;
        switch (match){
            case MOVIES:
                dataCursor = sqLiteDatabase.query(MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }

        dataCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return dataCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MOVIES:
                String valId = String.valueOf(values.get(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID));
                Cursor cursor = sqLiteDatabase.rawQuery("select * from "+ MoviesContract.MoviesEntry.TABLE_NAME+
                        " where "+ MoviesContract.MoviesEntry.COLUMN_MOVIE_ID+" = "+ valId,null);
                int count =cursor.getCount();
                if (count==0){
                    long id =sqLiteDatabase.insert(MoviesContract.MoviesEntry.TABLE_NAME,null,values);
                    if (id>0){
                        returnUri= ContentUris.withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);
                    }else {
                        Log.d("CONTENT_PROVIDER","Insert->Failed to insert row into "+uri);
                        throw new android.database.SQLException("Failed to Add");
                    }
                    cursor.close();
                    break;
                }else {
                    Log.d("CONTENT_PROVIDER","Insert->Already added to favorites "+uri);
                    throw new android.database.SQLException("Already added to favorites");
                }
            default:
                Log.d("CONTENT_PROVIDER","Insert->Unsupported");
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int deleted;
        switch (match){
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                deleted = sqLiteDatabase.delete(MoviesContract.MoviesEntry.TABLE_NAME,"id=?",new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (deleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
