package com.maxk.notebook.misc;

import com.edicon.lib.parse.PushUtil;
import com.maxk.notebook.maxkgi.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends FragmentActivity {
	
    private Context context;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings);
        
        context = this;
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        MaxkInfo.pushOn 		   = sp.getBoolean("PUSH_ON", true);

        CheckBox pushOnBox = (CheckBox) findViewById(R.id.push_on);
        
        TextView helpInfo = (TextView) findViewById(R.id.help_info);
        TextView useInfo = (TextView) findViewById(R.id.use_info);
        TextView verInfo = (TextView) findViewById(R.id.ver_info);
        
        helpInfo.setOnClickListener(new helpInfoOnClickListener());
        useInfo.setOnClickListener(new useInfoOnClickListener());
        verInfo.setOnClickListener(new verInfoOnClickListener());
        
        pushOnBox.setChecked(MaxkInfo.pushOn);
        pushOnBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.push_on) {
                    if (isChecked) {
                    	MaxkInfo.pushOn = true;
                    } else {
                    	MaxkInfo.pushOn = false;
                    }
                }
                
    			if (!MaxkInfo.logOn) {
    				MaxkInfo.pushOn = false;
    			}
    			
    	        Editor edit = sp.edit();
    	        edit.putBoolean("PUSH_ON", MaxkInfo.pushOn);
    	        edit.commit();
    	        
    			PushUtil.setPushSubscribe(context);
            }
        });
        
        MaxkUtils.initPopupMenu((Activity)context);
        MaxkUtils.initMenu((Activity)context);
    }

	private class helpInfoOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
			if (!MaxkInfo.logOn) {
				MaxkUtils.loginError(context);
				return;
			}
			
			MaxkInfo.infoType = MaxkInfo.INFO_TYPE_HELP;
			MaxkUtils.goInfoActivity(context);
		}
	}
	
	private class useInfoOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			
			if (!MaxkInfo.logOn) {
				MaxkUtils.loginError(context);
				return;
			}
			
			MaxkInfo.infoType = MaxkInfo.INFO_TYPE_USE;
			MaxkUtils.goInfoActivity( context);
		}
	}
	
	private class verInfoOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			PackageInfo i;
			try {
				i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
				String version = i.versionName;
				String msg = "사용중인 앱 버전은 V" + version + "입니다.";
				Toast toast= Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}