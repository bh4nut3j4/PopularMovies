package bhanuteja.android.com.popularmovies.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import bhanuteja.android.com.popularmovies.R;
import bhanuteja.android.com.popularmovies.data.MoviesContract;
import bhanuteja.android.com.popularmovies.ui.Activities.MovieDetailsActivity;
import bhanuteja.android.com.popularmovies.utils.Connection;
import bhanuteja.android.com.popularmovies.utils.MoviesJsonUtil;

/**
 * Created by root on 5/30/17.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoritesViewHolder> {
    Context context;
    Cursor cursor;
    Connection connection;

    public FavoriteAdapter(Context context) {
        this.context=context;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_model,parent,false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {

        cursor.moveToPosition(position);
        String iD =cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID));
        String name = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME));
        String url = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_URL));
        String date = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_DATE));
        String rating = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RATING));
        holder.id.setText(iD);
        Picasso.with(context).load(url).placeholder(R.drawable.image_placeholder).into(holder.poster);
        holder.name.setText(name);
        holder.date.setText(date);
        holder.rating.setText(rating);
    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }


    class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView poster;
        TextView id,name,date,rating;
        FavoritesViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.fav_img);
            id= (TextView) itemView.findViewById(R.id.fav_id);
            name= (TextView) itemView.findViewById(R.id.fav_movie_name);
            date= (TextView) itemView.findViewById(R.id.fav_date);
            rating= (TextView) itemView.findViewById(R.id.fav_rating);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            connection = new Connection(context);
            if (connection.isInternet()){
                TextView ID= (TextView) v.findViewById(R.id.fav_id);
                String movieid = ID.getText().toString();
                Intent intent = new Intent(v.getContext(),MovieDetailsActivity.class);
                intent.putExtra(MoviesJsonUtil.MOVIE_ID,movieid);
                v.getContext().startActivity(intent);
            }else {
                Toast.makeText(v.getContext(),R.string.no_internet,Toast.LENGTH_SHORT).show();
            }

        }
    }

    public Cursor swapCursor(Cursor c) {
        if (cursor == c) {
            return null;
        }
        Cursor temp = cursor;
        this.cursor = c;
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

}
