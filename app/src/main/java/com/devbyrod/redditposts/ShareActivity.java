package com.devbyrod.redditposts;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import java.util.List;


public class ShareActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        final RedditPost post = intent.getParcelableExtra( Constants.REDDIT_POST_EXTRA );

        Button btnEmail = (Button) findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                share( "text/html", post );

                finish();
            }
        });

        Button btnSms = (Button) findViewById(R.id.btnSms);
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                share( "text/plain", post );

                finish();
            }
        });
    }

    private void share( String type, RedditPost post ){

        String shareMethod = type.equals("text/html") ? "com.android.email.activity.MessageCompose" : "com.android.mms.ui.ConversationComposer";
        Intent intent = this.setIntent(type, post);

        PackageManager pm = getApplicationContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities( intent, 0 );

        for( ResolveInfo app: activityList ){

            if( app.activityInfo.name.contains( shareMethod ) ){

                ActivityInfo activityInfo = app.activityInfo;
                ComponentName name = new ComponentName( activityInfo.applicationInfo.packageName, activityInfo.name );
                intent.setComponent( name );
                startActivity( intent );
                return;
            }
        }
    }

    private Intent setIntent( String type, RedditPost post ){

        Intent intent = new Intent( Intent.ACTION_SEND );
        intent.setType( type );

        if( type.equals( "text/html" ) ){

            intent.putExtra( Intent.EXTRA_EMAIL, "" );
            intent.putExtra( Intent.EXTRA_SUBJECT, getString(R.string.title_post_share) );
            intent.putExtra( Intent.EXTRA_TEXT, Html.fromHtml( "<img src='" + post.getThumbnail() + "' style='float:left; margin-right:15px' />" +
                                                                "<div style='float:left'><h3 style='margin:0'>" + post.getAuthor() + "</h3>" +
                                                                "<p style='margin:0'>" + post.getTitle() + "</p></div>" ) );

            return intent;
        }

        intent.putExtra( Intent.EXTRA_TEXT, getString(R.string.title_post_share) + "\n" + post.getAuthor() + "\n" + post.getTitle() );
        return intent;
    }
}
