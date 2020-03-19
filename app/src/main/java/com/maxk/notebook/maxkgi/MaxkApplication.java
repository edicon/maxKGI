package com.maxk.notebook.maxkgi;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.edicon.lib.volly.Volly;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.maxk.notebook.misc.MaxkInfo;
import com.maxk.notebook.stetho.OptionalDependencies;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class MaxkApplication extends Application {

	private final static String YOUR_APPLICATION_ID = MaxkInfo.YOUR_APPLICATION_ID;
	private final static String YOUR_CLIENT_KEY 	= MaxkInfo.YOUR_CLIENT_KEY;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		// Multi-Dex
		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		MaxkInfo.volly = new Volly(getApplicationContext());
		// Add your initialization code here
		Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);

		if (!FirebaseApp.getApps(this).isEmpty()) {
			FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		}

		// Facebook Stetho
		new OptionalDependencies(this).initialize();
	}
}
