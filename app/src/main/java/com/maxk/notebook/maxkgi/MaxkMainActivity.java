package com.maxk.notebook.maxkgi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.edicon.lib.parse.PushUtil;
import com.idescout.sql.SqlScoutServer;
import com.maxk.notebook.misc.MaxkInfo;
import com.maxk.notebook.misc.MaxkUtils;

import static com.maxk.notebook.misc.MaxkUtils.getAppDataVersion;


public class MaxkMainActivity extends FragmentActivity {
    
	private Context context;
	private final int REQUEST_READ_PHONE_STATE = 301;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.maxk_main);

        context = this;

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            initMain();
        }
    }

    private void initMain() {
        // IDEScout.com
        // if( BuildConfig.DEBUG )
        //    SqlScoutServer.create(context, context.getPackageName());

        // getAppDataVersion( context );

        SharedPreferences sp 	= PreferenceManager.getDefaultSharedPreferences(this);
        MaxkInfo.pushOn 		= sp.getBoolean("PUSH_ON", true);
        MaxkInfo.dataLoading 	= sp.getBoolean("APP_DATA_LOADING", false);
        MaxkInfo.appDataVersion = sp.getLong("APP_DATA_VER", 0L);
        MaxkInfo.phoneNo		= MaxkUtils.getPhoneNo(this);

        if( BuildConfig.DEBUG && MaxkInfo.LOGIN_TEST )
            MaxkInfo.phoneNo = "01047160022"; // Admin: 01031620365, 01038130453, 01092487258

        MaxkInfo.logOn = MaxkUtils.queryLogin( this, MaxkInfo.phoneNo );
        // MaxkInfo.logOn = true;
        if (!MaxkInfo.logOn) {
            MaxkInfo.pushOn = false;
        }
        PushUtil.setPushSubscribe(this);

        // ToDo: Check Upgrade and Update
        if (!MaxkInfo.dataLoading) {
            MaxkUtils.getMemberFromSheet(this);
        } else {
            MaxkUtils.updateMember(this);
        }

        ImageButton mainButton1 = (ImageButton)findViewById(R.id.mainButton1);
        ImageButton mainButton2 = (ImageButton)findViewById(R.id.mainButton2);
        ImageButton mainButton3 = (ImageButton)findViewById(R.id.mainButton3);

        mainButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MaxkUtils.goGroupListActivity( context );
            }
        });

        mainButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MaxkUtils.goPostListActivity( context );
            }
        });

        mainButton3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MaxkUtils.goInfoGroupActivity( context );
            }
        });

        MaxkUtils.initMenu( (Activity)context );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initMain();
                }
                break;
            default:
                break;
        }
    }
}