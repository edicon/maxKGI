package com.edicon.lib.parse;

import java.util.LinkedList;
import java.util.Set;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

import com.maxk.notebook.misc.MaxkInfo;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;


public class PushUtil {

	// New Parse Push
	public static void setDefaultPushChannel( Context cx ) {
		String channel = PushUtil.getChannelFromPackage( cx );
		subScribe( channel );
	}
	
	public static void subScribe( String channel) {
		ParsePush.subscribeInBackground(channel);
	}
	
	public static void unsubScribe( String channel ) {
		ParsePush.unsubscribeInBackground( channel );
	}
	
	// get the set of channels that the current device is subscribed
	public static Set<String> getAllChannels( Context cx) {
		Set<String> setOfAllSubscriptions = PushService.getSubscriptions( cx );
		return setOfAllSubscriptions;
	}

	public static LinkedList<String> getPkgChannelList( Context cx ) {
		String pkgChannel = getChannelFromPackage( cx );
		String[] channels = {pkgChannel};
		LinkedList<String> channelList = getChannelList( channels );
		return channelList;
	}
	
	// channel name: must be empty string 
	// or a letter followed by alphanumerics or hyphen
	public static String getChannelFromPackage(Context cx){
		String pkgName = cx.getPackageName();
		pkgName = pkgName.replaceAll("\\.", "-");
		return pkgName;
	}
	
	public static LinkedList<String> getChannelList( String[] channelNames) {
		LinkedList<String> channelList = new LinkedList<String>();
		for( int i = 0; i < channelNames.length; i ++)
			channelList.add(channelNames[i]);
		return channelList;
	}
	
	// Pushes Using Channels
	public static void pushChannel( ParsePush push, LinkedList<String> channels, String pushMessage ) {	 
		push.setChannels(channels); // Notice we use setChannels not setChannel
		push.setMessage(pushMessage);
		push.sendInBackground();
	}
	
	// Store Installation Data
	public static void saveInstallation( String key, Object value ) {
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		installation.put(key, value);
		installation.saveInBackground();
	}
	
	// Advanced Targeting Push Using Query
	public static ParseQuery<?> setQuery( String key, Object value) {
		ParseQuery<?> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereEqualTo(key, value);
		// pushQuery.whereEqualTo("channels", "Giants"); // Set the channel
		return pushQuery;
	}
	
	public static void pushQuery( ParsePush push, ParseQuery<ParseInstallation> pushQuery, String pushMessage ) {	 
		push.setQuery(pushQuery);
		push.setMessage(pushMessage);
		push.sendInBackground();
	}
	
	public static void pushObject( ParsePush push, LinkedList<String> channels, JSONObject data ) {	 
		push.setChannels(channels);
		push.setData(data);
		push.sendInBackground();
	}
	
	// UNIX epoch time, 1408054152: August 14th
	public static void setExpireDate(ParsePush push, long date ) {	 
		push.setExpirationTime(date);
	}
	
	//long weekInterval = 60*60*24*7; // 1 week
	public static void setExpireInterval(ParsePush push, long interval ) {	 
		push.setExpirationTimeInterval(interval);
	}
		
	// platform: android, ios, winrt, winphone
	public static void setPlatform(ParsePush push, String platform) {
		ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
		query.whereEqualTo("deviceType", platform);
		push.setQuery(query);
	}
	
	// 해당 Push Channel sub/unsubscribe
	public static void setPushSubscribe( Context cx  ) {
		String pkgChannel = PushUtil.getChannelFromPackage( cx );
		if( MaxkInfo.pushOn ) {
			PushUtil.subScribe(pkgChannel);
		} else {
			PushUtil.unsubScribe(pkgChannel);
		}
	}
}
