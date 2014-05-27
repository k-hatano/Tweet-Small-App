package jp.nita.tweetsmallapp;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sony.smallapp.SmallAppWindow;

public class TweetAsyncTaskCollection {

	final static Handler handler = new Handler();

	public static final String PREF_KEY = "tweetsmallapp";
	public static final String PREF_TOKEN = "token";
	public static final String PREF_SECRET = "secret";

	static RequestToken requestToken=null;

	public class AuthorizationAsyncTask extends AsyncTask<Void, Void, Void>{

		MainApplication superview;

		AuthorizationAsyncTask(MainApplication app){
			super();
			superview=app;
		}

		@Override
		protected Void doInBackground(Void... params) {
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer("TkUccte9JhDJvkOpEfXSSNJkH",
					"ogf2wOHGxA5pmaGgl1DRbu4uKTpghudOkecMGGx2TmKwlmnrp7");
			twitter.setOAuthAccessToken(null);
			try {
				showProgressBar(superview);
				requestToken = twitter.getOAuthRequestToken();
				String url = requestToken.getAuthorizationURL();

				Uri uri = Uri.parse(url);
				Intent i = new Intent(Intent.ACTION_VIEW,uri);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				superview.startActivity(i);
				minimize(superview);
				showToast(superview,superview.getString(R.string.launching_browser));
			} catch (TwitterException e) {
				showToast(superview,superview.getString(R.string.setting_failed));
				e.printStackTrace();
			}finally{
				hideProgressBar(superview);
			}
			return null;
		}

	}

	public class InputPINCodeAsyncTask extends AsyncTask<String, Void, Void>{

		MainApplication superview;

		InputPINCodeAsyncTask(MainApplication app){
			super();
			superview=app;
		}

		@Override
		protected Void doInBackground(String... params) {
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer("TkUccte9JhDJvkOpEfXSSNJkH",
					"ogf2wOHGxA5pmaGgl1DRbu4uKTpghudOkecMGGx2TmKwlmnrp7");
			twitter.setOAuthAccessToken(null);
			try {
				showProgressBar(superview);
				AccessToken accessToken=twitter.getOAuthAccessToken(requestToken,params[0]);
				String token=accessToken.getToken();
				String secret=accessToken.getTokenSecret();

				SharedPreferences pref=superview.getSharedPreferences(PREF_KEY,Activity.MODE_PRIVATE);
				Editor editor=pref.edit();
				editor.putString(PREF_TOKEN, token);
				editor.putString(PREF_SECRET, secret);
				editor.commit();

				accessToken = new AccessToken(token, secret);
				if(accessToken!=null){
					showToast(superview,superview.getString(R.string.authorization_succeed));
				}else{
					showToast(superview,superview.getString(R.string.setting_failed));
				}
			} catch (TwitterException e) {
				showToast(superview,superview.getString(R.string.setting_failed));
				e.printStackTrace();
			}finally{
				hideProgressBar(superview);
			}
			return null;
		}

	}

	public class TweetAsyncTask extends AsyncTask<String, Void, Void>{

		MainApplication superview;

		TweetAsyncTask(MainApplication app){
			super();
			superview=app;
		}

		@Override
		protected Void doInBackground(String... params) {
			SharedPreferences pref=superview.getSharedPreferences(PREF_KEY,Activity.MODE_PRIVATE);
			String token=pref.getString(PREF_TOKEN,"");
			String secret=pref.getString(PREF_SECRET,"");
			if("".equals(token)||"".equals(secret)){
				showToast(superview,superview.getString(R.string.need_to_authorize));
				return null;
			}

			AccessToken accessToken = new AccessToken(token, secret);

			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer("TkUccte9JhDJvkOpEfXSSNJkH",
					"ogf2wOHGxA5pmaGgl1DRbu4uKTpghudOkecMGGx2TmKwlmnrp7");
			twitter.setOAuthAccessToken(accessToken);
			
			if("".equals(params[0])){
				showToast(superview,superview.getString(R.string.input_something));
				return null;
			}
			try {
				showProgressBar(superview);
				twitter4j.Status status = twitter.updateStatus(params[0]);
			} catch (TwitterException e) {
				showToast(superview,superview.getString(R.string.tweet_failed));
				e.printStackTrace();
				return null;
			}finally{
				hideProgressBar(superview);
			}
			showToast(superview,superview.getString(R.string.tweet_succeed));
			resetContent(superview);
			return null;
		}

	}

	public static void showToast(final Context context,final String title){
		new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Toast.makeText(context, title, Toast.LENGTH_LONG).show();
					}
				});
			}
		}).start();
	}
	
	public static void minimize(final MainApplication view){
		new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						view.getWindow().setWindowState(SmallAppWindow.WindowState.MINIMIZED);
					}
				});
			}
		}).start();
	}
	
	public static void showProgressBar(final MainApplication view){
		new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						(view.findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
						(view.findViewById(R.id.lettersCount)).setVisibility(View.GONE);
					}
				});
			}
		}).start();
	}

	public static void hideProgressBar(final MainApplication view){
		new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						(view.findViewById(R.id.progressBar)).setVisibility(View.GONE);
						(view.findViewById(R.id.lettersCount)).setVisibility(View.VISIBLE);
					}
				});
			}
		}).start();
	}

	public static void resetContent(final MainApplication view){
		new Thread(new Runnable(){
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						EditText edit=(EditText)(view.findViewById(R.id.content));
						edit.setText("");
					}
				});
			}
		}).start();
	}

}
