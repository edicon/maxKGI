package com.edicon.lib.parse;

import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;

import com.maxk.notebook.misc.MaxkInfo;
import com.maxk.notebook.maxkgi.R;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

public class PushActivity extends Activity {
	
	/** Called when the activity is first created. */
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_main);

		ParseAnalytics.trackAppOpened(getIntent());
		ParseInstallation pi = ParseInstallation.getCurrentInstallation();
		
		String pkgChannel = PushUtil.getChannelFromPackage( this );
		if( MaxkInfo.pushOn) {
			// When users indicate they are Giants fans, we subscribe them to that channel.
			PushUtil.subScribe( pkgChannel );
			
			String[] channels = {pkgChannel};
			LinkedList<String> pushChannels = PushUtil.getChannelList(channels);
			ParsePush push = new ParsePush();
			boolean pushChannel = true;
			if( pushChannel )
				PushUtil.pushChannel( push, pushChannels, pkgChannel + ": The Giants won against the Mets 2-3.");
			else {
				JSONObject data;
				try {
					data = new JSONObject("{\"action\": \"com.maxk.notebook.univ.UPDATE_STATUS\", \"name\": \"Vaughn\", \"newsItem\": \"Man bites dog\"}");
					 PushUtil.pushObject( push,  pushChannels, data);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// When users indicate they are no longer Giants fans, we unsubscribe them.
		// PushService.unsubscribe(this, "Giants");
	}
}
