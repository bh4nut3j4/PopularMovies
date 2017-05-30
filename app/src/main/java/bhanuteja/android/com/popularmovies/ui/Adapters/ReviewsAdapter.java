package bhanuteja.android.com.popularmovies.ui.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bhanuteja.android.com.popularmovies.R;
import bhanuteja.android.com.popularmovies.ui.Models.ReviewsModel;

/**
 * Created by root on 5/29/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    ArrayList<ReviewsModel> list;
    Context context;
    public ReviewsAdapter(ArrayList<ReviewsModel> list,Context context){
        this.context=context;
        this.list=list;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_model,parent,false);
        return new ReviewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        ReviewsModel model = list.get(position);
        holder.author.setText(model.getAuthor());
        holder.content.setText(model.getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        TextView author,content;
        public ReviewsViewHolder(View itemView) {
            super(itemView);
            author= (TextView) itemView.findViewById(R.id.reviews_author);
            content= (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}
