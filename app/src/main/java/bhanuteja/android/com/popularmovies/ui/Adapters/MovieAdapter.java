package bhanuteja.android.com.popularmovies.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bhanuteja.android.com.popularmovies.R;
import bhanuteja.android.com.popularmovies.ui.Activities.MovieDetailsActivity;
import bhanuteja.android.com.popularmovies.ui.Models.MovieModel;
import bhanuteja.android.com.popularmovies.utils.MoviesJsonUtil;

/**
 * Created by root on 5/26/17.
 */

public  class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Movie_ViewHolder> {
    ArrayList<MovieModel> models;
    Context context;

    public MovieAdapter(ArrayList<MovieModel> models, Context context) {
        this.models=models;
        this.context=context;
    }


    @Override
    public Movie_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_poster_model,parent,false);
        return new Movie_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Movie_ViewHolder holder, int position) {
        MovieModel movie_model = models.get(position);
        holder.id.setText(movie_model.getMovieID());
        Picasso.with(context).load(movie_model.getPosterUrl()).placeholder(R.drawable.image_placeholder).into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


     class Movie_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView poster;
        TextView id;
        Movie_ViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.movie_poster_thumb);
            id= (TextView) itemView.findViewById(R.id.movie_id);
            itemView.setOnClickListener(this);
        }
         @Override
         public void onClick(View v) {
             TextView ID= (TextView) v.findViewById(R.id.movie_id);
             String movieid = ID.getText().toString();
             Intent intent = new Intent(v.getContext(),MovieDetailsActivity.class);
             intent.putExtra(MoviesJsonUtil.MOVIE_ID,movieid);
             v.getContext().startActivity(intent);
         }
     }


}
