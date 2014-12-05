package com.devbyrod.redditposts;

import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements MainFragment.OnFragmentInteractionListener,
                                                                RedditRestClient.IRedditRestClient,
                                                                DisplayPostsFragment.OnFragmentInteractionListener{

    private ListView mListView;
    private ListAdapter mListAdapter;
    private RedditRestClient mRedditRestClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .add(R.id.container, new DisplayPostsFragment())
                    .commit();
        }

        mRedditRestClient = new RedditRestClient( this );

        try {

            mRedditRestClient.getRedditPosts(Constants.INITIAL_SEARCH_CRITERIA);

        }
        catch (JSONException e) {

            e.printStackTrace();
        }

        handleIntent( getIntent() );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            MenuItem menuItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) menuItem.getActionView();

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        setIntent( intent );
        handleIntent( intent );
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void processPosts( List<RedditPost> posts ) {

        if( mListView == null ) {

            mListView = (ListView) findViewById( R.id.listPosts );
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    RedditPost post = (RedditPost) parent.getItemAtPosition( position );

                    Intent intent = new Intent( getApplicationContext(), ShareActivity.class );
                    intent.putExtra( Constants.REDDIT_POST_EXTRA, post );
                    startActivity( intent );
                }
            });
        }

        if( mListAdapter == null ){

            mListAdapter = new ListAdapter( new ArrayList<RedditPost>(), this );

            mListView.setAdapter( mListAdapter );
        }

        mListAdapter.clear();

        //could have used adapter.addAll() if API level >= 11 :/
        for( RedditPost post : posts ){

            mListAdapter.add( post );
        }
    }

    private void handleIntent( Intent intent ) {

        if( intent.getAction().equals( Intent.ACTION_SEARCH  ) ){

            String searchCriteria = intent.getStringExtra( SearchManager.QUERY );

            try {
                mRedditRestClient.getRedditPosts( searchCriteria );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
