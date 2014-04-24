/*
 * Copyright 2011, 2012 Sony Corporation
 * Copyright (C) 2012 Sony Mobile Communications AB.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jp.nita.tweetsmallapp;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sony.smallapp.SmallAppWindow;
import com.sony.smallapp.SmallApplication;

public class MainApplication extends SmallApplication {
   
	TextView tx;
	
	@Override
    public void onCreate() {
        super.onCreate();
        setContentView(R.layout.main);
        setTitle(R.string.app_name);

        SmallAppWindow.Attributes attr = getWindow().getAttributes();
        attr.minWidth = 240; /* The minimum width of the application, if it's resizable.*/
        attr.minHeight = 120; /*The minimum height of the application, if it's resizable.*/
        attr.width = 240;  /*The requested width of the application.*/
        attr.height = 120;  /*The requested height of the application.*/
        attr.flags |= SmallAppWindow.Attributes.FLAG_RESIZABLE;   /*Use this flag to enable the application window to be resizable*/
//        attr.flags |= SmallAppWindow.Attributes.FLAG_NO_TITLEBAR;  /*Use this flag to remove the titlebar from the window*/
//        attr.flags |= SmallAppWindow.Attributes.FLAG_HARDWARE_ACCELERATED;  /* Use this flag to enable hardware accelerated rendering*/

        getWindow().setAttributes(attr); /*setting window attributes*/

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String tweet=((EditText)findViewById(R.id.content)).getText().toString();
            	if("".equals(tweet)){
            		Toast.makeText(v.getContext(),getString(R.string.input_something),Toast.LENGTH_LONG).show();
            		return;
            	}
            	
               	Twitter twitter = TwitterFactory.getSingleton();
               	try {
					Status status = twitter.updateStatus(tweet);
					Toast.makeText(v.getContext(),getString(R.string.tweet_successed),Toast.LENGTH_LONG).show();
				} catch (TwitterException e) {
					Toast.makeText(v.getContext(),getString(R.string.tweet_failed),Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
