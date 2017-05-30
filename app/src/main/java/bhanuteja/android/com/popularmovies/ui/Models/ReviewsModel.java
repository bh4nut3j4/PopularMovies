package bhanuteja.android.com.popularmovies.ui.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 5/29/17.
 */

public class ReviewsModel implements Parcelable{
    public String review_ID;
    public String author;
    public String content;

    public ReviewsModel(){

    }

    public ReviewsModel(Parcel in) {
        review_ID = in.readString();
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<ReviewsModel> CREATOR = new Creator<ReviewsModel>() {
        @Override
        public ReviewsModel createFromParcel(Parcel in) {
            return new ReviewsModel(in);
        }

        @Override
        public ReviewsModel[] newArray(int size) {
            return new ReviewsModel[size];
        }
    };

    public String getReview_ID() {
        return review_ID;
    }

    public void setReview_ID(String review_ID) {
        this.review_ID = review_ID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(review_ID);
        dest.writeString(author);
        dest.writeString(content);
    }
}
