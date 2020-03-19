package com.maxk.notebook.maxkgi;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.maxk.notebook.post.PostListActivity;
import com.parse.ParsePushBroadcastReceiver;

public class MaxkCustomReceiver extends ParsePushBroadcastReceiver {
	
	private static final String TAG = MaxkCustomReceiver.class.getSimpleName();

	@Override
	protected void onPushReceive(Context context, Intent intent) {
		super.onPushReceive(context, intent);
		
		// Push Channel Check
		// if ("background".equals(intent.getStringExtra(ParsePushBroadcastReceiver.KEY_PUSH_CHANNEL))) {
		// .. }
		if( BuildConfig.DEBUG ) {
			Log.w(TAG, "onPushReceive: Puch Channel: " 
					+ intent.getStringExtra(ParsePushBroadcastReceiver.KEY_PUSH_CHANNEL) 
					+ ", Action: " + intent.getAction());
		}
	}
	
	@Override
	protected void onPushOpen(Context context, Intent intent) {

		if( BuildConfig.DEBUG )
			Log.w(TAG, "onPushOpen:");
		
		String uriString = null;
		try {
			JSONObject pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));
			uriString = pushData.optString("uri");
		} catch (JSONException e) {
			Log.v(TAG, "Unexpected JSONException when receiving push data: ", e);
		}
		
		Class<? extends Activity> cls = getActivity(context, intent);
		Intent activityIntent;
		if (uriString != null && !uriString.isEmpty()) {
			activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
		} else {
			activityIntent = new Intent(context, PostListActivity.class);
		}
		activityIntent.putExtras(intent.getExtras());
		if (Build.VERSION.SDK_INT >= 16) {
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			stackBuilder.addParentStack(cls);
			stackBuilder.addNextIntent(activityIntent);
			stackBuilder.startActivities();
		} else {
			activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(activityIntent);
		}
		// MaxkUtils.goPostListActivity( context );
		// context.finish();
		
		// super.onPushOpen(context, intent);
	}
	
	@Override
	protected void onPushDismiss(Context context, Intent intent) {
		super.onPushDismiss(context, intent);
		
		if( BuildConfig.DEBUG )
			Log.v(TAG, "onPushDismiss:");
	}
	
	// Used by onPushOpen to determine which activity to launch or insert into the back stack.
	@Override
	protected  Class<? extends Activity> getActivity(Context context, Intent intent) {
		super.getActivity(context, intent);
		
		if( BuildConfig.DEBUG )
			Log.v(TAG, "getActivity:" + MaxkMainActivity.class.toString());
		
		return MaxkMainActivity.class;	// PostListActivity.class
	}
	
	@Override 
	protected int getSmallIconId(Context context, Intent intent) {
		if( BuildConfig.DEBUG )
			return R.drawable.push_icon;
		else
			return super.getSmallIconId(context, intent);
	}
	
	@Override 
	protected Notification getNotification(Context context, Intent intent) {
		// Your pushes won't create a notification anyway if it has no "alert" or "title" field.
		if( BuildConfig.DEBUG )
			Log.v(TAG, "getNotification:");
		
		return super.getNotification(context, intent);
	}
}