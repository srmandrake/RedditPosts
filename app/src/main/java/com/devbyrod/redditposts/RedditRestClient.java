package com.devbyrod.redditposts;

import android.app.Activity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodrigo on 12/3/2014.
 */
public class RedditRestClient {

    private IRedditRestClient mActivity;

    public RedditRestClient( Activity activity ){

        try {

            mActivity = (IRedditRestClient)activity;
        } catch (ClassCastException e) {

            throw new ClassCastException(activity.toString()
                    + " must implement processPosts()");
        }
    }

    public void getRedditPosts( String searchCriteria ) throws JSONException{

        RedditHttpClient.get( RedditHttpClient.GET_SUBREDDITS + searchCriteria.trim() + "/.json", new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                int x = 0;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject obj = response.getJSONObject("data");
                    JSONArray children = obj.getJSONArray("children");

                    List<RedditPost> posts = new ArrayList<RedditPost>();

                    for( int i = 0; i < children.length(); i++ ){

                        JSONObject post = children.getJSONObject( i ).getJSONObject("data");
                        posts.add( new RedditPost( post.getString("author"), post.getString("title"), post.getString("thumbnail") ) );
                    }

                    if(mActivity != null ){

                        mActivity.processPosts( posts );
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public interface IRedditRestClient {
        // TODO: Update argument type and name
        public void processPosts( List<RedditPost> posts );
    }
}

class RedditHttpClient{

    private static final String BASE_URL = "http://www.reddit.com/";
    public static final String SUBREDDITS_LIST = "";
    public static final String GET_SUBREDDITS = "r/";

    private static AsyncHttpClient mClient = new AsyncHttpClient();

    public static void get( String url, AsyncHttpResponseHandler responseHandler ){

        String finalUrl = BASE_URL + url;
        mClient.get( finalUrl, responseHandler );
    }
}

