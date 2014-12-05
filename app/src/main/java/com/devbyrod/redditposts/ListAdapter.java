package com.devbyrod.redditposts;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Rodrigo on 12/3/2014.
 */
public class ListAdapter extends ArrayAdapter<RedditPost> {

    private Context mContext;
    private List<RedditPost> mPosts;
    private Typeface font;

    public ListAdapter( List<RedditPost> posts, Context context){

        super( context, R.layout.layout_row, posts );

        this.mContext = context;
        this.mPosts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        PostHolder postHolder = new PostHolder();

        //optimize inflating process
        if( convertView == null ){

            font = Typeface.createFromAsset( mContext.getAssets(), "fonts/bebasneue.ttf");

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = inflater.inflate( R.layout.layout_row, null );

            postHolder.txtAuthor = (TextView) view.findViewById( R.id.txtAuthor );
            postHolder.txtAuthor.setTypeface( font );
            postHolder.txtTitle = (TextView) view.findViewById( R.id.txtTitle );
            postHolder.imgThumbnail = (ImageView) view.findViewById( R.id.imgThumbnail );

            view.setTag( postHolder );
        }
        else{

            postHolder = (PostHolder) view.getTag();
        }

        RedditPost post = this.mPosts.get( position );
        postHolder.txtTitle.setText( post.getTitle() );
        postHolder.txtAuthor.setText( post.getAuthor() );
        Picasso.with( mContext )
                .load( post.getThumbnail() )
                .resize( 90, 90 )
                .into( postHolder.imgThumbnail );

        return view;
    }

    //Private, static class to implement View-Holder pattern
    // to avoid consuming resources after inflating the view on each call
    private static class PostHolder{

        public TextView txtTitle;
        public TextView txtAuthor;
        public ImageView imgThumbnail;
    }
}
