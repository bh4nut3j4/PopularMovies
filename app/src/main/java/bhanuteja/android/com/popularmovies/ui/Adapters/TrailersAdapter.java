package bhanuteja.android.com.popularmovies.ui.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bhanuteja.android.com.popularmovies.R;
import bhanuteja.android.com.popularmovies.ui.Models.TrailersModel;

/**
 * Created by root on 5/29/17.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    ArrayList<TrailersModel> list;
    Context context;
    public TrailersAdapter(ArrayList<TrailersModel> list,Context context){
        this.context=context;
        this.list=list;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_model,parent,false);
        return new TrailerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        TrailersModel model = list.get(position);
        holder.trailername.setText(model.getTrailerName());
        holder.trailerkey.setText(model.getTrailerUrl());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView trailername,trailerkey;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailername = (TextView) itemView.findViewById(R.id.trailer_name);
            trailerkey= (TextView) itemView.findViewById(R.id.trailer_key);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TextView trailerlink = (TextView) v.findViewById(R.id.trailer_key);
            String id = trailerlink.getText().toString();

            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            try {
                v.getContext().startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                v.getContext().startActivity(webIntent);
            }
        }
    }
}
