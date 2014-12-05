package com.devbyrod.redditposts;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rodrigo on 12/3/2014.
 */
public class RedditPost implements Parcelable {

    private String mTitle;
    private String mAuthor;
    private String mThumbnail;

    RedditPost( String author, String title, String thumbnail ){

        mAuthor = "@" + author;
        mTitle = title;
        mThumbnail = thumbnail;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString( mAuthor );
        dest.writeString( mTitle );
        dest.writeString( mThumbnail );
    }

    public static final Creator<RedditPost> CREATOR = new Creator<RedditPost>() {
        @Override
        public RedditPost createFromParcel(Parcel source) {
            return new RedditPost( source );
        }

        @Override
        public RedditPost[] newArray(int size) {
            return new RedditPost[size];
        }
    };

    private RedditPost( Parcel in ){

        mAuthor = in.readString();
        mTitle = in.readString();
        mThumbnail = in.readString();
    }
}
