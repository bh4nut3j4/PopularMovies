package bhanuteja.android.com.popularmovies.ui.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 5/29/17.
 */

public class TrailersModel implements Parcelable {
    public String trailerID;
    public String trailerUrl;
    public String trailerName;

    public TrailersModel(){

    }

    public TrailersModel(Parcel in) {
        trailerID = in.readString();
        trailerUrl = in.readString();
        trailerName = in.readString();
    }

    public static final Creator<TrailersModel> CREATOR = new Creator<TrailersModel>() {
        @Override
        public TrailersModel createFromParcel(Parcel in) {
            return new TrailersModel(in);
        }

        @Override
        public TrailersModel[] newArray(int size) {
            return new TrailersModel[size];
        }
    };

    public String getTrailerID() {
        return trailerID;
    }

    public void setTrailerID(String trailerID) {
        this.trailerID = trailerID;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerID);
        dest.writeString(trailerUrl);
        dest.writeString(trailerName);
    }
}
