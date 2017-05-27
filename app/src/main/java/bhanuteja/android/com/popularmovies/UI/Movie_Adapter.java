package bhanuteja.android.com.popularmovies.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import bhanuteja.android.com.popularmovies.R;
import bhanuteja.android.com.popularmovies.Utils.Movies_JsonUtil;

/**
 * Created by root on 5/26/17.
 */

public  class Movie_Adapter extends RecyclerView.Adapter<Movie_Adapter.Movie_ViewHolder> {
    List<Movie_Model> models;
    Context context;

    public Movie_Adapter(List<Movie_Model> models, Context context) {
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
        Movie_Model movie_model = models.get(position);
        holder.id.setText(movie_model.getMovie_ID());
        Picasso.with(context).load(movie_model.getPoster_url()).into(holder.poster);
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
             Intent intent = new Intent(v.getContext(),Movie_Details.class);
             intent.putExtra(Movies_JsonUtil.MOVIE_ID,movieid);
             v.getContext().startActivity(intent);
         }
     }


}
