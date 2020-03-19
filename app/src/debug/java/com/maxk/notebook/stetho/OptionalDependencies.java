package com.maxk.notebook.stetho;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.idescout.sql.SqlScoutServer;
import com.maxk.notebook.maxkgi.BuildConfig;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// https://androidhub.intel.com/en/posts/blundell/Inspecting_databases_with_Stetho.html
public class OptionalDependencies {
    private final Context context;

    public OptionalDependencies(Context context) {
        this.context = context;
    }
    public void initialize() {

        if(BuildConfig.DEBUG ) {
            initializeStetho(context);
            SqlScoutServer.create(context, context.getPackageName());
        } else
            Stetho.initializeWithDefaults(context);

        // Start Network Thread for Demo
        new Thread() {
            @Override
            public void run() {
                initNetConn();
            }
        }.start();
    }

    // http://code.tutsplus.com/tutorials/debugging-android-apps-with-facebooks-stetho--cms-24205
    private void initializeStetho( Context context ) {
        long startTime = SystemClock.elapsedRealtime();

        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(context);
        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector( Stetho.defaultInspectorModulesProvider(context) );
        // Enable command line interface
        initializerBuilder.enableDumpapp( Stetho.defaultDumperPluginsProvider(context) );
        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();
        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);

        long elapsed = SystemClock.elapsedRealtime() - startTime;
        Log.i("APP", "Stetho initialized in " + elapsed + " ms");
    }


    private void initNetConn(){
        // Create an instance of OkHttpClient and Add Stetho interceptor
        // OkHttpClient httpClient = new OkHttpClient();
        // httpClient.networkInterceptors().add(new StethoInterceptor());
        OkHttpClient httpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();
        try {
            // Fetch the contents of http://httpbin.org/ip
            Response response = httpClient.newCall( new Request.Builder().url("http://httpbin.org/ip").build()
            ).execute();
        } catch(IOException ioe) {
            Log.d("StethoTut", ioe.getMessage());
        }
    }
}
