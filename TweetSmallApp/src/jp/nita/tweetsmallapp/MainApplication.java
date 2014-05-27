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

import jp.nita.tweetsmallapp.TweetAsyncTaskCollection.AuthorizationAsyncTask;
import jp.nita.tweetsmallapp.TweetAsyncTaskCollection.InputPINCodeAsyncTask;
import jp.nita.tweetsmallapp.TweetAsyncTaskCollection.TweetAsyncTask;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.sony.smallapp.SmallAppWindow;
import com.sony.smallapp.SmallApplication;

public class MainApplication extends SmallApplication {

	static Handler handler;

	TextView tx;

	@Override
	public void onCreate() {
		super.onCreate();
		setContentView(R.layout.main);
		setTitle(R.string.app_name);

		handler = new Handler();

		SmallAppWindow.Attributes attr = getWindow().getAttributes();
		attr.minWidth = 360; /* The minimum width of the application, if it's resizable.*/
		attr.minHeight = 360; /*The minimum height of the application, if it's resizable.*/
		attr.width = 480;  /*The requested width of the application.*/
		attr.height = 400;  /*The requested height of the application.*/
		attr.flags |= SmallAppWindow.Attributes.FLAG_RESIZABLE;   /*Use this flag to enable the application window to be resizable*/
		//        attr.flags |= SmallAppWindow.Attributes.FLAG_NO_TITLEBAR;  /*Use this flag to remove the titlebar from the window*/
		//        attr.flags |= SmallAppWindow.Attributes.FLAG_HARDWARE_ACCELERATED;  /* Use this flag to enable hardware accelerated rendering*/

		getWindow().setAttributes(attr); /*setting window attributes*/

		findViewById(R.id.auth).setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				
				((InputMethodManager)MainApplication.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
		
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
				alertDialogBuilder.setTitle(v.getContext().getString(R.string.authorize));
				alertDialogBuilder.setMessage(v.getContext().getString(R.string.request_or_input));
				alertDialogBuilder.setPositiveButton(v.getContext().getString(R.string.request),
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						TweetAsyncTaskCollection collection=new TweetAsyncTaskCollection();
						AuthorizationAsyncTask task=collection.new AuthorizationAsyncTask(MainApplication.this);
						task.execute();
					}
				});
				alertDialogBuilder.setNeutralButton(v.getContext().getString(R.string.input),
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						inputPINCode(v);
					}
				});
				alertDialogBuilder.setNegativeButton(v.getContext().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				alertDialogBuilder.setCancelable(true);
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});

		findViewById(R.id.tweet).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String tweet=((EditText)findViewById(R.id.content)).getText().toString();
				TweetAsyncTaskCollection collection=new TweetAsyncTaskCollection();
				TweetAsyncTask task=collection.new TweetAsyncTask(MainApplication.this);
				task.execute(tweet);
			}
		});
		
		((EditText)findViewById(R.id.content)).addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				EditText content = ((EditText)findViewById(R.id.content));
				int length=content.getText().length();
				((TextView)findViewById(R.id.lettersCount)).setText(""+length);
				if(length>140){
					((TextView)findViewById(R.id.lettersCount)).setTextColor(getResources().getColor(R.color.red));
				}else{
					((TextView)findViewById(R.id.lettersCount)).setTextColor(getResources().getColor(R.color.ocean));
				}
			}
		});
	}

	public void inputPINCode(final View v){
		final EditText editView = new EditText(v.getContext());
		new AlertDialog.Builder(v.getContext())
		.setTitle(v.getContext().getString(R.string.input))
		.setMessage(v.getContext().getString(R.string.input_pin_code))
		.setView(editView)
		.setPositiveButton(v.getContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				TweetAsyncTaskCollection collection=new TweetAsyncTaskCollection();
				InputPINCodeAsyncTask task=collection.new InputPINCodeAsyncTask(MainApplication.this);
				task.execute(editView.getText().toString());
			}
		})
		.setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		})
		.show();
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
