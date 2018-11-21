package com.example.breezil.favnews.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleResult implements Parcelable {

    @SerializedName("status")
    private String status;
    @SerializedName("totalResults")
    private int totalResults;
    @SerializedName("message")
    private String message;
    @SerializedName("articles")
    private List<Articles> articles;

    public ArticleResult() {}

    protected ArticleResult(Parcel in) {
        status = in.readString();
        totalResults = in.readInt();
        message = in.readString();
    }

    public static final Creator<ArticleResult> CREATOR = new Creator<ArticleResult>() {
        @Override
        public ArticleResult createFromParcel(Parcel in) {
            return new ArticleResult(in);
        }

        @Override
        public ArticleResult[] newArray(int size) {
            return new ArticleResult[size];
        }
    };


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public List<Articles> getArticles() {
        return articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeInt(totalResults);
        dest.writeString(message);
    }
}
